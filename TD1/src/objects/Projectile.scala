package objects

import scala.math._

class Projectile(d: Defence) {
  // location of the projectile
  var x = d.location._1
  var y = d.location._2
  // location of the target
  def tx = d.t.x
  def ty = d.t.y
  // distance of the defence and the mob (hypotenuse)
  def hyp = hypot(tx - x,ty - y)
  
  // direction is which the projectile should go as a vector.
  def dir = (asin((tx - x)/hyp).toFloat,acos(hyp/(ty - y)).toFloat)
  // TODO
}