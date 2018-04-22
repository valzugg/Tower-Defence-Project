/**@author Valtteri Kortteisto */
package general

import user_interface.Game

/** Contains some general references and constants.
 *  Made to extend other classes with. */
class Helper(g: Game) {
  lazy val player = g.player
  def gameOver = player.hp <= 0
  val sounds = g.sounds
  def arena  = g.arena
  val sqSize = 40
  val halfPi = (scala.math.Pi.toFloat/2)
  
  def mouseX = g.mouseX
  def mouseY = g.mouseY
  
  // mouse position in squares
  def mSqX = mouseX/sqSize
  def mSqY = mouseY/sqSize
  
  // arena size in squares
  def aWidth  = g.arena.dims._1
  def aHeight = g.arena.dims._2
  
  val mWidth  = 4
  
  // vector directions
  val right = (1, 0)
  val up    = (0,-1)
  val down  = (0, 1)
  val left  = (-1,0)
  
  // processing specific
  val leftMouse = 37
  val enter     = 10
  
  // misc
  lazy val sIntensity = 100
}
