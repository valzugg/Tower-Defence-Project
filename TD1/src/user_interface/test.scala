package user_interface

import processing.core.PApplet

class Test(p: Game) {
  var parent: PApplet = p.asInstanceOf[PApplet] // The parent PApplet that we will render ourselves onto
  var x = 0

  // Draw stripe
  def display() {
    //parent.rotate(100)
    parent.rect(x,41,4,300)
    move()
  }
  
  def move() = {
    x += 1
  }

}
