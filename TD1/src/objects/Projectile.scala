package objects

import scala.math._
import arena.Square

class Projectile(d: Defence, damage: Int, speed: Int) {
  val g = d.game
  val sqSize = Square.size
  val hitboxSize = 4
  
  // location of the projectile
  var x = d.location._1
  var y = d.location._2
  // location of the target
  def tx = d.t.x + sqSize/2
  def ty = d.t.y + sqSize/2
  
  // the target location in relation to the projectile
  def xtx = tx-x
  def yty = ty-y
  
  // the angle at which the projectile is shot
  def angle = atan((ty - y)/(tx - x)).toFloat
  
  // direction in which the projectile should go as a vector with length 1.
  def dir = {
    ((xtx/sqrt((xtx*xtx)+(yty*yty))).toFloat,(yty/sqrt((xtx*xtx)+(yty*yty))).toFloat)
  }

  def hitsTarget = (x > tx - hitboxSize && x < tx + hitboxSize) && 
                   (y > ty - hitboxSize && y < ty + hitboxSize) 
  
  def damage(): Unit = {
    if (hitsTarget)
      d.t.damage(this.damage)
  }
  
  def move() = {
    if (!hitsTarget) {
      x += dir._1 * speed
      y += dir._2 * speed
    } else {
      x = d.location._1
      y = d.location._2
    }
  }
  
  def doStuff() = {
    damage()
    g.fill(0,0,255,255)
    g.stroke(0,0,255,255)
    g.ellipse(x,y,3,3)
    move()
    //println((x, y), (tx, ty))
  }
  
}