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
  
  
  //determines whether the mouse is on the menu
  def onMenu = mSqX > aWidth-1
  
  var buyT = false      //keeps track of if the player is buying towers currently
  var menuCol = (0,255) //changes the menu buttons from green to red and back
  var menuChoose = 0    //transparency of the menu button
  
  
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
    
    
    highlight()
  }
  
  def highlight() = {
    if (!onMenu && !buyT) {
      game.image(g.highlight(0),(mSqX)*sqSize, 
                 (mSqY)*sqSize, sqSize, sqSize)
    }
  }
  
  
  def clickingStuff() = {
    if (game.mouseButton == leftMouse && !onMenu) {
      if (arena.squares(mSqX)(mSqY).isInstanceOf[Tower]) 
        store.buyDef(arena.squares(mSqX)(mSqY).asInstanceOf[Tower],store.basicDef)
      else
        store.buyTower(mSqX,mSqY)
    }
  }
  
  
}


class Store(m: Menu) extends Helper(m.g) {
  val g = m.g      //game
  
  def basicDef  = new BasicDefence(m.arena.towers(mSqX)(mSqY),140,60,3,5,m.g)
  def iceDef    = new IceDefence(m.arena.towers(mSqX)(mSqY),80,20,1,5,m.g,0.5.toFloat)
  def fireDef   = new FireDefence(m.arena.towers(mSqX)(mSqY),100,10,3,5,m.g)
  
  def buyDef(t: Tower, d: Defence) = {
    if (player.money >= d.cost)
      if (t.addDefence(d)) {
        g.sounds.play(g.sounds.crossbow)
        player.money -= d.cost
      }
  }

  def buyTower(x: Int, y: Int) = {
    if (player.money > 4)
      if (arena.setTower(x,y)) {
        g.sounds.play(g.sounds.build)
        player.money -= 5
      }
  }
  
}

