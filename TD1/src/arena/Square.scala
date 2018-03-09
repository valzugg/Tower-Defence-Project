package arena

/** Represents a square on the arena grid.
 *  A square can be an instance of Empty, Path or
 *  Tower.
 */
trait Square {
  val i: Int //the index in level creation
  def doStuff(): Unit
}

object Square {
  val size = 40
}

case class Empty(x: Int, y: Int) extends Square { 
  val i = 0 ; def doStuff() = {} 
  override def toString() = "0"
}
case class Path(x: Int, y: Int)  extends Square { 
  val i = 1 ; def doStuff() = {} 
  override def toString() = "1"
}
case class Obstacle(x: Int, y: Int) extends Square { 
  val i = 0 ; def doStuff() = {} 
  override def toString() = "x"
} //no working implementation yet