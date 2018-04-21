package user_interface

import processing._
import processing.core._
import scala.util.Random
import files._
import map._
import objects._
import general.Helper

class Menu(val g: Game) extends Helper(g) {
  val game = g.asInstanceOf[PApplet]
  val store = new Store(this)
  
  val fullWidth = aWidth + mWidth
  
  //determines whether the mouse is on the menu
  def onMenu = mSqX > aWidth-1
  
  var buyT = false      //keeps track of if the player is buying towers currently
  var menuCol = (0,255) //changes the menu buttons from green to red and back
  var menuChoose = 0    //transparency of the menu button
  
  var storeMenu: StoreMenu = null
  val infoScreen = new InfoScreen(this)
  
  def doStuff() = {
    
    game.image(g.menuS(0),sqSize*aWidth,0,sqSize*mWidth,sqSize*aHeight)
    
    
    game.textFont(g.font,16)
    game.fill(0)  
    game.text("Val's Tower Defence",sqSize*aWidth+ 5,21)
    game.fill(46, 204, 113)
    game.text("Val's Tower Defence",sqSize*aWidth+ 4,20)
    game.fill(0) 
    game.textFont(g.font,20)
    game.text("Money:   " + player.money,sqSize*aWidth+ 9,61)
    game.fill(255, 255, 0)
    game.text("Money:   " + player.money,sqSize*aWidth+ 8,60)
    game.fill(0) 
    game.textFont(g.font,22)
    game.text("HP:       " + player.hp,sqSize*aWidth+ 9,91)
    game.fill(255, 0, 0)
    game.text("HP:       " + player.hp,sqSize*aWidth+ 8,90)
    ////////////////////////////////////////////////////////
    
    game.stroke(1)
    
    
    infoScreen.doStuff()
      
    
    drawWaveButton()
    
    drawMuteButton()
    
    if (storeMenu != null && storeMenu.toggled)
      storeMenu.doStuff()
      
      
    highlight()
  }
  
  def highlight() = {
    if (!onMenu && !buyT) {
      game.image(g.highlight(0),(mSqX)*sqSize, 
                 (mSqY)*sqSize, sqSize, sqSize)
    }
  }
  
  // MUTE BUTTON ///////////////////////////////////////////////////
  val muteLoc = (fullWidth * sqSize - sqSize + sqSize/8.0.toFloat, 
                   aHeight * sqSize - sqSize + sqSize/8.0.toFloat)
  
  def drawMuteButton() = {
    if (g.sounds.muted)
      game.image(g.muteButton(1),muteLoc._1,muteLoc._2)
    else
      game.image(g.muteButton(0),muteLoc._1,muteLoc._2)
  }
  
  def onMuteButton = (mSqX > fullWidth - 2) && (mSqY > aHeight - 2)
  ///////////////////////////////////////////////////////////////////
  
  // WAVE BUTTON /////////////////////////////////////////////////////////////////////////
  val waveButtonPos = (aWidth*sqSize + sqSize/2,sqSize*10 + sqSize/2)
  val waveButtonSize = (sqSize*3,sqSize*2)
  
  def onWaveButton = (mouseX > waveButtonPos._1) && (mouseY > waveButtonPos._2) &&
                     (mouseX < waveButtonPos._1 + waveButtonSize._1) && 
                     (mouseY < waveButtonPos._2 + waveButtonSize._2)
                
  def drawWaveButton() = {
    var color = (150,0,0)
    var text  = "  Wave In\n Progress"
    if (g.currentWave.isComplete) {
      color = (0,150,0)
      text  = "Start Next\n    Wave"
    }
      
    def stuff = {
      game.rect(waveButtonPos._1,waveButtonPos._2,waveButtonSize._1,waveButtonSize._2)
      game.textFont(g.font,12)
      game.fill(0) 
      game.text(text,waveButtonPos._1 + sqSize*3/4 +2,waveButtonPos._2 + sqSize*7/8 + 2)
      game.fill(250) 
      game.text(text,waveButtonPos._1 + sqSize*3/4,waveButtonPos._2 + sqSize*7/8)
    }
    
    if (onWaveButton) {
      game.fill(color._1,color._2,color._3,160)
      stuff
    } else {
      game.fill(color._1,color._2,color._3,100)
      stuff
    }
  }
  /////////////////////////////////////////////////////////////////////////////////////////
  
  
  def mouseSq = {
    arena.squares(mSqX)(mSqY)
  }
  
  
  /** What happens when the mouse is over a tower on the arena. */
  def mouseTower() = {
    if (!onMenu && mouseSq.isInstanceOf[Tower]) {
      val t = mouseSq.asInstanceOf[Tower]
      if (!t.hasDef) {
        infoScreen.title("Empty Tower")
        infoScreen.description("Click to see\nDefences")
      } else if (t.getDef.isInstanceOf[BasicDefence]) {
        val d = t.getDef.asInstanceOf[BasicDefence]
        infoScreen.title("Crossbow\nDefence")
        infoScreen.description("A Basic Defence.")
        infoScreen.stats(d.damage.toInt,d.range,d.speed,d.cost)
      } else if (t.getDef.isInstanceOf[IceDefence]) {
        val d = t.getDef.asInstanceOf[IceDefence]
        infoScreen.title("Ice Defence")
        infoScreen.description("Slows the mobs\nwithin range.\nDoes no damage.")
        infoScreen.stats(d.damage.toInt,d.range,d.speed,d.cost)
      }
        
    }
  }
  
  
  def mouseEmpty() = {
    if (!onMenu && mouseSq.isInstanceOf[Empty]) {
      infoScreen.title("Empty Tile")
      infoScreen.description("Click to \nBuy Tower")
      infoScreen.stats(0,0,0,5)
    }
  }
  
  def mousePath() = {
    if (!onMenu && mouseSq.isInstanceOf[Path]) {
      infoScreen.title("Path")
      infoScreen.description("Click to Buy\nTrap")
    }
  }
  
  def mouseObs() = {
    if (!onMenu && mouseSq.isInstanceOf[Obstacle]) {
      infoScreen.title("Obstacle")
      infoScreen.description("Cannot Buy \nAnything Here")
    }
  }
  
  
  def infoScreenText() = {
    mouseTower()
    mouseEmpty()
    mousePath()
    mouseObs()
  }
  
 
  def clickingStuff() = {
    
    if (game.mouseButton == leftMouse) {
    
      // when the mouse is on the arena
      if (!onMenu) {
        val sq = arena.squares(mSqX)(mSqY)
        
        if (storeMenu != null) {
          if (storeMenu.mouseOn) {
            storeMenu.whenClicked()
          } else {
            if (sq.isInstanceOf[Tower]) {
              val t = sq.asInstanceOf[Tower]
              if (storeMenu.t != t) 
                storeMenu = new StoreMenu(t,store)
              else
                storeMenu.toggle()
            } else if (!sq.isInstanceOf[Tower] && !storeMenu.toggled) {
              store.buyTower(mSqX,mSqY)
            } else {
              storeMenu.toggle()
            }
          }
        } else if (sq.isInstanceOf[Tower]) {
          val t = sq.asInstanceOf[Tower]
          storeMenu = new StoreMenu(t,store)
        } else if (sq.isInstanceOf[Empty]) {
          store.buyTower(mSqX,mSqY)
        }
        // when the mouse is on the menu
      } else {
        // mute button
        if (onMuteButton) {
          g.sounds.toggleMute()
        }
        // wave button
        if (onWaveButton) {
          g.nextWave()
        } 
      }
      
      if (g.level.isComplete) {
        
      }
      
    }
    
  }
  
}
