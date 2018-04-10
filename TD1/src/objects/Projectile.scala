package objects

import scala.math._
import arena.Square

class Projectile(d: Defence, speed: Int, dam: Double) {
  val g = d.game
  val sqSize = Square.size
  val hitboxSize = 10
  var hasHit = false
  var age = 0
  
  var size = 7.0.toFloat
  
  val lifetime = 200/speed.toFloat // the time it takes for a new projectile to be made
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
  lazy val angle = {
    if (tx < x)
      atan((ty - y)/(tx - x)).toFloat + scala.math.Pi.toFloat
    else
      atan((ty - y)/(tx - x)).toFloat
  }
  
  /** direction in which the projectile should go as a vector with length 1.*/
  def dir = (xtx/hypot(xtx,yty).toFloat, yty/hypot(xtx,yty).toFloat)
  
  def damageTarget = {
    if ((x > tx - hitboxSize && x < tx + hitboxSize) && 
        (y > ty - hitboxSize && y < ty + hitboxSize)) {
      if (!hasHit) target.damage(dam)
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
  
  
  def doStuff() = {
    val di = dir
    age += 1
    move()
    if (!hasHit && d.withinRange(x,y)) {
      if (d.withinRange(target.pos)) curDir = di
      g.fill(0,0,0,255)
      g.stroke(0,0,0)
      g.line(x + di._1*size,y + di._2*size,x,y)
    }
  }
  
}