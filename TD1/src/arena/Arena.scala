package arena

import processing.core.PApplet
import user_interface._

class Arena(g: Game, width: Int = 20, height: Int = 15) {
  val parent = g.asInstanceOf[PApplet]
  val squares = Array.ofDim[Square](width,height)
  
  val sizeX = 20
  val sizeY = 15
  
  // the start and end rows of the level
  var start = 0
  var end   = 0
  
  def towerCount = squares.flatten.filter(_.isInstanceOf[Tower]).length
  
  private def makeSquare(x: String, col: Int, row: Int) = {
    x match {
      case "0" => new Empty(col,row)
      case "1" => new Path( col,row)
      case "x" => new Obstacle(col,row)
      case  _  => new Tower(col,row)
    }
  }
  
  //adds a given row of squares in the Array
  //also sets the start row correctly for the mobs
  def setRow(row: Int, line: Array[String]) = {
    if (line(0) == "1")     { start = row } //start row set
    squares(row) = line.zipWithIndex.map(x => makeSquare(x._1,x._2,row))
  }
  
  /** Sets a Tower in the given position if possible.
   *  Only possible if the position is Empty. Returns
   *  Boolean indicating if the placing was successful.*/
  def setTower(x: Int, y: Int) = {
    if (squares(x)(y).isInstanceOf[Empty]) {
      squares(x)(y) = new Tower(x,y)
      true
    } else {
      false
    }
  }

}