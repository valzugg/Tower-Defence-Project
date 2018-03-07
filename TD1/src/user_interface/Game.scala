package user_interface

import processing._
import processing.core._
import processing.core.PConstants
import scala.util.Random
import file_parser._
import arena._
import objects._

// https://processing.org/reference/
// https://www.youtube.com/thecodingtrain
// http://kenney.nl/assets/tower-defense-top-down

//makes the program runnable
object Game extends App {
  PApplet.main("user_interface.Game")
}

//processing -> purkkaratkaisut
class Game extends PApplet {
  def r = Random.nextInt(3)
  
  var fr = 0 // the current frame of the animation
  
  var lvlN = 0 //index of the level vector
  val lvls = Vector(new Level("lvls/1.lvl", this),
                    new Level("lvls/2.lvl", this))
                    
  def arena = lvls(lvlN).arena
                    
  // the starting line number for the mobs
  def startPath = arena.start                
  
  val sqSize  = 40 //square's size in pixels
  
  val aWidth  = 20 //arena's width
  val aHeight = 15 //arena's height
  
  val mWidth  = 4  //menu's width
  
  //mouse's location in the grid's coodinates
  def mSqX = mouseX.toInt/sqSize
  def mSqY = mouseY.toInt/sqSize
  
  //determines whether the mouse is on the menu
  def onMenu = mSqX > aWidth-1
  
  val obstacles = Array.ofDim[PImage](8)
  
  val wave = new Wave(10,1,1.0, 100, "imgs/ant.png", this)
  val testDef1 = new Defence(sqSize*7 + sqSize/2.toFloat,sqSize*4 + sqSize/2.toFloat,100,0.5,this)
  val testDef2 = new Defence(sqSize*7 + sqSize/2.toFloat,sqSize*5 + sqSize/2.toFloat,100,0.5,this)
  
  val squares = Array.ofDim[PImage](4)
  val menu = Array.ofDim[PImage](2)
  var font: PFont  = null
  
  val sounds = Array.ofDim[PImage](2)
  
  var buyT = false      //keeps track of if the player is buying towers currently
  var menuCol = (0,255) //changes the menu buttons from green to red and back
  
  override def setup() {
    frameRate(60)
    squares(0) = loadImage("imgs/grass.png")
    squares(1) = loadImage("imgs/path.png")
    squares(2) = loadImage("imgs/tower.png")
    squares(3) = loadImage("imgs/towerNo.png")
    
    (0 to 7).foreach(o => obstacles(o) = loadImage("imgs/obs" + o + ".png"))
    
    wave.sprite = loadImage(wave.img)
    
    menu(0)  = loadImage("imgs/menu.jpg")
    menu(1)  = loadImage("imgs/menutop.jpg")
    font = createFont("Arial Bold",16,true)
  }
  
  //sets the size of the window
  override def settings() = {
    size(aWidth * sqSize + sqSize * mWidth, aHeight * sqSize)
  }
  
  
  
  override def draw() = {
    
    ////////////////////MENU////////////////////////////
    
    if (fr < 1) { // joooh, purkkaratkaisu
      image(menu(0),sqSize*20,sqSize*3,sqSize*4,sqSize*12)
    }
    
    image(menu(1),sqSize*20,0,sqSize*4,sqSize*3)
    
    textFont(font,16)
    fill(0)  
    text("Val's Tower Defence",sqSize*aWidth+ 5,21)
    fill(46, 204, 113)
    text("Val's Tower Defence",sqSize*aWidth+ 4,20)
    fill(0) 
    textFont(font,20)
    text("Money:   " + Player.money,sqSize*aWidth+ 9,61)
    fill(255, 255, 0)
    text("Money:   " + Player.money,sqSize*aWidth+ 8,60)
    
    fill(0) 
    textFont(font,22)
    text("HP:       " + Player.hp,sqSize*aWidth+ 9,91)
    fill(255, 0, 0)
    text("HP:       " + Player.hp,sqSize*aWidth+ 8,90)
    
    stroke(1)
    
    for (row <- 0 until 4) {
      for (col <- 0 until 2) {
        if (mouseX > sqSize*21 + col*sqSize && mouseX < sqSize*22 + col*sqSize &&
          mouseY > sqSize*3 + row*sqSize && mouseY < sqSize*4 + row*sqSize) {
          fill(menuCol._1,menuCol._2, 0,menuChoose + 80)
          rect((mSqX)*sqSize,(mSqY)*sqSize,sqSize,sqSize)
        }
      }
    }
      
      
    fill(180,180,180,100-menuChoose)
    for (row <- 0 until 4) {
      for (col <- 0 until 2) {
        rect(sqSize * 21 + col * sqSize, 
             sqSize * 3 + row * sqSize, sqSize, sqSize)
      }
    }
    
    image(squares(2),sqSize*21,sqSize*3)
    
    //////////////////////////////////////////////////////
      
      
    //draws the grid of the arena
    for (row <- 0 until aHeight) {
      for (col <- 0 until aWidth) {
        //if (lvls(lvlN).arena.squares(row)(col).i == 0 || lvls(lvlN).arena.squares(row)(col).i == 1 || lvls(lvlN).arena.squares(row)(col).i == 3)
          image(squares(lvls(lvlN).arena.squares(row)(col).i), 
          col * sqSize, row * sqSize, sqSize, sqSize)
        //else 
        //  image(arena(0), col * sqSize, row * sqSize, sqSize, sqSize)  
      }
    }
    
    
    
    //draws obstacles
//    for (row <- 0 until aHeight) {
//      for (col <- 0 until aWidth) {
//        if (lvls(lvlN).arena.squares(row)(col).i == 2) {
//          image(obstacles(0), 
//          col * sqSize, row * sqSize, sqSize, sqSize)
//        }
//      }
//    }
    
    //sets the sprites of towers
    if (Player.money > 4) { //menu color turns off when not enough money
      menuCol = (0, 255) //when the player has enough money, the menu turns green
      if (!onMenu && buyT) {
        if (lvls(lvlN).arena.squares(mSqY)(mSqX).isInstanceOf[Empty]) {
          image(squares(2),(mSqX)*sqSize, 
                         (mSqY)*sqSize, 
                          sqSize, sqSize)
        } else {
          image(squares(3),(mSqX)*sqSize, 
                         (mSqY)*sqSize, 
                          sqSize, sqSize)
        }
      } 
    } else {
      menuChoose = 0        //the color's opacity the the cursor isn't there goes to zero
      menuCol    = (255, 0) //when the player doesnt have enough money, the menu turns red
      buyT       = false
    }
    
    
    
    /////////////////////mob stuff//////////////////////////
      
    wave.doStuff()
    ////////////////////////////////////////////////////////
    
    testDef1.doStuff()
    testDef2.doStuff()
    
    fr += 1
    if (fr % 200 == 0)
      Player.money += 1
    
  }
  
  
  
  
  val leftMouse = 37
  var menuChoose = 0 //transparency of the menu button
  
  //korjattavaa
  override def mousePressed() {
    if (mouseButton == leftMouse)
      if (!onMenu && buyT) { 
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
  
  
  //lol
  override def keyPressed() {
    wave.mobs(Random.nextInt(wave.size - 1)).damage(10)
  }
  
  
}
