package objects

import arena._
import user_interface._
import processing.core.PImage

/** Mob represents an enemy in a tower defence game 
 *  
 */
class Mob(sx: Float ,sy: Float ,val speed: Float, var img: PImage, p: Game) {
  val sqSize  = 40
  
  var x = sx
  var y = sy
  
  var dir = (0,0)
  
  //the mob's current square
  def square = {
    p.lvls(p.lvlN).arena.squares(x.toInt/sqSize)(y.toInt/sqSize)
  }
  
  //a square of given arena coordinates relative to the mob
  def nextSq(xx: Int,yy: Int) = {
    p.lvls(p.lvlN).arena.squares((y.toInt/sqSize) + yy)((x.toInt/sqSize) + xx)
  } //changin x and y around does magic
  
  
  //still a problem with the coords detection
  //intelligent movement
  //true if-helvetti
  def act() = {
    //when at start
    if (x < 0) {
      move(1,0)
    } else if (if (dir != (-1,0)) {      //checks only at every square
        x.toInt % 40 == 20 || y.toInt % 40 == 0 
      } else { 
        x.toInt % 40 ==  0 || y.toInt % 40 == 20 
      }) { 
      //moving right
      if (dir == (1,0)) {
        if (!nextSq(1,0).isInstanceOf[Path]) {
          if (nextSq(0,1).isInstanceOf[Path]) {
            move(0, 1)
          } else {
            move(0,-1)
          }
        } else {
          move(1,0)
        } //moving down
      } else if (dir == (0,1)) {
        if (!nextSq(0,1).isInstanceOf[Path]) {
          if (nextSq(1,0).isInstanceOf[Path]) {
            move(1, 0)
          } else {
            move(-1,0)
          }
        } else {
          move(0,1)
        } //moving left
      } else if (dir == (-1,0)) {
        if (!nextSq(-1,0).isInstanceOf[Path]) {
          if (nextSq(0,1).isInstanceOf[Path]) {
            move(0, 1)
          } else {
            move(0,-1)
          }
        } else {
          move(-1,0)
        } //moving up
      } else if (dir == (0,-1)) {
        if (!nextSq(0,-1).isInstanceOf[Path]) {
          if (nextSq(1,0).isInstanceOf[Path]) {
            move(1, 0)
          } else {
            move(-1,0)
          }
        } else {
          move(0,-1)
        }
      }
    } else {
      move(dir._1, dir._2)
    }
  }
  
  // move(1, 0) will move right
  // move(-1,0) will move left
  // move(0, 1) will move down
  // move(0,-1) will move up
  def move(mx: Int,my: Int) = {
    x += mx*speed
    y += my*speed
    dir = (mx,my)
  }
  
  
}