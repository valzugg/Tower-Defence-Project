package arena

/** Represents a square on the arena grid.
 *  A square can be an instance of Empty, Path or
 *  Tower.
 */
trait Square {
  val i: Int //the index in level creation
  def doStuff(): Unit
  override def toString() = i.toString
}

object Square {
  val size = 40
}

case class Empty(x: Int, y: Int) extends Square { 
  val i = 0 ; def doStuff() = {} 
}
case class Path(x: Int, y: Int)  extends Square { 
  val i = 1 ; def doStuff() = {} 
}
case class Obstacle(x: Int, y: Int) extends Square { 
  val i = 9 ; def doStuff() = {} 
} //no working implementation yet