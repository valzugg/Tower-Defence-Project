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
  
  def onEasy = ???
  def onNormal = ???
  def onHard = ???
  def onInsane = ???
  
  def onCredits = ???
  
  def doStuff() = {
    g.image(g.introImg(0), 0, 0, aWidth*sqSize+mWidth*sqSize, aHeight*sqSize)
    g.textFont(g.font,24)
    g.noStroke()
    g.fill(0)
    g.text("Desert Tower Defence",11.4.toFloat*sqSize,1.65.toFloat*sqSize)
    g.fill(100,100,100,100)
    g.rect(6*sqSize,3*sqSize,16*sqSize,3*sqSize)
    g.fill(0)
    g.text("Easy Levels",11.4.toFloat*sqSize,4.65.toFloat*sqSize)
    g.fill(100,100,100,100)
    g.rect(6*sqSize,7*sqSize,16*sqSize,3*sqSize)
    g.fill(0)
    g.text("Normal Levels",11.4.toFloat*sqSize,8.65.toFloat*sqSize)
    g.fill(100,100,100,100)
    g.rect(6*sqSize,11*sqSize,16*sqSize,3*sqSize)
    g.fill(0)
    g.text("Hard Levels",11.4.toFloat*sqSize,12.65.toFloat*sqSize)
    g.fill(100,100,100,100)
    g.rect(6*sqSize,15*sqSize,16*sqSize,3*sqSize)
    g.fill(0)
    g.text("Credits",11.4.toFloat*sqSize,16.65.toFloat*sqSize)
    g.fill(100,100,100,100)
  }
  
}