/**@author Valtteri Kortteisto */
package map

import processing.core.PApplet
import user_interface._
import files.Level
import scala.collection.mutable.Queue
import general.Helper

/** Represents the arena of a level. An arena is made of squares, 
 *  which can be Empty, Path, Obstacle, or Tower. 
 *  An arena also has a the directions for the path so the mobs can navigate it.
 *  Extends Helper to get some constants and generally useful methods.*/
class Arena(g: Game, l: Level) extends Helper(g) {
  val game = g.asInstanceOf[PApplet]
  lazy val dims = (l.width,l.height)
  
  // in order to read the level row by row, it has to be transposed afterwards
  lazy val squaresTransposed = Array.ofDim[Square](dims._2,dims._1)
  lazy val squares = squaresTransposed.transpose
  
  /** Resets the arena grid. */
  def resetSquares() = {
    val originalSquares = squaresTransposed.transpose
    for (col <- 0 until dims._1) {
      for (row <- 0 until dims._2) {
        squares(col)(row) = originalSquares(col)(row)
      }
    }
  }
  
  // the start and end rows of the level
  var start = 0
  var end   = 0
  
  // keeps track of the towers in the arena
  lazy val towers = Array.ofDim[Tower](dims._1,dims._2)
  
  def towersDoStuff() = towers.flatten.foreach(t => if (t != null) t.doStuff())
  
  /** Reset's the 'towers' grid, including defences.  */
  def resetTowers() = {
    for (col <- 0 until dims._1) {
      for (row <- 0 until dims._2) {
        if (squares(col)(row).isInstanceOf[Tower]) {
          towers(col)(row) = squares(col)(row).asInstanceOf[Tower]
          towers(col)(row).removeDef()
        } else {
          towers(col)(row) = null
        }
      }
    }
  }
  
  def apply(x: Int, y: Int) = {
    require(x >= 0 && x < aWidth)
    require(y >= 0 && y < aHeight)
    this.squares(x)(y)
  }
  
  /** Draws the arena as string.
   *  Not utilized. */
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
  
  /** Adds a given row of squares in the Array.
   *  Also sets the start and end row correctly for the mobs.*/
  def setRow(row: Int, line: Array[String]) = {
    if (line(0) == "1") { start = row } //start row set
    squaresTransposed(row) = line.zipWithIndex.map(x => makeSquare(x._1,x._2,row))
  }
  
  /** Turns a piece string in a .lvl file into a Square on this arena. */
  private def makeSquare(x: String, col: Int, row: Int) = {
    require(x == "0" || x == "1" || x == "x" || x == "t",
            "Unknown character used in level file's arena.")
    x match {
      case "0"  => new Empty(col,row)
      case "1"  => new Path( col,row)
      case "x"  => new Obstacle(col,row)
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
    var loc = (0,start)           // the start location
    var dir = right               // the start direction
    var steps = 0                 // how many steps (squares) so far
    val q   = Queue[(Int,Int)]()  // the queue of directions
    
    // goes through the path until the right side of the arena is reached
    while (loc._1 < aWidth - 1) {
      require(steps < 2000, "The path goes in an infinite loop or exceeds the length of 2000 steps.")
      require(if (loc._1 == 0) 
                dir != left 
              else if (loc._2 == aHeight) 
                dir != down
              else if (loc._2 == 0) 
                dir != up
              else true, "The path doesnt follow specifications.")
      q += checkDir(loc, dir)
      dir = q.last
      loc = (loc._1 + dir._1, loc._2 + dir._2)
      steps += 1
    }
    q += right // The path must end going right
    q.toVector
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
  
  // shadow distance for towers
  private val sDistTower = -sqSize/8
  // shadow distance for obstacles
  private val sDistObst = -sqSize/20
  
  def drawArena(width: Int, height: Int) = {
    for (col <- 0 until width) {
      for (row <- 0 until height) {
        val sq = this.squares(col)(row)
        if (sq.basic) {
          g.image(g.squares(sq.i), col * sqSize, row * sqSize, sqSize, sqSize)
        } else if (sq.isInstanceOf[Obstacle]) {
          g.image(g.squares(0), col * sqSize, row * sqSize, sqSize, sqSize)
          g.tint(0,0,0,sIntensity) // draw the shadow of the obstacles
          g.image(g.obstacles(sq.i), col * sqSize + sDistObst, row * sqSize + sDistObst, sqSize, sqSize)
          g.noTint()
          g.image(g.obstacles(sq.i), col * sqSize, row * sqSize, sqSize, sqSize)
        } else {
          g.image(g.squares(0), col * sqSize, row * sqSize, sqSize, sqSize)
          g.tint(0,0,0,sIntensity) // draw the shadow of the tower
          g.image(g.squares(2), col * sqSize + sDistTower, row * sqSize + sDistTower, sqSize, sqSize)
          g.noTint()
          g.image(g.squares(2), col * sqSize, row * sqSize, sqSize, sqSize)
        }
      }
    }
  }

}
