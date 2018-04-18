package map

/** Represents a square on the arena grid.
 *  A square can be an instance of Empty, Path, Obstacle or
 *  Tower.
 */
trait Square {
  val i: Int //the index in level creation
  def doStuff(): Unit
  val basic: Boolean
  override def toString() = i.toString
}


case class Empty(x: Int, y: Int) extends Square { 
  val i = 2 ; def doStuff() = {} 
  val basic = true
}
case class Path(x: Int, y: Int)  extends Square { 
  val i = 3 ; def doStuff() = {} 
  val basic = true
}
case class Obstacle(x: Int, y: Int) extends Square { 
  def doStuff() = {} 
  val basic = false
  val i = scala.util.Random.nextInt(4)
}