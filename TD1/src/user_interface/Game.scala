package user_interface

import processing._
import processing.core._
import scala.util.Random
import file_parser._

// https://processing.org/reference/
// https://www.youtube.com/thecodingtrain
// http://kenney.nl/assets/tower-defense-top-down

class Game extends PApplet {
  def r = Random.nextInt(2)
  
  val lvl1 = new Level("lvls/lvl1.txt")
  
  val aWidth  = 20 //arena's width
  val aHeight = 15 //arena's height
  
  val mWidth  = 6  //menu's width
  val mHeight = 15 //menu's height
  
  val arena = Array.ofDim[PImage](3)
  var bg: PImage = null
  
  override def setup() {
    background(123)
    frameRate(60)
    arena(0) = loadImage("imgs/grass.png")
    arena(1) = loadImage("imgs/path.png")
    arena(2) = loadImage("imgs/tower.png")
    
    
    for (row <- 0 until aHeight) {
      for (col <- 0 until aWidth) {
        image(arena(lvl1.arena.squares(row)(col).i), col * 40, row * 40, 40, 40)
      }
    }
    
  }
  
  //sets the size of the window
  override def settings() {
    size(aWidth * 40 + 240, aHeight * 40)
    
  }
  
  override def draw() {

//    rect(mouseX, mouseY, 55, 55)
//    lvl1.arena.squares(mouseX)(mouseY)
  }
  
}

//makes the program runnable
object Game extends App {
  PApplet.main("user_interface.Game")
}

