/**@author Valtteri Kortteisto */
package objects

import map._
import user_interface._
import files.Level
import processing.core.PImage
import general.Helper
import scala.math.abs

/** Represents an enemy in a tower defence game.
 *  A mob is part of a Wave (w)
 *  Extends Helper to get some constants and generally useful methods.*/
class Mob(w: Wave ,var speed: Float, hitpoints: Int,  val moneyValue: Int, g: Game, 
          val i: Int, size: Float = 0.25.toFloat, lvl: Level) extends Helper(g) {
  val hp = new HealthBar(this,hitpoints)
  val originalSpeed = speed
  val hitboxSize = scala.math.max((size * 40).toInt,20) // the hitbox must be at the least 20x20 pixels
  
  private var dist = -sqSize.toFloat // keeps track of how far the mob is along the path
  def distance = abs(dist) // the distance the mob has moved (starts when the mob first comes into view)
  
  val r = scala.util.Random
  
  // keep track of the mob's location
  // the start x postion depends on how big the distance between the mobs is and
  // how far they are in the wave's queue
  // the start y postion is the level's start path spot
  // theres also a random element to both, so it looks less superficial
  var x = (-sqSize * (i + 1) * w.distance) - (r.nextFloat() * w.distance * 20) 
  var y = lvl.pathStart*sqSize.toFloat - sqSize/12 + r.nextInt(sqSize/6)
  
  /** The current position of the center of the mob. */
  def pos = (x + sqSize/2,y + sqSize/2)
  
  //keeps track of the mob's current direction
  private var dir   = (0,0)
  
  //returns the index of the mob in the wave
  override def toString() = i.toString
  
  /** Checks if mobs hitpoints are up. */
  def dead = hp.amount < 1
  
  // assists hp in knowing when to display itself
  var hasBeenDamaged = false
  
  /** Removes given amount of hitpoints.
   *  Also gives the player money if the mob dies. */
  def damage(by: Double) = {
    this.hp.damage(by)
    hasBeenDamaged = true
    if (dead) { 
      g.player.getPaid()      
      g.sounds.play(g.sounds.antDead)
    }
  }
  
  //the mob's current square
  def square = {
    g.arena(x.toInt/sqSize,y.toInt/sqSize)
  }
  
  /** Moves the mob in the given direction and sets 
   *  it's dir and dist variables correct.*/
  private def move(d: (Int, Int)) = {
    x += d._1*speed*g.runSpeed
    y += d._2*speed*g.runSpeed
    dir = (d._1,d._2)
    if (x > -sqSize) // updates the distance variable
      dist += abs(d._1*speed*g.runSpeed + d._2*speed*g.runSpeed)
    if (speed != originalSpeed) // returns the speed of the mob to normal after it is slowed
      speed = originalSpeed
  }
  
  
  private def moveToStart() = {
    this.x = -sqSize
    this.y = sqSize*lvl.pathStart
    // damage the player
    g.player.hp -= moneyValue * 5
  }
  
  // assist the mob in moving
  // uses the path that Arena class created to 
  // navigate via vector directions on an Array
  private def pathIndex = ((dist)/sqSize).toInt
  private def currentDir = lvl.path(pathIndex)
  
  /** The practical way the mob is moved.*/
  private def act() = {
    // when the mob has walked to the end of the path, it is teleported back to start
    if (x >= sqSize*(g.arena.aWidth)) moveToStart()
    if (x < sqSize*(g.arena.aWidth - 1)) {
      if (x < 0) { //when at start (left of the frame)
        move(right)
      } else {     //when in the main arena
        move(currentDir)
        rotate()
      }
    } else {
      move(dir) // in the case the mob finishes the path
      dist = -sqSize  // reset distance for new round
    }
  }
 
  /** Rotates the mob to the direction it is moving. 
   *  See act()*/
  private def rotate() = {
    dir match {
      case this.right => sx = sDist; sy = sDist
      case this.down  => g.rotate(halfPi); sy = -sDist; sx = sDist
      case this.left  => g.rotate(2*halfPi); sx = -sDist; sy = -sDist
      case _          => g.rotate(3*halfPi); sx = -sDist; sy = sDist
    }
  }
  
  // the shadow's distance from mob
  private val sDist = -sqSize/12
  // the shadow's offset
  private var sx = sDist
  private var sy = sDist
  
  /** The concrete stuff that is sent to be done at the Game class.
   *  @param img The sprite of the mob as a PImage already loaded.*/
  def doStuff(img: PImage) = {
    if (!dead) {
      g.pushMatrix()
      g.translate(sqSize/2,sqSize/2)
      g.translate(this.x,this.y) // the 'axis' of the mob is being moved
      this.act()
      g.tint(0,0,0,sIntensity) // draws the shadow
      g.image(img,-sqSize*size+sx,-sqSize*size+sy,sqSize*2*size,sqSize*2*size)
      g.noTint()
      g.image(img,-sqSize*size,-sqSize*size,sqSize*2*size,sqSize*2*size)
      g.popMatrix()
    }
  }

}


/** Represents the health bar of a mob.
 *  A mob's health bar follows it, going above it and showing in
 *  green and red how much health a mob has. 
 *  If health is up, mob dies. The health bar should only show up
 *  after the mob has been damaged.
 * 	@param m The mob to which the health bar belongs
 *  @param fullAmount the original amount of health the mob spawns with.*/
class HealthBar(val m: Mob, fullAmount: Double) {
  // keep track of the mob's coordinates
  def x = m.x + 2
  def y = m.y - 5
  
  // the size of the health bar
  private val xSize = 35
  private val ySize = 3
  
  var amount = fullAmount
  
  def damage(by: Double) = { amount -= by }
  
  /** Drawing of the health bar when called in Game's draw().
   *  Drawn only if mob is alive and has been damaged.*/
  def doStuff() = {
    if (!m.dead && m.hasBeenDamaged) {
      m.g.noStroke()
      m.g.fill(255,0,0)
      m.g.rect(x,y,xSize,ySize)
      m.g.noStroke()
      m.g.fill(0,255,0)
      m.g.rect(x,y,(amount/fullAmount).toFloat*xSize,ySize)
    }
  }
  
}