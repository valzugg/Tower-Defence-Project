package user_interface

import processing._
import processing.core._
import scala.util.Random
import file_parser._
import arena._
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
  
  def doStuff() = {
    
    game.image(g.menuS(0),sqSize*20,0,sqSize*4,sqSize*15)
    
    
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
    
    for (row <- 0 until 4) {
      for (col <- 0 until 2) {
        if (game.mouseX > sqSize*21 + col*sqSize && game.mouseX < sqSize*22 + col*sqSize &&
          game.mouseY > sqSize*3 + row*sqSize && game.mouseY < sqSize*4 + row*sqSize) {
          game.fill(menuCol._1,menuCol._2, 0,menuChoose + 80)
          game.rect((mSqX)*sqSize,(mSqY)*sqSize,sqSize,sqSize)
        }
      }
    }
      
    
    game.noFill()
    game.stroke(0,0,0)
    game.rect(20*sqSize + sqSize/2,11*sqSize,4*sqSize - sqSize,3*sqSize)
    game.fill(255, 0, 0)
    game.text("Next",20*sqSize + sqSize,12*sqSize)
    game.text("Wave",20*sqSize + sqSize,13*sqSize)
    
    // MUTE BUTTON //////////////////////////////////////////////////
    val muteLoc = (fullWidth * sqSize - sqSize + sqSize/8.0.toFloat, 
                   aHeight * sqSize - sqSize + sqSize/8.0.toFloat)
    
    if (g.sounds.muted)
      game.image(g.muteButton(1),muteLoc._1,muteLoc._2)
    else
      game.image(g.muteButton(0),muteLoc._1,muteLoc._2)
    /////////////////////////////////////////////////////////////////
    
      

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
  
  
  def onMuteButton = (mSqX > fullWidth - 2) && (mSqY > aHeight - 2)
  
  
  def clickingStuff() = {
    if (game.mouseButton == leftMouse && !onMenu) {
      val sq = arena.squares(mSqX)(mSqY)
      if (sq.isInstanceOf[Tower]) {
        val t = sq.asInstanceOf[Tower]
        if (storeMenu == null || storeMenu.t != t)
          storeMenu = new StoreMenu(t,store)
        else
          storeMenu.toggle()
      } else if (storeMenu != null) {
        if (storeMenu.mouseOn) {
          if (storeMenu.mouseOn(1)) {
            if (store.buyDef(storeMenu.t,store.basicDef(storeMenu.t)))
              storeMenu.toggle()
          } else if (storeMenu.mouseOn(2)) {
            if (store.buyDef(storeMenu.t,store.iceDef(storeMenu.t)))
              storeMenu.toggle()
          }
        } else if (storeMenu.toggled)
          storeMenu.toggle()
        else
          store.buyTower(mSqX,mSqY)
      }
    } else if (game.mouseButton == leftMouse && onMuteButton) {
      g.sounds.toggleMute()
    }
  }
  
  
}

/** A menu which is created whenever a tower is clicked. */
class StoreMenu(val t: Tower, s: Store) extends Helper(s.g) {
  val pos = (t.pos._1 + sqSize/2,t.pos._2 - sqSize/2)
  var toggled = true
  
  val size = 2
  
  def toggle() = {
    if (toggled)
      toggled = false
    else
      toggled = true
  }
  
  def mouseOn(n: Int) = {
    toggled &&
    (mouseX > pos._1 && mouseY > pos._2 + (n - 1) * sqSize) &&
    (mouseX < pos._1 + sqSize && mouseY < pos._2  + n * sqSize)
  }
  
  def mouseOn = {
    toggled &&
    (mouseX > pos._1 && mouseY > pos._2) &&
    (mouseX < pos._1 + sqSize && mouseY < pos._2  + size * sqSize)
  }
  
  def setImg(img: PImage, spot: Int) = {
    s.g.image(img,pos._1,pos._2 + (sqSize* spot),sqSize,sqSize)
  }
  
  def doStuff() = {
    s.g.fill(150,150,150,100)
    s.g.rect(pos._1,pos._2,sqSize,size*sqSize)
    setImg(s.g.defences(0),0)
    setImg(s.g.defences(1),1)
  }
  
}


class Store(m: Menu) extends Helper(m.g) {
  val g = m.g      //game
  
  def basicDef(t: Tower) = new BasicDefence(t,110,60,3,5,m.g)
  def iceDef(t: Tower)   = new IceDefence(t,110,20,2,5,m.g,0.8.toFloat)
  def fireDef   = new FireDefence(m.arena.towers(mSqX)(mSqY),100,10,3,5,m.g)
  
  def buyDef(t: Tower, d: Defence): Boolean = {
    if (player.money >= d.cost)
      if (t.addDefence(d)) {
        g.sounds.play(g.sounds.crossbow)
        player.money -= d.cost
        true
      } else {
        false
      }
    else 
      false
  }

  def buyTower(x: Int, y: Int) = {
    if (player.money > 4)
      if (arena.setTower(x,y)) {
        g.sounds.play(g.sounds.build)
        player.money -= 5
      }
  }
  
}

