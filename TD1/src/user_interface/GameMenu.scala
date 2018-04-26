/**@author Valtteri Kortteisto */
package user_interface

import processing._
import processing.core._
import scala.util.Random
import files._
import map._
import objects._
import general.Helper

class GameMenu(g: Game) extends Helper(g) {
  val game = g.asInstanceOf[PApplet]
  val store = new Store(this)
  
  val fullWidth = aWidth + mWidth
  
  //determines whether the mouse is on the menu
  def onMenu = mSqX > aWidth-1
  
  var storeMenu: StoreMenu = null
  val infoScreen = new InfoScreen(this)
  
  /** All the methods called doStuff() are called in the Game's main draw loop.
   *  They use processing specific methods to draw what is needed every frame, 
   *  and call other methods to change or check things every frame. */
  def doStuff() = {
    game.noStroke()
    
    game.image(g.menuS(0),sqSize*aWidth,0,sqSize*mWidth,sqSize*aHeight)
    game.fill(0) 
    game.textFont(g.font,20)
    game.text(" Money:   " + player.money,sqSize*aWidth+ 9,51)
    game.fill(255, 255, 0)
    game.text(" Money:   " + player.money,sqSize*aWidth+ 8,50)
    game.fill(0) 
    game.textFont(g.font,22)
    game.text("   HP:    " + player.hp,sqSize*aWidth+ 9,81)
    game.fill(255, 0, 0)
    game.text("   HP:    " + player.hp,sqSize*aWidth+ 8,80)
    
    infoScreen.doStuff()
      
    drawWaveButton()
    drawFastButton()
    drawMuteButton()
    drawMenuButton()
    
    if (storeMenu != null && storeMenu.isToggled)
      storeMenu.doStuff()
      
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
      
    def draw() = {
      game.rect(waveButtonPos._1,waveButtonPos._2,waveButtonSize._1,waveButtonSize._2)
      game.textFont(g.font,12)
      game.fill(0) 
      game.text(text,waveButtonPos._1 + sqSize*3/4 +1,waveButtonPos._2 + sqSize*7/8 + 1)
      game.fill(250) 
      game.text(text,waveButtonPos._1 + sqSize*3/4,waveButtonPos._2 + sqSize*7/8)
    }
    
    if (onWaveButton) {
      game.fill(color._1,color._2,color._3,160)
      draw()
    } else {
      game.fill(color._1,color._2,color._3,100)
      draw()
    }
  }
  /////////////////////////////////////////////////////////////////////////////////////////
  
  // FASTFORWARD BUTTON //////////////////////////////////////////////////////////////////////
  val fastButtonPos = (aWidth*sqSize + sqSize/2,sqSize*12 + sqSize)
  val fastButtonSize = (sqSize*3,sqSize)
  
  def onFastButton = (mouseX > fastButtonPos._1) && (mouseY > fastButtonPos._2) &&
                     (mouseX < fastButtonPos._1 + fastButtonSize._1) && 
                     (mouseY < fastButtonPos._2 + fastButtonSize._2)
                
  def drawFastButton() = {
    var color = (100,100,100)
    var text  = "     >  "
    if (g.isFast) {
      color = (0,0,150)
      text  = "  >>>> "
    }
      
    // A bit wetwet, but there are some details that make it difficult to generalize 
    def draw() = {
      game.rect(fastButtonPos._1,fastButtonPos._2,fastButtonSize._1,fastButtonSize._2)
      game.textFont(g.font,12)
      game.fill(0) 
      game.text(text,fastButtonPos._1 + sqSize +1,fastButtonPos._2 + sqSize*5/8 + 1)
      game.fill(250) 
      game.text(text,fastButtonPos._1 + sqSize,fastButtonPos._2 + sqSize*5/8)
    }
    
    if (onFastButton) {
      game.fill(255,255,255,160)
      draw()
      if (g.isFast) {
        infoScreen.title("Return to\nNormal \nSpeed")
        infoScreen.description("\n         (Enter)")
      } else {
        infoScreen.title("Fastforward")
        infoScreen.description("\n         (Enter)")
      }
    } else {
      game.fill(color._1,color._2,color._3,100)
      draw()
    }
  }
  /////////////////////////////////////////////////////////////////////////////////////////
  
