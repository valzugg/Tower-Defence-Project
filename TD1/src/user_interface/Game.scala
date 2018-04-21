/**@author Valtteri Kortteisto */
package user_interface

import processing._
import processing.core._
import scala.util.Random
import files._
import map._
import objects._
import general._

// references:
// https://processing.org/reference/
// https://www.youtube.com/thecodingtrain
// http://kenney.nl/assets/tower-defense-top-down

/**makes the program runnable*/
object Game extends App {
  PApplet.main("user_interface.Game")
}


class Game extends PApplet {
  var fr = 0 // the current frame of the animation
  
  val progress = new Progress
  val introMenu = new IntroMenu(this,progress)
  
  // LEVELS /////////////////////////////////////////
  val easyLvls   = Vector(new Level("lvls/1.lvl", this),
                          new Level("lvls/2.lvl", this),
                          new Level("lvls/3.lvl", this))
  val normalLvls = Vector(new Level("lvls/1.lvl", this),
                          new Level("lvls/2.lvl", this),
                          new Level("lvls/3.lvl", this))
  val hardLvls   = Vector(new Level("lvls/1.lvl", this),
                          new Level("lvls/2.lvl", this),
                          new Level("lvls/3.lvl", this))
  val insaneLvls = Vector(new Level("lvls/1.lvl", this),
                          new Level("lvls/2.lvl", this),
                          new Level("lvls/3.lvl", this))
  private var currentLvl = easyLvls(0)
  def level = currentLvl
  def startLevel(l: Level) = {
    introMenu.isOn = false
    currentLvl = l
  }
  //////////////////////////////////////////////////// 
  
  def player = currentLvl.player
  val menu   = new Menu(this)
  
  // ARENA AND SIZE /////////////////////////////////
  def arena = currentLvl.arena
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
  var waveIndex = -1 // starts with the empty Wave
  def emptyWave = new Wave(1,0,0,0,0,0,currentLvl,this)
  def currentWave = {
    if (waveIndex >= 0) 
      currentLvl.waves(waveIndex)
    else 
      emptyWave
  }
  def nextWave()= {
    if (currentWave.isComplete)
      waveIndex += 1
  }
  ////////////////////////////////////////////////////
  
  // IMAGES //////////////////////////////////////////
  val introImg   = Array.ofDim[PImage](1)
  val highlight  = Array.ofDim[PImage](1)
  val muteButton = Array.ofDim[PImage](2)
  val obstacles = Array.ofDim[PImage](8)
  val defences  = Array.ofDim[PImage](4)
  val squares   = Array.ofDim[PImage](6)
  val menuS     = Array.ofDim[PImage](2)
  val mobSprites = Array.ofDim[PImage](3,4)
  val antSprites = Array.ofDim[PImage](4)
  val beetleSprites = Array.ofDim[PImage](4)
  val spiderSprites = Array.ofDim[PImage](4)
  mobSprites(0) = antSprites
  mobSprites(1) = beetleSprites
  mobSprites(2) = spiderSprites
  ////////////////////////////////////////////////////
  
  
  // relative speed of the program, 1 is normal rate
  private var rate = 1
  def isFast = rate == fast
  def runSpeed = rate // controls the rate of all the things that have speed in the game
  // when sped up, the speed is 4 times faster
  private val fast = 4
  
  def toggleRunSpeed() = {
    if (runSpeed == fast)
      rate = 1
    else
      rate = fast
  }
  
  var sounds: Sounds = null
  var font: PFont = null
  
  override def setup() {
    
    frameRate(60)
    
    introImg(0)  = loadImage("imgs/intro.png")
    highlight(0) = loadImage("imgs/highlight.png")
    muteButton(0) = loadImage("imgs/unmuted.png")
    muteButton(1) = loadImage("imgs/muted.png")
    squares(0) = loadImage("imgs/arena/arena0.png")
    squares(1) = loadImage("imgs/arena/arena1.png")
    squares(2) = loadImage("imgs/arena/arena2.png")
    squares(3) = loadImage("imgs/arena/arena3.png")
    squares(4) = loadImage("imgs/tower.png")
    squares(5) = loadImage("imgs/towerNo.png") 
    
    (0 to 3).foreach(s => antSprites(s)    = loadImage("imgs/ant/" + s + ".png"))
    (0 to 3).foreach(s => beetleSprites(s) = loadImage("imgs/beetle/" + s + ".png"))
    (0 to 3).foreach(s => spiderSprites(s) = loadImage("imgs/spider/" + s + ".png"))
    
    (0 to 4).foreach(o => obstacles(o) = loadImage("imgs/arena/obs" + o + ".png"))
    (0 until defences.length).foreach(d => defences(d) = loadImage("imgs/def/" + d + ".png"))
    menuS(0)  = loadImage("imgs/menu.png")
    
    font = createFont("Arial Bold",16,true)
    
    sounds = new Sounds(this)
    sounds.loop(sounds.bg)
  }
  
  //sets the size of the window
  override def settings() = {
    size(aWidth * sqSize + sqSize * mWidth, aHeight * sqSize)
  }
  
  def gameOver = player.hp <= 0
  
  
  override def draw() = {
    
    if (introMenu.isOn) {
      introMenu.doStuff()
      
    } else if (!gameOver) {
      // draws the tiles of the arena
      arena.drawArena()
    
      // handles the defences
      arena.towers.flatten.foreach(t => if (t != null) t.doStuff())
      
      // handles the mobs
      currentWave.doStuff()  
      
      // handles the HUD
      menu.doStuff()
      
      fr += 1
    } else {
      fill(0)
      textFont(font,44)
      text("GAME OVER", aWidth*sqSize/2, aHeight*sqSize/2)
    }
     // TODO
    if (currentLvl.isComplete) {
      fill(0)
      textFont(font,44)
      text("Level Complete", aWidth*sqSize/2, aHeight*sqSize/2)
    }
    
    if (this.exitCalled()) {
      sounds.stop()
    }
    
  }
  
  
  
  
  override def mousePressed() {
    if (introMenu.isOn) {
        introMenu.isOn = false
      if (mouseButton == menu.leftMouse)
        startLevel(easyLvls(0))
      else
        startLevel(easyLvls(2))
    } else {
      menu.clickingStuff()
    }
    
    if (currentLvl.isComplete) {
      introMenu.isOn = true
    }
  }
  
  
  override def keyPressed() {
    //toggleRunSpeed()
  }
  
  
}
