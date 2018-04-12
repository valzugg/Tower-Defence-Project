package user_interface

import processing._
import processing.core._
import scala.util.Random
import file_parser._
import arena._
import objects._
import general._
import ddf.minim._

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
  
  // LEVELS /////////////////////////////////////////
  var lvlN = 0 //index of the level vector
  val lvls = Vector(new Level("lvls/1.lvl", this),
                    new Level("lvls/2.lvl", this))
  def currentLvl = lvls(lvlN)
  //////////////////////////////////////////////////// 
                    
  // ARENA AND SIZE /////////////////////////////////
  def arena = lvls(lvlN).arena
  // the starting line number for the mobs         
  val sqSize  = Square.size //square's size in pixels
  val aWidth  = 20 //arena's width
  val aHeight = 15 //arena's height
  val mWidth  = 4  //menu's width
  //mouse's location in the grid's coodinates
  def mSqX = mouseX.toInt/sqSize
  def mSqY = mouseY.toInt/sqSize
  ////////////////////////////////////////////////////
    
  // MOB WAVES ///////////////////////////////////////
  var waveIndex = 0 // starts with the first wave
  def currentWave = currentLvl.waves(waveIndex)
  ////////////////////////////////////////////////////
  
  // IMAGE ARRAYS ////////////////////////////////////
  val highlight = Array.ofDim[PImage](1)
  val obstacles = Array.ofDim[PImage](8)
  val defences  = Array.ofDim[PImage](3)
  val squares   = Array.ofDim[PImage](6)
  val menuS     = Array.ofDim[PImage](2)
  val mobSprites = Array.ofDim[PImage](1,4)
  val antSprites = Array.ofDim[PImage](4)
  mobSprites(0) = antSprites
  ////////////////////////////////////////////////////
  
  // SOUND ///////////////////////////////////////////
  var minim: Minim = null
  var arrowSound: AudioPlayer = null
  var antDeadSound: AudioPlayer = null
  var bgSound: AudioPlayer = null
  ////////////////////////////////////////////////////
  
  var font: PFont = null
  
  override def setup() {
    
    minim = new Minim(this)
    
    arrowSound = minim.loadFile("sound/arrow.mp3")
    antDeadSound = minim.loadFile("sound/antDead.mp3")
    
    frameRate(60)
    highlight(0) = loadImage("imgs/highlight.png")
    
    squares(0) = loadImage("imgs/arena/arena0.png")
    squares(1) = loadImage("imgs/arena/arena1.png")
    squares(2) = loadImage("imgs/arena/arena2.png")
    squares(3) = loadImage("imgs/arena/arena3.png")
    squares(4) = loadImage("imgs/tower.png")
    squares(5) = loadImage("imgs/towerNo.png")
    
    antSprites(0) = loadImage("imgs/ant/0.png")
    antSprites(1) = loadImage("imgs/ant/1.png")
    antSprites(2) = loadImage("imgs/ant/2.png")
    antSprites(3) = loadImage("imgs/ant/3.png")
    
    (0 to 4).foreach(o => obstacles(o) = loadImage("imgs/arena/obs" + o + ".png"))
    
    (0 until defences.length).foreach(d => defences(d) = loadImage("imgs/def" + d + ".png"))
    
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

    arena.drawArena()
    
    arena.towers.flatten.foreach(t => if (t != null) t.doStuff())
    
    currentWave.doStuff()
    
    menu.doStuff()
    
    //if (fr%60 == 0) println(currentWave.mobs(0).moneyValue)
    
    fr += 1
    
  }
  
  
  //korjattavaa
  override def mousePressed() {
    menu.clickingStuff()
  }
  
  
  //lol
  override def keyPressed() {
    //changeFPS
    if (currentWave.isComplete && waveIndex != currentLvl.waves.size - 1)
      waveIndex += 1
  }
  
  
}
