package arena

import processing.core.PApplet
import user_interface._

class Arena(p: Game, width: Int = 20, height: Int = 15) {
  val parent = p.asInstanceOf[PApplet]
  val squares = Array.ofDim[Square](width,height)
  
  def towerCount = squares.flatten.filter(_.isInstanceOf[Tower]).length
  
  private def makeSquare(x: String, col: Int, row: Int) = {
    x match {
      case "0" => new Empty(col,row)
      case "1" => new Path( col,row)
      case _   => new Tower(col,row)
    }
  }
  
  //adds a given row of squares in the Array
  def setRow(row: Int, line: Array[String]) = {
    squares(row) = line.zipWithIndex.map(x => makeSquare(x._1,x._2,row))
  }
  
  def setTower(x: Int, y: Int) = {
    if (squares(x)(y).isInstanceOf[Empty]) {
      squares(x)(y) = new Tower(x,y)
      true
    } else {
      false
    }
  }

}