  // MENU BUTTON //////////////////////////////////////////////////////////////////////
  val menuButtonPos = (aWidth*sqSize + sqSize/2,sqSize*17 + sqSize/2)
  val menuButtonSize = (sqSize*3,sqSize)
  
  def onMenuButton = (mouseX > menuButtonPos._1) && (mouseY > menuButtonPos._2) &&
                     (mouseX < menuButtonPos._1 + menuButtonSize._1) && 
                     (mouseY < menuButtonPos._2 + menuButtonSize._2)
                
  def drawMenuButton() = {
    val color = (100,100,50)
    val text  = "  Menu"
      
    // A bit wetwet, but there are some details that make it difficult to generalize 
    def draw() = {
      game.rect(menuButtonPos._1,menuButtonPos._2,menuButtonSize._1,menuButtonSize._2)
      game.textFont(g.font,12)
      game.fill(0) 
      game.text(text,menuButtonPos._1 + sqSize +1,menuButtonPos._2 + sqSize*5/8 + 1)
      game.fill(250) 
      game.text(text,menuButtonPos._1 + sqSize,menuButtonPos._2 + sqSize*5/8)
    }
    
    if (onMenuButton) {
      game.fill(255,255,255,160)
      draw()
      infoScreen.title("Pause the\ngame and\nopen menu")
      infoScreen.description("\n         (Tab)")
      
    } else {
      game.fill(color._1,color._2,color._3,100)
      draw()
    }
  }
  /////////////////////////////////////////////////////////////////////////////////////////
  
  def mouseSq = {
    if (mSqX >= 0 && mSqX < aWidth && 
        mSqY >= 0 && mSqY < aHeight)
      arena.squares(mSqX)(mSqY)
    else
      new Obstacle(-1,-1) 
    // prevents the problems from the player accidentally clicking out of bounds
  }
  
  
  def mouseStoreMenu() = {
    if (storeMenu != null && storeMenu.mouseOn) {
      val c = storeMenu.mouseCell
      infoScreen.title(c._2)
      infoScreen.description(c._3)
      infoScreen.stats(c._4._2,c._4._1,c._4._3)
      infoScreen.cost(c._5)
    }
  }
  
  /** Decides what happens in the infoScreen when the mouse is over the arena. */
  def mouseArena() = {
    if (!onMenu) {
     val sq = mouseSq
     sq match {
        case Empty(_,_) => {
          infoScreen.title("Empty Tile")
          infoScreen.description("Click to \nBuy Tower")
          infoScreen.cost(store.towerCost)
        }
        case Path(_,_) => {
          infoScreen.title("Path")
          infoScreen.description("Cannot Buy \nAnything Here")
        }
        case Obstacle(_,_) => {
          infoScreen.title("Obstacle")
          infoScreen.description("Cannot Buy \nAnything Here")
        }
        case _ => {
          val t = sq.asInstanceOf[Tower]
          if (!t.hasDef) {
            infoScreen.title("Empty Tower")
            infoScreen.description("Click to see\nDefences")
          } else if (t.getDef.isInstanceOf[Defence]) {
            val d = t.getDef.asInstanceOf[Defence]
            infoScreen.title(d.title)
            infoScreen.description(d.description)
            infoScreen.stats(d.damage.toInt,d.range,d.speed)
          }
        }
      }
    }
  }
  
  /** Writes the text on the infoScreen. */
  def infoScreenText() = {
    if (storeMenu != null && storeMenu.mouseOn) {
      mouseStoreMenu()
    } else {
      mouseArena()
    }
  }
  
  /** Decides what happens with clicks in the game.
   *  A bit of a mess, could have been done more in separate methods.*/
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
            } else if (!sq.isInstanceOf[Tower] && !storeMenu.isToggled) {
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
          g.level.nextWave()
        } 
        // fastforward button
        if (onFastButton) {
          g.toggleRunSpeed()
        } 
        // menu/pause button
        if (onMenuButton) {
          g.togglePause()
        } 
      }

    }
    
  }
  
}
