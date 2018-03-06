package objects

import arena._
import user_interface._
import processing.core.PImage

/** Represents an enemy in a tower defence game 
 *  
 */
class Mob(sx: Float ,sy: Float ,val speed: Float, img: String, p: Game) {
  val sqSize  = 40
  
  //keep track of the mob's location
  var x = sx
  var y = sy
  
  //keeps track of the mob's current direction
  var dir = (0,0)
  
  //directions defined for ease of use
  val right = (1, 0)
  val left  = (-1,0)
  val down  = (0, 1)
  val up    = (0,-1)
  
  /** Returns the direction to the right of the given direction*/
  private def rightOf(x: (Int,Int)) = {
    if (x._2==0) (x._2,x._1) else (-x._2,x._1)
  }
  
  /** Returns the direction to the left of the given direction*/
  private def leftOf(x: (Int,Int)) = {
    if (x._1==0) (x._2,x._1) else (x._2,-x._1)
  }
  
  var rotate = false
  
  //the mob's current square
  def square = {
    p.lvls(p.lvlN).arena.squares(x.toInt/sqSize)(y.toInt/sqSize)
  }
  
  //a square of given arena coordinates relative to the mob
  private def nextSq(d: (Int, Int)) = {
    p.lvls(p.lvlN).arena.squares((y.toInt/sqSize) + d._2)((x.toInt/sqSize) + d._1)
  } //changin x and y around does magic
  
  
  /** The core of the act() method's algorithm.
   *  Checks the next square in the given direction and 
   *  moves the mob in the direction where possible.
   *  Note that it is assumed that a mob cannot be in a situation 
   *  where it would have to go back to where it came from.*/
  private def checkDir(d: (Int, Int)) = {
    if (dir == d) {
      if (!nextSq(d).isInstanceOf[Path]) {
        if (nextSq(rightOf(d)).isInstanceOf[Path]) {
          move(rightOf(d))
        } else {
          move(leftOf(d))
        }
      } else {
        move(d)
      }
    }
  }
  
  /** Moves the mob in the given direction and sets it's dir variable correct*/
  private def move(d: (Int, Int)) = {
    x += d._1*speed
    y += d._2*speed
    dir = (d._1,d._2)
  }
  
  
  /**The algorithm by which the mobs find their way on the path*/
  def act() = {
    //when at start
    if (x < 0) {
      move(right)
    } else if (if (dir != left) {    
        x.toInt % 40 == 20 || y.toInt % 40 == 0 
      } else {     //checks only at every square
        x.toInt % 40 ==  0
      }) { 
      checkDir(dir)
    } else {
      move(dir)
    }
  }
 
}