package arena

import processing.core.PApplet
import user_interface._
import scala.collection.mutable.Buffer

class Arena(g: Game, width: Int = 20, height: Int = 15) {
  val game = g.asInstanceOf[PApplet]
  val squaresTransposed = Array.ofDim[Square](height,width)
  var squares = squaresTransposed.transpose
  val sqSize = Square.size
  
  val sizeX = 20
  val sizeY = 15
  
  // the start and end rows of the level
  var start = 0
  var end   = 0
  
  //keeps track of the towers in the arena
  val towers = Array.ofDim[Tower](width,height)
  
  override def toString() = {
    val s = Buffer[String]()
    for (col <- 0 until sizeX) {
      for (row <- 0 until sizeY) {
        s += (squares(col)(row) + " ")
      }
      s += "\n"
    }
    s.reduceLeft(_+_)
  }
  
  private def makeSquare(x: String, col: Int, row: Int) = {
    x match {
      case "0" => new Empty(col,row)
      case "1" => new Path( col,row)
      case "x" => new Obstacle(col,row)
      case  _  => new Tower(col,row)
    }
  }
  
  
  def path: Vector[(Int,Int)] = {
    //TODO
    ???
  }
  
  
  //adds a given row of squares in the Array
  //also sets the start row correctly for the mobs
  def setRow(row: Int, line: Array[String]) = {
    if (line(0) == "1")     { start = row } //start row set
    squaresTransposed(row) = line.zipWithIndex.map(x => makeSquare(x._1,x._2,row))
  }
  
  /** Sets a Tower in the given position if possible.
   *  Only possible if the position is Empty. Returns
   *  Boolean indicating if the placing was successful.*/
  def setTower(x: Int, y: Int) = {
    if (squares(x)(y).isInstanceOf[Empty]) {
      towers(x)(y) = new Tower(x,y) 
      squares(x)(y) = towers(x)(y)
      true
    } else {
      false
    }
  }
  
  def drawArena() = {
    for (col <- 0 until g.aWidth) {
      for (row <- 0 until g.aHeight) {
        if (!this.squares(col)(row).isInstanceOf[Tower]) {
          g.image(g.squares(this.squares(col)(row).i), 
                col * sqSize, row * sqSize, sqSize, sqSize)
        } else {
          g.image(g.squares(0), col * sqSize, row * sqSize, sqSize, sqSize)
          g.image(g.squares(2), col * sqSize, row * sqSize, sqSize, sqSize)
        }
      }
    }
  }

}