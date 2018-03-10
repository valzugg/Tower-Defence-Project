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
  
  var lvlN = 1 //index of the level vector
  val lvls = Vector(new Level("lvls/1.lvl", this),
                    new Level("lvls/2.lvl", this))
                    
  def arena = lvls(lvlN).arena
          
  val player = new Player(this)
  
  // the starting line number for the mobs
  def startPath = arena.start                
  
  val sqSize  = Square.size //square's size in pixels
  
  val aWidth  = 20 //arena's width
  val aHeight = 15 //arena's height
  
  val mWidth  = 4  //menu's width
  
  //mouse's location in the grid's coodinates
  def mSqX = mouseX.toInt/sqSize
  def mSqY = mouseY.toInt/sqSize
  
  //determines whether the mouse is on the menu
  def onMenu = mSqX > aWidth-1
  
  val obstacles = Array.ofDim[PImage](8)
  val defences = Array.ofDim[PImage](1)
  
  def centerOfSquare(x: Int, y: Int) = {
    (sqSize*x + sqSize/2.toFloat,sqSize*y + sqSize/2.toFloat)
  }
  
  val wave = new Wave(10,1,1.0, 100, "imgs/ant.png", this)
//  val testDef1 = new Defence(centerOfSquare(8,9),100,0.7,this)
//  val testDef2 = new Defence(centerOfSquare(8,10),100,0.7,this)
  def currentWave = wave
  
  
  val squares = Array.ofDim[PImage](4)
  val menu = Array.ofDim[PImage](2)
  var font: PFont  = null
  
  val sounds = Array.ofDim[PImage](2)
  
  var buyT = false      //keeps track of if the player is buying towers currently
  var menuCol = (0,255) //changes the menu buttons from green to red and back
  
  override def setup() {
    frameRate(30)
    squares(0) = loadImage("imgs/grass.png")
    squares(1) = loadImage("imgs/path.png")
    squares(2) = loadImage("imgs/tower.png")
    squares(3) = loadImage("imgs/towerNo.png")
    
    (0 to 7).foreach(o => obstacles(o) = loadImage("imgs/obs" + o + ".png"))
    
    (0 until defences.length).foreach(d => defences(d) = loadImage("imgs/def" + d + ".png"))
    
    wave.sprite = loadImage(wave.img)
    
    menu(0)  = loadImage("imgs/menu.jpg")
    menu(1)  = loadImage("imgs/menutop.jpg")
    font = createFont("Arial Bold",16,true)
  }
  
  //sets the size of the window
  override def settings() = {
    size(aWidth * sqSize + sqSize * mWidth, aHeight * sqSize)
  }
  
  
  ////pretty risky business////////////////////////////////
  var fps = 30
  
  def changeFPS = {
    if (fps == 30) fps = 120 else fps = 30
  }
  //////////////////////////////////////////////////////////
  //smarter way to do - make a method here 
  //somewhere that changes the speed of the mobs,
  //the damage made by defences, and other stuff accordingly
  //////////////////////////////////////////////////////////
  
  override def draw() = {
    
    frameRate(fps)
    
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
    text("Money:   " + player.money,sqSize*aWidth+ 9,61)
    fill(255, 255, 0)
    text("Money:   " + player.money,sqSize*aWidth+ 8,60)
    
    fill(0) 
    textFont(font,22)
    text("HP:       " + player.hp,sqSize*aWidth+ 9,91)
    fill(255, 0, 0)
    text("HP:       " + player.hp,sqSize*aWidth+ 8,90)
    
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
      
    //TODO: Korjaa alempi koodi fiksuksi  
    
    //println(arena)
    
    //draws the grid of the arena
    for (col <- 0 until aWidth) {
      for (row <- 0 until aHeight) {
        if (!arena.squares(col)(row).isInstanceOf[Tower]) {
          image(squares(arena.squares(col)(row).i), 
                col * sqSize, row * sqSize, sqSize, sqSize)
        } else {
          image(squares(0), col * sqSize, row * sqSize, sqSize, sqSize)
          image(squares(2), col * sqSize, row * sqSize, sqSize, sqSize)
        }
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
    
    
    //TODO: Korjaa alempi koodi fiksuksi
    
    //sets the sprites of towers
    if (player.money > 4) { //menu color turns off when not enough money
      menuCol = (0, 255) //when the player has enough money, the menu turns green
      if (!onMenu && buyT) {
        if (arena.squares(mSqX)(mSqY).isInstanceOf[Empty]) {
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
    
    
    
    /////////////////////#doStuff()//////////////////////////
    arena.towers.flatten.foreach(t => if (t != null) t.doStuff())
    wave.doStuff()
    /////////////////////////////////////////////////////////
    
    
    
    player.getMoney()
    
    fr += 1
    if (fr % 200 == 0)
      player.money += 1
    
  }
  
  
  
  
  val leftMouse = 37
  var menuChoose = 0 //transparency of the menu button
  
  //korjattavaa
  override def mousePressed() {
    if (mouseButton == leftMouse) {
      if (!arena.towers.isEmpty) {
//        println(arena.towers(0).pos)       //TESTING
//        println((mSqX*sqSize,mSqY*sqSize))
      }
      if (!onMenu && buyT) { 
        player.buyTower(mSqX,mSqY)
        
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
      
      ////TÄSSÄ/////
      arena.towers(mSqX)(mSqY).addDefence(new IceDefence(arena.towers(mSqX)(mSqY),100,0.7,1,1,0,this))
      //////////////
    }
  }
  
  
  //lol
  override def keyPressed() {
    changeFPS
  }
  
  
}
