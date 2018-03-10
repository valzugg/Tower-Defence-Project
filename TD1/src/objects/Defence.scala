package objects

import user_interface.Game
import arena._
import objects._
import processing.core.PApplet
import scala.math._


/** Represents a defence unit which has been assigned to a tower.
 *  In this abstract class only the speciality() method is undefined.
 *  @param tower The tower to which this defence belongs. 
 *  A defence doesnt exist without being in a tower.
 *  @param range The range of the defence in pixels
 *  @param damage The damage per hit caused to hit mob
 *  @param speed The speed at which defence fires
 *  @param cost The cost this defence has in the store
 *  @param i The index of this defence's sprite
 *  @param g The game of which it is a part */
abstract class Defence(val tower: Tower, range: Int, damage: Double, speed: Double, cost: Int, i: Int, g: Game) {
  val game = g.asInstanceOf[PApplet]
  val sqSize = Square.size
  val location = tower.pos
  
  /**keeps track of the target mob*/
  var t: Mob = closestMob 
  
  /**Determines whether the target mob is dead. */
  def deadTarget = t.dead
  
  /**Retargets the defence, in other words, changes the target mob
   * to the closest alive mob. It makes sense to target the closest mob,
   * because that mob will stay within the range for at least some time before
   * going out of range, and in doing so, it is not needed to know any other
   * details about the mob, like its direction.*/
  private def target() = { t = closestMob }
  
  /** Returns the distance between two positions in 2D space.
   *  A position is represented by a tuple of Float numbers, where
   *  the first number is the x-coord and second the y-coord.*/
  private def distance(pos1: (Float, Float), pos2: (Float, Float)) = {
    hypot(abs(pos1._1 - pos2._1), abs(pos1._2 - pos2._2))
  }
  
  /**Returns the alive mob closest to this defence. 
   * Used when retargeting. see target()*/
  private def closestMob: Mob = {
    g.wave.mobs.sortBy(m => distance(location,(m.x,m.y))).
    filter(!_.dead).reverse.last
  }
  
  /**Determines when defence is retargeted, and retargets it if necessary. 
   * A defence is retargeted if the target mob is no longer within range, 
   * or it is dead.*/
  private def chooseTarget() = {
    if (!withinRange(targetPos) || t.dead) {
        target()
    }
  }
  
  /**Returns the position of the center of the current target mob. */
  def targetPos = (t.x + sqSize/2, t.y + sqSize/2)
  
  /**Checks if a given position is within the range of this defence.*/
  def withinRange(pos: (Float, Float)) = {
    distance(location,pos) < range
  }
  
  /**Takes care of drawing the shooting animation when a target is being shot.
   * Also damages the target mob accordingly. The speciality() method is also
   * called here. Note that it is called before anything else.*/
  private def shoot() = {
    speciality()
    chooseTarget()
    if (withinRange(targetPos)) {
      game.stroke(255,0,0,150)
      game.line(location._1,location._2,targetPos._1,targetPos._2)
      t.damage(damage)
    }
  }
  
  /**The method which is defined differently for every defence */
  def speciality(): Unit
  
  /**The method that is called in Game class.
   * Draws the defence, and shows the range as a circle.*/
  def doStuff() = {
    game.image(g.defences(i), location._1 - sqSize/2, location._2 - sqSize/2, sqSize, sqSize)
    game.noFill()
    game.stroke(0,0,0,100)
    game.ellipse(location._1, location._2, range*2, range*2)
    shoot()
  }
  
}

class IceDefence(tower: Tower, range: Int, damage: Double, speed: Double, cost: Int, i: Int, g: Game) 
extends Defence(tower,range,damage,speed,cost,i,g) {
  
  //the original speed of the mob
  val tSpeed = g.currentWave.speed

  /**Slows the mob down while it is being shot.*/
  def speciality() = {
    if (withinRange(targetPos)) {
      if (tSpeed < 2*t.speed) 
        t.speed = t.speed * 0.4.toFloat
    } else {
      t.speed = tSpeed.toFloat
    }
  }
}

