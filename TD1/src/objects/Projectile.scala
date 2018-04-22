package objects

import scala.math._
import map.Square
import general.Helper

class Projectile(d: Defence, speed: Int, dam: Double) {
  val g = d.game
  val sqSize = d.sqSize
  var hasHit = false
  var age = 0
  
  var size = 7.0.toFloat
  
  // the time it takes for a new projectile to be made
  val lifetime = 300/speed.toFloat // limits the maximum range :(
  def died = age > lifetime/d.g.runSpeed
  
  // location of the projectile
  var x = d.location._1
  var y = d.location._2
  
  val target = d.t
  var curDir = dir
  
  def hitboxSize = target.hitboxSize
  
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
      if (!hasHit) target.damage(dam/4 + scala.util.Random.nextInt(dam.toInt))
      hasHit = true
      true
    } else {
      false
    }
  }
                   
  
  def move() = {
    x += curDir._1 * speed * d.g.runSpeed
    y += curDir._2 * speed * d.g.runSpeed
  }
  
  /**Draws and updates the projectile. */
  def doStuff() = {
    val di = dir
    age += 1
    move()
    if (!hasHit && d.withinRange(x,y)) {
      if (d.withinRange(target.pos) && !target.dead) curDir = di
      // shadow
      g.stroke(50,50,50,100)
      g.line(x + di._1*size-2,y + di._2*size-2,x-2,y-2)
      //actual projectile
      g.fill(0,0,0,255)
      g.stroke(50,50,50)
      g.line(x + di._1*size,y + di._2*size,x,y)
    }
  }
  
}