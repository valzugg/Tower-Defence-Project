/**@author Valtteri Kortteisto */
package user_interface

import general.Helper
import files.Progress
import files.Level
import processing.core.PApplet

class IntroMenu(g: Game, p: Progress) extends Helper(g) {
  val game = g.asInstanceOf[PApplet]
  
  var isOn = true
  
  def clickingStuff() = {
    ???
  }
  
  def onEasy = (mouseX > 6 * sqSize) && (mouseY > 3* sqSize) &&
               (mouseX < 6 * sqSize + buttonSize._1 * sqSize) && 
               (mouseY < 3 * sqSize + buttonSize._2 * sqSize)
               
  def onNormal = ???
  def onHard = ???
  def onInsane = ???
  
  def onCredits = ???
  
  val buttonSize = (16 * sqSize,3 * sqSize)
  
  /**All the buttons in this class follow the same format. */
  def button(text: String, pos: Int) = {
    def onButton = (mouseX > 6 * sqSize) && (mouseY > pos * sqSize) &&
                   (mouseX < 6 * sqSize + buttonSize._1) && 
                   (mouseY < pos * sqSize + buttonSize._2)
    if (onButton) {
      //g.stroke(0)
      g.fill(255,255,255,150)
      g.rect(6*sqSize,pos*sqSize,buttonSize._1,buttonSize._2)
    } else {
      //g.noStroke()
      g.fill(100,100,100,100)
      g.rect(6*sqSize,pos*sqSize,buttonSize._1,buttonSize._2)
    }
    g.textFont(g.font,24)
    g.fill(0)
    g.text(text,6*sqSize + (buttonSize._1/2),pos*sqSize + (buttonSize._2/2))
  } // TODO: Really slow for some reason?
  
  def doStuff() = { // processing doesnt like drawing big images // TODO: arena in the background.
    //g.image(g.introImg(0), 0, 0, aWidth*sqSize+mWidth*sqSize, aHeight*sqSize)
    g.textFont(g.font,24)
    g.noStroke()
    g.fill(0)
    g.text("Desert Tower Defence",11.4.toFloat*sqSize,1.65.toFloat*sqSize)
    button("Easy Levels",3)
    button("Normal Levels",7)
    button("Hard Levels",11)
    button("Credits",15)
  }
  
}