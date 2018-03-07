package arena

/** Represents a square on the arena grid.
 *  A square can be an instance of Empty, Path or
 *  Tower.
 */
trait Square {
  val i: Int //the index in level creation
}

case class Empty(x: Int, y: Int)    extends Square { val i = 0 }
case class Path(x: Int, y: Int)     extends Square { val i = 1 }
case class Obstacle(x: Int, y: Int) extends Square { val i = 0 } //no working implementation yet