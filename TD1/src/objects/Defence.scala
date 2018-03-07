package objects

import user_interface.Game
import arena._
import objects._
import processing.core.PApplet
import scala.math._


//needs comments

class Defence(/**val t: Tower, */x: Float, y: Float, range: Int, damage: Double, g: Game) {
  val game = g.asInstanceOf[PApplet]
  val sqSize = 40
  val location = (x, y) 
  
  //keeps track of the target mob
  var t: Mob = closestMob 
  
  def target() = {
    if (!closestMob.dead) t = closestMob 
  }
  
  private def distance(pos1: (Float, Float), pos2: (Float, Float)) = {
    hypot(abs(pos1._1 - pos2._1), abs(pos1._2 - pos2._2))
  }
  
  private def closestMob: Mob = {
    g.wave.mobs.minBy(m => distance(location,(m.x,m.y)))
  }
  private var closest = closestMob
  
  private def closestDist = {
    distance(location,(closest.x,closest.y))
  }
  
  
  private def chooseTarget() = {
    if (!withinRange(targetPos) || t.dead) {
        target()
    }
  }
  
  private def targetPos = (t.x + sqSize/2, t.y + sqSize/2)
  
  private def withinRange(pos: (Float, Float)) = {
    distance(location,pos) < range
  }
  
  private def shoot() = {
    if (withinRange(t.x,t.y)) {
      chooseTarget()
      game.stroke(1)
      game.line(location._1,location._2,targetPos._1,targetPos._2)
      t.damage(damage)
    }
  }
  
  //still glitchy
  def doStuff() = {
    game.noStroke()
    game.fill(255,0,0)
    game.ellipse(location._1, location._2, sqSize/3, sqSize/3)
    shoot()
  }
  
}