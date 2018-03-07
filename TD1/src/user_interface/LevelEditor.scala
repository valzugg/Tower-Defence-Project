package user_interface

import processing._
import processing.core._
import processing.core.PConstants
import scala.util.Random
import file_parser._
import arena._

object LevelEditor extends App {
  PApplet.main("user_interface.LevelEditor")
}

class LevelEditor extends PApplet {
  val sqSize = 40
  
  override def settings() {
    size(sqSize*20,sqSize*15)
  }
  
  override def setup() {
    
  }
  
  override def draw() {
    
  }
}