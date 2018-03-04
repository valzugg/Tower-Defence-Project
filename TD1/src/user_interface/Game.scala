package user_interface

import processing._
import processing.core._
import scala.util.Random
import file_parser._
import arena._

// https://processing.org/reference/
// https://www.youtube.com/thecodingtrain
// http://kenney.nl/assets/tower-defense-top-down

//makes the program runnable
object Game extends App {
  PApplet.main("user_interface.Game")
}

class Game extends PApplet {
  def r = Random.nextInt(2)
  
  var fr = 0
  
  var lvlN = 0
  val lvls = Vector(new Level("lvls/1.lvl", this),
                    new Level("lvls/2.lvl", this))
  
  val sqSize  = 10 //square's size in pixels
  
  val aWidth  = 20 //arena's width
  val aHeight = 15 //arena's height
  
  val mWidth  = 6  //menu's width
  val mHeight = 15 //menu's height
  
  //mouse's location in the grid's coodinates
  def mSqX = mouseX.toInt/sqSize
  def mSqY = mouseY.toInt/sqSize
  
  //determines whether the mouse is on the menu
  def onMenu = mSqX > aWidth-1
  
  val ant = new Mob("ant",-sqSize,2*sqSize,1, "imgs/ant.png")
  
  val arena = Array.ofDim[PImage](4)
  var mob: PImage  = null
  val menu = Array.ofDim[PImage](2)
  var font: PFont  = null
  
  val sounds = Array.ofDim[PImage](2)
  
  var buyT = false
  
  override def setup() {
    frameRate(30)
    arena(0) = loadImage("imgs/grass.png")
    arena(1) = loadImage("imgs/path.png")
    arena(2) = loadImage("imgs/tower.png")
    arena(3) = loadImage("imgs/towerNo.png")
    mob      = loadImage("imgs/ant.png")
    menu(0)  = loadImage("imgs/menu.jpg")
    menu(1)  = loadImage("imgs/menutop.jpg")
    font = createFont("Arial",16,true)
  }
  
  //sets the size of the window
  override def settings() = {
    size(aWidth * sqSize + sqSize * 4, aHeight * sqSize)
  }
  
  
  
  override def draw() = {
    
    ////////////////////MENU////////////////////////////
    
    if (fr < 1) { //magic
      image(menu(0),sqSize*20,sqSize*3,sqSize*4,sqSize*12)
    }
    
    image(menu(1),sqSize*20,0,sqSize*4,sqSize*3)
    
    textFont(font,16)
    fill(0)  
    text("Val's Tower Defence",sqSize*aWidth+ 9,21)
    fill(255,0,0)
    text("Val's Tower Defence",sqSize*aWidth+ 8,20)
    fill(0) 
    textFont(font,20)
    text("Money: " + Player.money,sqSize*aWidth+ 9,61)
    fill(255, 255, 0)
    text("Money: " + Player.money,sqSize*aWidth+ 8,60)
    
    if (mouseX > sqSize*21 && mouseX < sqSize*22 &&
        mouseY > sqSize*3  && mouseY < sqSize*4) {
      fill(0, 255, 0,menuChoose + 80)
      rect((mSqX)*sqSize,(mSqY)*sqSize,sqSize,sqSize)
    }
      
    fill(180,180,180,100-menuChoose)
    for (row <- 0 until 4) {
      for (col <- 0 until 2) {
        rect(sqSize * 21 + col * sqSize, 
             sqSize * 3 + row * sqSize, sqSize, sqSize)
      }
    }
    
    //////////////////////////////////////////////////////
      
      
    //draws the grid of the arena
    for (row <- 0 until aHeight) {
      for (col <- 0 until aWidth) {
        image(arena(lvls(lvlN).arena.squares(row)(col).i), 
        col * sqSize, row * sqSize, sqSize, sqSize)
      }
    }
    
    if (!onMenu && buyT) {
      if (lvls(lvlN).arena.squares(mSqY)(mSqX).isInstanceOf[Empty] &&
          Player.money > 4) {
        image(arena(2),(mSqX)*sqSize, 
                       (mSqY)*sqSize, 
                        sqSize, sqSize)
      } else {
        image(arena(3),(mSqX)*sqSize, 
                       (mSqY)*sqSize, 
                        sqSize, sqSize)
      }
    }
    
    fr += 1
    if (fr % 90 == 0)
      Player.money += 1
    
  }
  
  
  
  
  val leftMouse = 37
  var menuChoose = 0 //transparency of the menu button
  
  override def mousePressed() {
    if (mouseButton == leftMouse)
      if (!onMenu && buyT) { //tornin asettaminen tapahtuu tässä
        Player.buyTower(mSqY,mSqX,lvls(lvlN).arena)
      } else if (mouseX > sqSize*21 && mouseX < sqSize*22 &&
                 mouseY > sqSize*3  && mouseY < sqSize*4) {
        if (buyT) {
          buyT = false 
          menuChoose = 0
        } else {
          buyT = true
          menuChoose = 100
        }
      }
      
  }
  
}


object Player {
    var money = 10
    var hp    = 100
    
    def buyTower(x: Int, y: Int, a: Arena) = {
      if (money > 4)
        if (a.setTower(x,y)) money -= 5
    }
    
}

class Mob(val name: String, sx: Int ,sy: Int ,val speed: Int, img: String) {
  val sqSize  = 40
  
  var x = sx
  var y = sy
  def moveL() = {
    x += speed
  }
  def moveR() = {
    x -= speed
  }
  def moveU() = {
    y -= speed
  }
  def moveD() = {
    y += speed
  }
  
}

