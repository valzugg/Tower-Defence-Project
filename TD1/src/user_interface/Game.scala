package user_interface

import processing._
import processing.core._
import scala.util.Random
import file_parser._
import arena._
import objects._

// https://processing.org/reference/
// https://www.youtube.com/thecodingtrain
// http://kenney.nl/assets/tower-defense-top-down

/**makes the program runnable*/
object Game extends App {
  PApplet.main("user_interface.Game")
}


class Game extends PApplet {
  val player = new Player(this)
  val menu   = new Menu(this)
  
  var fr = 0 // the current frame of the animation
  
  var lvlN = 1 //index of the level vector
  val lvls = Vector(new Level("lvls/1.lvl", this),
                    new Level("lvls/2.lvl", this))
                    
  def arena = lvls(lvlN).arena
  
  // the starting line number for the mobs
  def startPath = arena.start                
  
  val sqSize  = Square.size //square's size in pixels
  
  val aWidth  = 20 //arena's width
  val aHeight = 15 //arena's height
  
  val mWidth  = 4  //menu's width
  
  //mouse's location in the grid's coodinates
  def mSqX = mouseX.toInt/sqSize
  def mSqY = mouseY.toInt/sqSize
  
  
  val obstacles = Array.ofDim[PImage](8)
  val defences  = Array.ofDim[PImage](2)
  val squares   = Array.ofDim[PImage](4)
  val menuS     = Array.ofDim[PImage](2)
  
  
  def centerOfSquare(x: Int, y: Int) = {
    (sqSize*x + sqSize/2.toFloat,sqSize*y + sqSize/2.toFloat)
  }
  
  
  val wave = new Wave(10,2,2.4, 200, "imgs/ant.png", this)
  def currentWave = wave
  
  var font: PFont  = null
  
  val sounds = Array.ofDim[PImage](2)
  
  override def setup() {
    
    frameRate(60)
    squares(0) = loadImage("imgs/arena0.png")
    squares(1) = loadImage("imgs/arena1.png")
    squares(2) = loadImage("imgs/tower.png")
    squares(3) = loadImage("imgs/towerNo.png")
    
    (0 to 7).foreach(o => obstacles(o) = loadImage("imgs/obs" + o + ".png"))
    
    (0 until defences.length).foreach(d => defences(d) = loadImage("imgs/def" + d + ".png"))
    
    wave.sprite = loadImage(wave.img)
    
    menuS(0)  = loadImage("imgs/menu.jpg")
    menuS(1)  = loadImage("imgs/menutop.jpg")
    font = createFont("Arial Bold",16,true)
  }
  
  //sets the size of the window
  override def settings() = {
    size(aWidth * sqSize + sqSize * mWidth, aHeight * sqSize)
  }
  
  
  ////pretty risky business////////////////////////////////
  var fps = 30
  
  def changeFPS = {
    //if (fps == 30) fps = 120 else fps = 30
  }
  
  //TODO: fps change
  //////////////////////////////////////////////////////////
  //smarter way to do - make a method here 
  //somewhere that changes the speed of the mobs,
  //the damage made by defences, and other stuff accordingly
  //////////////////////////////////////////////////////////
  
  override def draw() = {
    
    //frameRate(fps)
    
    //should draw the menu background, the tiles, and the text on the top
    menu.doStuff()

    arena.drawArena()
    
    menu.buyingShit()
    
    arena.towers.flatten.foreach(t => if (t != null) t.doStuff())
    
    wave.doStuff()
    
    fr += 1
    
  }
  
  
  //korjattavaa
  override def mousePressed() {
    menu.clickingStuff()
  }
  
  
  //lol
  override def keyPressed() {
    changeFPS
  }
  
  
}
