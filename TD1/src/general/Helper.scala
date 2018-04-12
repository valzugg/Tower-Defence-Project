package general

import user_interface.Game

class Helper(g: Game) {
  val player = g.player
  def arena  = g.arena
  val sqSize = 40
  val halfPi = (scala.math.Pi.toFloat/2)
  
  // mouse position in squares
  def mSqX = g.mouseX.toInt/sqSize
  def mSqY = g.mouseY.toInt/sqSize
  
  // arena size in squares
  val aWidth  = 20
  val aHeight = 15
  
  // vector directions
  val right = (1, 0)
  val up    = (0,-1)
  val down  = (0, 1)
  val left  = (-1,0)
  
  // processing specific
  val leftMouse = 37
}