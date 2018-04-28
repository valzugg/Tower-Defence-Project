package map

/** Represents a square on the arena grid.
 *  A square can be an instance of Empty, Path, 
 *  Obstacle or Tower.*/
trait Square {
  val i: Int //the index in level creation
  val basic: Boolean  // tells if the squares type is of empty or path
  override def toString() = i.toString
}


case class Empty(x: Int, y: Int) extends Square { 
  val i = 0 
  val basic = true
}
case class Path(x: Int, y: Int)  extends Square { 
  val i = 1
  val basic = true
}
case class Obstacle(x: Int, y: Int) extends Square { 
  val basic = false
  // the obstacle sprite is chosen randomly
  val i = scala.util.Random.nextInt(4)
}