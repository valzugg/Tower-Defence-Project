package objects

import scala.math._
import arena.Square

class Projectile(d: Defence) {
  val g = d.game
  val sqSize = Square.size
  
  // location of the projectile
  var x = d.location._1
  var y = d.location._2
  // location of the target
  def tx = d.t.x + sqSize/2
  def ty = d.t.y + sqSize/2
  
  // the target location in relation to thep projectile
  def xtx = tx-x
  def yty = ty-y
  
  // distance of the defence and the mob (hypotenuse)
  def hyp = hypot(tx - x,ty - y)
  
  // the angle at which the projectile is shot
  def angle = atan((ty - y)/(tx - x)).toFloat
  
  // direction is which the projectile should go as a vector with length 1.
  def dir = {
    ((xtx/sqrt((xtx*xtx)+(yty*yty))).toFloat,(yty/sqrt((xtx*xtx)+(yty*yty))).toFloat)
  }

  def hitsTarget = (x > tx - 0.5 && x < tx + 0.5) || (y > ty - 0.5 && y < ty + 0.5) 
  
  def move() = {
    if (!hitsTarget) {
      x += dir._1
      y += dir._2
    } else {
      x = d.location._1
      y = d.location._2
    }
  }
  
  def doStuff() = {
    g.fill(255,255,255,255)
    g.stroke(255,255,255,255)
    g.ellipse(x,y,3,3)
    move()
    //println(x, tx)
  }
  
}