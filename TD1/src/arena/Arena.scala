package arena

import processing.core.PApplet
import user_interface._
import file_parser._
import scala.collection.mutable.Queue
import general.Helper

class Arena(g: Game, l: Level) extends Helper(g) {
  val game = g.asInstanceOf[PApplet]
  val dims = (l.width,l.height)
  
  lazy val squaresTransposed = Array.ofDim[Square](dims._2,dims._1)
  lazy val squares = squaresTransposed.transpose
  
  // the start and end rows of the level
  var start = 0
  var end   = 0
  
  // keeps track of the towers in the arena
  lazy val towers = Array.ofDim[Tower](dims._1,dims._2)
  
  
  def apply(x: Int, y: Int) = {
    require(x >= 0 && x < aWidth)
    require(y >= 0 && y < aHeight)
    this.squares(x)(y)
  }
  
  override def toString() = {
    val s = Queue[String]()
    for (row <- 0 until aHeight) {
      for (col <- 0 until aWidth) {
        s += (this(col,row) + " ")
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
      case "t"  => {
        towers(col)(row) = new Tower(col,row)
        towers(col)(row)
      }
    }
  }
  
  /** Returns the direction to the right of the given direction*/
  private def rightOf(d: (Int,Int)) = {
    if (d._2==0) (d._2,d._1) else (-d._2,d._1)
  }
  
  /** Returns the direction to the left of the given direction*/
  private def leftOf(d: (Int,Int)) = {
    if (d._1==0) (d._2,d._1) else (d._2,-d._1)
  }
  
  //a square of given arena coordinates relative to the mob
  private def nextSq(loc : (Int, Int), d: (Int, Int)) = {
    this(loc._1 + d._1,loc._2 + d._2)
  }
  
  /** 
   *  @param d The direction in which the mob is moving as a vector.*/
  private def checkDir(loc: (Int, Int), d: (Int, Int)) = {
    nextSq(loc,d) match {                  //checks the square in the front
      case Path(_,_) => d
      case _ => {
        nextSq(loc,rightOf(d)) match {     //checks the square on the right
          case Path(_,_) => rightOf(d)
          case _         => leftOf(d) } }
    } 
  }
  
  
  /** Creates a Vector of directions of the path which the mob uses to navigate. */
  def path: Vector[(Int,Int)] = {
    var loc = (0,start)
    var dir = right
    val q   = Queue[(Int,Int)]()
    
    while (loc._1 < aWidth - 1) {
      q += checkDir(loc, dir)
      dir = q.last
      loc = (loc._1 + dir._1, loc._2 + dir._2)
    }
    q += right // The path must end going right
    q.toVector
  }
  
  
  //adds a given row of squares in the Array
  //also sets the start row correctly for the mobs
  def setRow(row: Int, line: Array[String]) = {
    if (line(0) == "1") { start = row } //start row set
    squaresTransposed(row) = line.zipWithIndex.map(x => makeSquare(x._1,x._2,row))
  }
  
  /** Sets a Tower in the given position if possible.
   *  Only possible if the position is Empty. Returns
   *  Boolean indicating if the placing was successful.*/
  def setTower(x: Int, y: Int): Boolean = {
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
        val sq = this.squares(col)(row)
        if (sq.basic) {
          g.image(g.squares(sq.i), col * sqSize, row * sqSize, sqSize, sqSize)
        } else if (sq.isInstanceOf[Obstacle]) {
          g.image(g.squares(2), col * sqSize, row * sqSize, sqSize, sqSize)
          g.image(g.obstacles(sq.i), col * sqSize, row * sqSize, sqSize, sqSize)
        } else {
          g.image(g.squares(2), col * sqSize, row * sqSize, sqSize, sqSize)
          g.image(g.squares(4), col * sqSize, row * sqSize, sqSize, sqSize)
          sq.asInstanceOf[Tower].doStuff()
        }
      }
    }
  }

}
