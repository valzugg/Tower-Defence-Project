package user_interface

import processing._
import processing.core._
import scala.util.Random
import files._
import map._
import objects._
import general._

// https://processing.org/reference/
// https://www.youtube.com/thecodingtrain
// http://kenney.nl/assets/tower-defense-top-down

/**makes the program runnable*/
object Game extends App {
  PApplet.main("user_interface.Game")
}


class Game extends PApplet {
  var fr = 0 // the current frame of the animation
  
  def nextLevel() = {
    ???
  }
  
  def nextWave()= {
    if (currentWave.isComplete)
      waveIndex += 1
  }
  
  // LEVELS /////////////////////////////////////////
  private var lvlN = 1 //index of the level vector
  val lvls = Vector(new Level("lvls/1.lvl", this),
                    new Level("lvls/2.lvl", this))
  def currentLvl = lvls(lvlN)
  //////////////////////////////////////////////////// 
  
  def player = currentLvl.player
  val menu   = new Menu(this)
  
  // ARENA AND SIZE /////////////////////////////////
  def arena = lvls(lvlN).arena
  // the starting line number for the mobs         
  val sqSize  = menu.sqSize  //square's size in pixels
  def aWidth  = menu.aWidth  //arena's width
  def aHeight = menu.aHeight //arena's height
  val mWidth  = menu.mWidth  //menu's width
  //mouse's location in the grid's coodinates
  def mSqX = menu.mSqX
  def mSqY = menu.mSqY
  ////////////////////////////////////////////////////
    
  // MOB WAVES ///////////////////////////////////////
  var waveIndex = -1 // starts with the first wave
  def emptyWave = new Wave(1,0,0,0,0,0,currentLvl,this)
  def currentWave = {
    if (waveIndex >= 0) 
      currentLvl.waves(waveIndex)
    else 
      emptyWave
  }
  ////////////////////////////////////////////////////
  
  // IMAGES //////////////////////////////////////////
  val highlight = Array.ofDim[PImage](1)
  val muteButton = Array.ofDim[PImage](2)
  val obstacles = Array.ofDim[PImage](8)
  val defences  = Array.ofDim[PImage](3)
  val squares   = Array.ofDim[PImage](6)
  val menuS     = Array.ofDim[PImage](2)
  val mobSprites = Array.ofDim[PImage](1,4)
  val antSprites = Array.ofDim[PImage](4)
  mobSprites(0) = antSprites
  ////////////////////////////////////////////////////
  
  var sounds: Sounds = null
  var font: PFont = null
  
  override def setup() {
    
    
    
    frameRate(60)
    
    highlight(0) = loadImage("imgs/highlight.png")
    muteButton(0) = loadImage("imgs/unmuted.png")
    muteButton(1) = loadImage("imgs/muted.png")
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
    
    sounds = new Sounds(this)
    sounds.loop(sounds.bg)
  }
  
  //sets the size of the window
  override def settings() = {
    size(aWidth * sqSize + sqSize * mWidth, aHeight * sqSize)
  }
  
  
  
  //TODO: fps change
  ////////////////////////////////////////////////////////////
  //smarter way to do - make a method here                  //
  //somewhere that changes the speed of the mobs,           //
  //the damage made by defences, and other stuff accordingly//
  ////////////////////////////////////////////////////////////
  
  override def draw() = {
    
//    if (fr%1000 == 999) {
//      if (lvlN < 1) lvlN += 1
//    }
    
    arena.drawArena()
    
    arena.towers.flatten.foreach(t => if (t != null) t.doStuff())
    
    currentWave.doStuff()
    
    menu.doStuff()
    
    fr += 1
    
    if (this.exitCalled()) {
      sounds.stop()
    }
    
  }
  
  
  //korjattavaa
  override def mousePressed() {
    menu.clickingStuff()
  }
  
  
  //lol
  override def keyPressed() {
    //changeFPS
//    if (currentWave.isComplete)
//      waveIndex += 1
  }
  
  
}
