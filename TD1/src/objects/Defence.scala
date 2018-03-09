package objects

import user_interface.Game
import arena._
import objects._
import processing.core.PApplet
import scala.math._


//needs comments

class Defence(val tower: Tower, range: Int, damage: Double, g: Game) {
  val game = g.asInstanceOf[PApplet]
  val sqSize = Square.size
  val location = tower.pos
  
  //keeps track of the target mob
  var t: Mob = closestMob 
  
  def deadTarget = t.dead
  
  //retargets the defence, in other words, changes the target mob
  def target() = { t = closestMob }
  
  /** Returns the distance between two positions in 2D space */
  private def distance(pos1: (Float, Float), pos2: (Float, Float)) = {
    hypot(abs(pos1._1 - pos2._1), abs(pos1._2 - pos2._2))
  }
  
  //why choose the closest mob tho?
  private def closestMob: Mob = {
    g.wave.mobs.sortBy(m => distance(location,(m.x,m.y))).
    filter(!_.dead).reverse.last
  }
  
  private def chooseTarget() = {
    if (!withinRange(targetPos) || t.dead) {
        target()
    }
  }
  
  //the center of the target mob
  private def targetPos = (t.x + sqSize/2, t.y + sqSize/2)
  
  private def withinRange(pos: (Float, Float)) = {
    distance(location,pos) < range
  }
  
  
  
  private def shoot() = {
    chooseTarget()
    if (withinRange(targetPos)) {
      game.stroke(255,0,0,150)
      game.line(location._1,location._2,targetPos._1,targetPos._2)
      t.damage(damage)
    }
  }
  
  //still glitchy
  def doStuff() = {
    game.noStroke()
    game.fill(255,0,0)
    game.ellipse(location._1, location._2, sqSize/3, sqSize/3)
    game.noFill()
    game.stroke(0,0,0,100)
    game.ellipse(location._1, location._2, range*2, range*2)
    shoot()
  }
  
}