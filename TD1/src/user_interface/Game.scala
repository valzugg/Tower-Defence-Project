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
  progress.load(1) // loads the first slot by default
  val introMenu = new IntroMenu(this,progress)
  
  // LEVELS /////////////////////////////////////////
  val menuLvl = new Level("lvls/menu.lvl", this)
  val levels  = (0 to progress.lastLevelIndex) // creates all the 12 levels
                .map(i => new Level("lvls/" + (i + 1) + ".lvl", this)).toVector
  private var currentLvl = levels(0)
  def level = currentLvl
  def levelIndex = level.fileIndex - 1
  def startLevel(l: Level) = {
    if (l.isComplete) l.reset()
    currentLvl = l
    introMenu.toggle()
  }
  //////////////////////////////////////////////////// 
  
  
  /** Calculates the score for the current level from the total distance all the mobs moved,
   *  and the the health the player has at the end. 
   *  The maximum score is 1000. (though impossible)*/
  def score = {
    val pathLen = arena.pathLength
    val mobAmount = currentLvl.waves.flatMap(_.mobs).size
    val bestPossibleScore = mobAmount * pathLen * 100
    val currentScore = currentLvl.waves.map(_.mobs.map(pathLen - _.distance)
                                       .reduceLeft(_+_)).reduceLeft(_+_).toInt
    (((currentScore.toDouble*player.hp)/bestPossibleScore)*1000).toInt
  }
  
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
    
  
  def currentWave = level.currentWave
  
  // IMAGES //////////////////////////////////////////
  val highlig    = Array.ofDim[PImage](1)
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
  
  def isPaused = runSpeed == 0
  def togglePause() = {
    introMenu.togglePause()
    if (isPaused)
      rate = 1
    else
      rate = 0
  }
  
  var sounds: Sounds = null
  var font: PFont = null
  
  override def setup() {
    
    frameRate(60)
    
    highlig(0)   = loadImage("imgs/highlight.png")
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
  
  def highlight() = {
    if (!menu.onMenu) {
      image(highlig(0),(mSqX)*sqSize, (mSqY)*sqSize, sqSize, sqSize)
    }
  }
  
  override def draw() = {
    
      // Main menu loop
    if (introMenu.isOn) {
      menuLvl.arena.drawArena(menuLvl.width,menuLvl.height)
      introMenu.drawStuff()
      
      // Main game loop
    } else if (!gameOver && !currentLvl.isComplete) {
      
      // draws the tiles of the arena
      arena.drawArena(aWidth,aHeight)
    
      highlight()
      
      // handles the defences
      arena.towers.flatten.foreach(t => if (t != null) t.doStuff())
      
      // handles the mobs
      level.currentWave.doStuff()  
      
      // handles the HUD
      menu.doStuff()
      
      if (isPaused) {
        introMenu.drawPauseMenu()
      } else {
        fr += 1
      }
      
      // Game over
    } else if (gameOver && !introMenu.isOn) {
      arena.drawArena(aWidth,aHeight)
      level.currentWave.doStuff()  
      menu.doStuff()
      if (isFast) toggleRunSpeed()
      bigText("   Game Over")
    } else if (currentLvl.isComplete && !introMenu.isOn) {
      arena.drawArena(aWidth,aHeight)
      arena.towers.flatten.foreach(t => if (t != null) t.doStuff())
      menu.doStuff()
      if (isFast) toggleRunSpeed()
      bigText("Level Complete" + "\n    Score: " + score)
    }
    
    // stops sounds so that minim doesnt give error
    if (this.exitCalled()) {
      sounds.stop()
    }
    
  }
  
  def bigText(t: String) = {
    noStroke()
    fill(100,100,150,100)
    rect(6*sqSize,5*sqSize,12*sqSize,8*sqSize)
    
    fill(0,0,0,100)
    textFont(font,34)
    text(t, 9*sqSize-2, 8*sqSize-2)
    textFont(font,18)
    text("\n\n\n\n(Click anywhere to continue)", 9*sqSize-2, 8*sqSize-2)
    fill(0)
    textFont(font,34)
    text(t, 9*sqSize, 8*sqSize)
    textFont(font,18)
    text("\n\n\n\n(Click anywhere to continue)", 9*sqSize, 8*sqSize)
  }
  
  
  
  override def mousePressed() {
    if (introMenu.isOn) {
      introMenu.clickingStuff()
    } else if (isPaused) {
      introMenu.clickingPauseMenu()
    } else if (!currentLvl.isComplete && !gameOver) {
      menu.clickingStuff()
    } else if (currentLvl.isComplete) {
      if (levelIndex == progress.available && !progress.hasFinished) 
        progress.unlock()
      progress.setHighscore(levelIndex,score)
      progress.save(introMenu.currentSave)
      introMenu.toggle()
      introMenu.changeState(introMenu.Progress)
    } else if (gameOver) {
      level.reset()
      introMenu.toggle()
      introMenu.changeState(introMenu.Progress)
    }
    
  }
  
  
  override def keyPressed() {
    if (key == menu.enter && !introMenu.isOn) 
      toggleRunSpeed()
    if (key == menu.tab && !introMenu.isOn) 
      togglePause()
  }
  
  
}
