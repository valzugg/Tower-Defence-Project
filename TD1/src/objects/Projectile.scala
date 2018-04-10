package objects

import scala.math._
import arena.Square

class Projectile(d: Defence, speed: Int) {
  val g = d.game
  val sqSize = Square.size
  val hitboxSize = 10
  var hasHit = false
  var age = 0
  val lifetime = speed * 5 // the time it takes for a new projectile to be made
  def died = age > lifetime
  
  // location of the projectile
  var x = d.location._1
  var y = d.location._2
  
  val target = d.t
  var curDir = dir
  
  // location of the target
  def tx = target.x + sqSize/2
  def ty = target.y + sqSize/2
  
  // the target location in relation to the projectile
  def xtx = tx - x
  def yty = ty - y
  
  // the angle at which the projectile is shot
  def angle = atan((ty - y)/(tx - x)).toFloat
  
  /** direction in which the projectile should go as a vector with length 1.*/
  def dir = (xtx/hypot(xtx,yty).toFloat, yty/hypot(xtx,yty).toFloat)
  
  def hitsTarget = {
    if ((x > tx - hitboxSize && x < tx + hitboxSize) && 
        (y > ty - hitboxSize && y < ty + hitboxSize)) {
      hasHit = true
      true
    } else {
      false
    }
  }
                   
  
  def move() = {
    x += curDir._1 * speed
    y += curDir._2 * speed
  }
  
  // TODO: Jotain pielessä tässä tai defencen puolella
  // kaikki voisi mieluiten tapahtua yhdessä paikassa
  def doStuff() = {
    if (d.withinRange(target.pos)) {
      age += 1
      move()
      if (!hasHit) {
        curDir = dir
        g.fill(255,255,255,255)
        g.noStroke()
        g.ellipse(x,y,3,3)
      }
    }
  }
  
}