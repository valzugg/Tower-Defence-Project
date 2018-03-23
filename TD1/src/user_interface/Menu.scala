package user_interface

import processing._
import processing.core._
import scala.util.Random
import file_parser._
import arena._
import objects._

class Menu(val g: Game) {
  val sqSize = Square.size
  val game = g.asInstanceOf[PApplet]
  def arena = g.arena
  val store = new Store(this)
  
  //mouse's location in the grid's coodinates
  def mSqX = game.mouseX.toInt/sqSize
  def mSqY = game.mouseY.toInt/sqSize
  
  //determines whether the mouse is on the menu
  def onMenu = mSqX > aWidth-1
  
  def player  = g.player
  val aWidth  = 20
  val aHeight = 15
  
  var buyT = false      //keeps track of if the player is buying towers currently
  var menuCol = (0,255) //changes the menu buttons from green to red and back
  val leftMouse = 37
  var menuChoose = 0    //transparency of the menu button
  
  
  def doStuff() = {
    if (g.fr < 1) { 
      game.image(g.menuS(0),sqSize*20,sqSize*3,sqSize*4,sqSize*12)
    }
    
    game.image(g.menuS(1),sqSize*20,0,sqSize*4,sqSize*3)
    
    //TODO: Tämä koodi lyhyemmäksi
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
      
      
    game.fill(180,180,180,100-menuChoose)
    for (row <- 0 until 4) {
      for (col <- 0 until 2) {
        game.rect(sqSize * 21 + col * sqSize, 
             sqSize * 3 + row * sqSize, sqSize, sqSize)
      }
    }
    
    game.image(g.squares(2),sqSize*21,sqSize*3)
    
    //println(aWidth)
  }
  
  def buyingShit() = {
    if (player.money > 4) { //menu color turns off when not enough money
      menuCol = (0, 255) //when the player has enough money, the menu turns green
      if (!onMenu && buyT) {
        if (arena.squares(mSqX)(mSqY).isInstanceOf[Empty]) {
          game.image(g.squares(2),(mSqX)*sqSize, 
                     (mSqY)*sqSize, sqSize, sqSize)
        } else {
          game.image(g.squares(3),(mSqX)*sqSize, 
                     (mSqY)*sqSize, sqSize, sqSize)
        }
      } 
    } else {
      menuChoose = 0        //the color's opacity the the cursor isn't there goes to zero
      menuCol    = (255, 0) //when the player doesnt have enough money, the menu turns red
      buyT       = false
    }
  }
  
  
  def clickingStuff() = {
    if (game.mouseButton == leftMouse) {
      if (!arena.towers.isEmpty) {
      }
      if (!onMenu && buyT) { 
        store.buyTower(mSqX,mSqY)
        
      } else if (mSqX == 21 && mSqY == 3) {
        if (buyT) {
          buyT = false 
          menuChoose = 0
        } else {
          buyT = true
          menuChoose = 100
        }
      }
    } else if (arena.squares(mSqX)(mSqY).isInstanceOf[Tower]) {
      /////TÄSSÄ/////
      store.buyDef(arena.towers(mSqX)(mSqY),store.iceDef)
      ///////////////
    }
  }
  
  
}


class Store(m: Menu) {
  val p = m.player //player
  val g = m.g      //game
  def a = g.arena  //current arena
  
  def iceDef  = new IceDefence(m.arena.towers(m.mSqX)(m.mSqY),80,0.7,1,5,m.g)
  def fireDef = new FireDefence(m.arena.towers(m.mSqX)(m.mSqY),100,0.7,1,5,m.g)
  
  def buyDef(t: Tower, d: Defence) = {
    if (p.money >= d.cost)
      if (t.addDefence(d))
        p.money -= d.cost
  }

  def buyTower(x: Int, y: Int) = {
    if (p.money > 4)
      if (a.setTower(x,y)) 
        p.money -= 5
  }
  
}

