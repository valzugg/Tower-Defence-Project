/**@author Valtteri Kortteisto */
package general

import user_interface.Game

/** Contains some general references and constants.
 *  Made to extend other classes with. */
class Helper(val g: Game) {
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
  lazy val right = (1, 0)
  lazy val up    = (0,-1)
  lazy val down  = (0, 1)
  lazy val left  = (-1,0)
  
  // processing specific
  lazy val leftMouse = 37
  lazy val enter     = 10
  lazy val esc       = 27
  lazy val tab       = 9
  
  // misc
  lazy val sIntensity = 100
}
