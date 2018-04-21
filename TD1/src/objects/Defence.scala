/**@author Valtteri Kortteisto */
package objects

import user_interface.Game
import map._
import objects._
import processing.core.PApplet
import scala.math._
import general.Helper

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
abstract class Defence(val tower: Tower, val range: Int, val damage: Double, 
                       val speed: Int, val cost: Int, val g: Game, spriteI: Int, 
                       soundI: Int, val title: String, val description: String) extends Helper(g) {
  val game = g.asInstanceOf[PApplet]
  val location = tower.pos
  
  /**keeps track of the target mob*/
  var t: Mob = closestMob 
  
  // the current projectile
  var bullet: Projectile = null
  
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
  def distance(pos1: (Float, Float), pos2: (Float, Float)) = {
    hypot(abs(pos1._1 - pos2._1), abs(pos1._2 - pos2._2))
  }
  
  /**Returns the alive mob closest to this defence. 
   * Used when retargeting. see target().*/
  private def closestMob: Mob = {
    if (g.currentWave.isComplete)
      g.currentWave.mobs(0) // prevents defences created between waves from glitching
    else
      g.currentWave.aliveMobs.minBy(m => distance(location,(m.x,m.y)))
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
   * Also damages the target mob accordingly.*/
  def shoot() = {
    if (bullet != null) bullet.doStuff()
    if (!g.currentWave.isComplete) {
      if (bullet == null || bullet.died) chooseTarget()
      if ((bullet == null || bullet.died)) {
        if (withinRange(targetPos)) {
          bullet = new Projectile(this, speed, damage)
          g.sounds.play(g.sounds.arr(soundI))
        }
      } else if (bullet != null && withinRange(targetPos)) {
        bullet.damageTarget
      }
    }
  }
  
  
  /**The method which is defined differently for every defence */
  def speciality(): Unit
  
  /**The method that is called in Game class.
   * Draws the defence, and shows the range as a circle. 
   * The speciality() method is also called here. 
   * Note that it is called before shoot().*/
  def doStuff() = {
    game.image(g.defences(spriteI), location._1 - sqSize/2, 
               location._2 - sqSize/2, sqSize, sqSize)
    if ((mSqX*sqSize + sqSize/2,mSqY*sqSize + sqSize/2) == this.location) {
      game.noFill()
      game.stroke(0,0,0,100)
      game.ellipse(location._1, location._2, range*2, range*2)
    }
    if (t != null) {
      speciality()
      shoot()
    }
  }
  
}

/**A defence without a speciality.*/
class BasicDefence(tower: Tower, range: Int, damage: Int, speed: Int, 
                   cost: Int, g: Game, spriteI: Int = 0, soundI: Int = 0,
                   title: String, description: String) 
extends Defence(tower,range,damage,speed,cost,g,spriteI,soundI,title,description) {
  
  /**Does nothing.*/
  def speciality() = {}
  
  override def doStuff() = {
    if (bullet != null) {
      game.pushMatrix()
      game.translate(location._1,location._2)
      game.rotate(bullet.angle)
      game.image(g.defences(spriteI),-sqSize/2,-sqSize/2, sqSize, sqSize)
      game.popMatrix()
    } else {
      game.image(g.defences(spriteI), location._1 - sqSize/2, 
                 location._2 - sqSize/2, sqSize, sqSize)
    }
    if ((mSqX*sqSize + sqSize/2,mSqY*sqSize + sqSize/2) == this.location) {
      game.noFill()
      game.stroke(0,0,0,100)
      game.ellipse(location._1, location._2, range*2, range*2)
    }
    if (t != null) shoot()
  }
  
}


/**Defence which slow the opponent down by the given ratio of slowBy when being shot at. */
class IceDefence(tower: Tower, range: Int, damage: Int, speed: Int, 
                 cost: Int, g: Game, val slowBy: Float, spriteI: Int = 1,soundI: Int = 5,
                 title: String, description: String) 
extends Defence(tower,range,damage,speed,cost,g,spriteI,soundI,title,description) {
  
  //the original speed of the mob
  def tSpeed = g.currentWave.speed

  def mobsInRange =  g.currentWave.aliveMobs.filter(m => this.withinRange(m.x + sqSize/2,m.y + sqSize/2))
  
  // the changing circle size that is emitted
  private var circleSize = 0
    
  /**Slows all the mobs within range down.*/
  def speciality() = {
    if (!mobsInRange.isEmpty) {
      circleSize += 2 * g.runSpeed
      mobsInRange.foreach(_.speed = tSpeed.toFloat * (1 - slowBy))
      game.noFill()
      game.stroke(0,0,250,200 - circleSize%range*2)
      game.ellipse(location._1, location._2, circleSize%range*2, circleSize%range*2)
      // sound
      if (circleSize%range*2 == 0) g.sounds.play(g.sounds.arr(soundI))
    } else {
      circleSize = 0
    }
  }
  
  override def shoot() = {}
  
}


/**Defence which 'chain targets' another mob as well, and damages that by 50% of the normal damage. */
class FireDefence(tower: Tower, range: Int, damage: Int, speed: Int, 
                  cost: Int, g: Game, spriteI: Int = 2, soundI: Int = 0,
                  title: String, description: String) 
extends Defence(tower,range,damage,speed,cost,g,spriteI,soundI,title,description) {
  
  //the other target
  var t2 = {
    if (g.currentWave.aliveMobs.size > 1)
      secondMob
    else
      null
  }

  private def chooseT2() = {
    if (g.currentWave.aliveMobs.size > 1)
      t2 = secondMob
  }
  
  private def secondMob = {
    g.currentWave.aliveMobs.sortBy(m => distance((t.x,t.y),(m.x,m.y))).
    filter(_ != t)(0)
  }
  
  /**Targets and damages another mob which is also within range.
   * A line is drawn between the target mob and the other mob.*/
  def speciality() = {
    if (t2 != null) {
      if (t.dead || t2.dead || !withinRange(t2.pos)) {
        chooseT2()
      } else {
        //game.stroke(color._1,color._2,color._3,color._4)
        //game.line(targetPos._1,targetPos._2,t2.x + sqSize/2,t2.y + sqSize/2)
        t2.damage(damage/2) //the second target experiences only half of the normal damage
      }
    }
  }
}


