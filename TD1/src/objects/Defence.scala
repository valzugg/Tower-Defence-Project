package objects

import user_interface.Game
import arena._
import objects._
import processing.core.PApplet

class Defence(val t: Tower, g: Game) {
  val game = g.asInstanceOf[PApplet]
  
  def shoot() = ???
  
}