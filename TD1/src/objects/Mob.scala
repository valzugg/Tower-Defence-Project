/**@author Valtteri Kortteisto */
package objects

import map._
import user_interface._
import files.Level
import processing.core.PImage
import processing.core.PApplet
import general.Helper
import scala.math.abs

/** Represents an enemy in a tower defence game 
 *  
 */
class Mob(w: Wave ,var speed: Float, hitpoints: Int, g: Game, 
          val i: Int, size: Float = 0.25.toFloat, lvl: Level) extends Helper(g) {
  val game = g.asInstanceOf[PApplet]
  val hp = new HealthBar(this,hitpoints)
  val moneyValue = ((hitpoints/80)*speed).toInt
  val originalSpeed = speed
  val hitboxSize = (size * 40).toInt
  
  private var dist = -sqSize.toFloat // keeps track of how far the mob is along the path
  def distance = abs(dist)
  
  val r = scala.util.Random
  
  //keep track of the mob's location
  var x = (-sqSize * (i + 1) * w.distance) - (r.nextFloat() * w.distance * 20) 
  var y = lvl.pathStart*sqSize.toFloat - sqSize/12 + r.nextInt(sqSize/6)
  
  def pos = (x + sqSize/2,y + sqSize/2)
  
  //keeps track of the mob's current direction
  var dir   = (0,0)
  
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
  
  private def pathIndex = ((dist)/sqSize).toInt
  private def currentDir = lvl.path(pathIndex)
  
  /** The practical way the mob is moved.*/
  private def act() = {
    if (x >= sqSize*(g.arena.aWidth)) moveToStart()
    if (x < sqSize*(g.arena.aWidth - 1)) {
      if (x < 0) { //when at start
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
      case this.right => 
      case this.down  => game.rotate(halfPi)
      case this.left  => game.rotate(2*halfPi)
      case _          => game.rotate(3*halfPi)
    }
  }
  
  /** The concrete stuff that is sent to be done at the Game class.
   *  @param img The sprite of the mob as a PImage already loaded.*/
  def doStuff(img: PImage) = {
    if (!dead) {
      game.pushMatrix()
      game.translate(sqSize/2,sqSize/2)
      game.translate(this.x,this.y) // the 'axis' of the mob is being moved
      this.act()
      game.image(img,-sqSize*size,-sqSize*size,sqSize*2*size,sqSize*2*size)
      game.popMatrix()
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
  val xSize = 35
  val ySize = 3
  
  var amount = fullAmount
  
  def damage(by: Double) = { amount -= by }
  
  /** Drawing of the health bar when called in Game's draw().
   *  Drawn only if mob is alive and has been damaged.*/
  def doStuff() = {
    if (!m.dead && m.hasBeenDamaged) {
      m.game.noStroke()
      m.game.fill(255,0,0)
      m.game.rect(x,y,xSize,ySize)
      m.game.noStroke()
      m.game.fill(0,255,0)
      m.game.rect(x,y,(amount/fullAmount).toFloat*xSize,ySize)
    }
  }
  
}