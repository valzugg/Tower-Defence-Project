package arena

import processing.core.PApplet

class Arena(width: Int = 20, height: Int = 16) {
  
  val squares = Array.ofDim[Square](width,height)
  
  private def makeSquare(x: String, col: Int, row: Int) = {
    if (x == "0") {
      new Empty(col,row)
    } else if (x == "1") {
      new Path(col,row)
    } else {
      new Tower(col,row)
    }
  }
  
  //adds a given row of squares in the Array
  def setRow(row: Int, line: Array[String]) = {
    squares(row) = line.zipWithIndex.map(x => makeSquare(x._1,x._2,row))
  }
  
  //def mouseSquare(x: Int, y: Int) = squares(x)(y)
  

}