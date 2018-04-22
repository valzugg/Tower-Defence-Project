/**@author Valtteri Kortteisto */
package user_interface

import general.Helper
import files.Progress
import files.Level
import processing.core.PApplet

class IntroMenu(g: Game, p: Progress) extends Helper(g) {
  var isOn = true
  
  def toggle() = {
    if (isOn)
      isOn = false
    else
      isOn = true
  }
  
  
  def onEasy = ???
               
  def onNormal = ???
  def onHard   = ???
  def onInsane = ???
  
  def onCredits = ???
  
  // numbers which represent states of the menu
  private val Start    = 0
  private val Progress = 1
  private val Saves    = 2
  private val Help     = 3
  private val Credits  = 4
  
  var currentState = Start
  
  
  /**All the buttons in this class follow the same format. */
  def button(text: String, pos: (Int,Int), size: (Int,Int)) = {
    def onButton = (mouseX > pos._1 * sqSize) && (mouseY > pos._2 * sqSize) &&
                   (mouseX < pos._1 * sqSize + size._1 * sqSize) && 
                   (mouseY < pos._2 * sqSize + size._2 * sqSize)
    if (onButton) {
      g.noStroke()
      g.fill(0,0,0,100)
      g.rect(pos._1*sqSize - 4,pos._2*sqSize - 4,size._1*sqSize,size._2*sqSize)
      g.fill(255,255,255,150)
      g.rect(pos._1*sqSize,pos._2*sqSize,size._1*sqSize,size._2*sqSize)
      g.fill(0,0,0,100)
      g.text(text,2*sqSize + (size._1*sqSize/2) - 2,pos._2*sqSize + (size._2*sqSize*3/5) - 2)
    } else {
      g.noStroke()
      g.fill(100,100,100,100)
      g.rect(pos._1*sqSize,pos._2*sqSize,size._1*sqSize,size._2*sqSize)
    }
    g.fill(0)
    g.text(text,2*sqSize + (size._1*sqSize/2),pos._2*sqSize + (size._2*sqSize*3/5))
  }
  
  // start menu buttons
  val progressButton = new Button("Progression Map",(6,3 ),(16,3),this)
  val savesButton    = new Button("Load Save",      (6,7 ),(16,3),this)
  val helpButton     = new Button("Help",           (6,11),(16,3),this)
  val creditsButton  = new Button("Credits",        (6,15),(16,3),this)
  
  // progress menu buttons
  val first  = new Button("1",(7,5 ),(1,1),this)
  val second = new Button("2",(7,8 ),(1,1),this)
  val third  = new Button("3",(6,11),(1,1),this)
  val fourth = new Button("4",(6,15),(1,1),this)
  
  def startDraw() = {
    g.textFont(g.font,24)
    g.noStroke()
    g.fill(0)
    g.text("Desert Tower Defence",11.4.toFloat*sqSize,1.65.toFloat*sqSize)
    progressButton.draw()
    savesButton.draw()
    helpButton.draw()
    creditsButton.draw()
  }
  
  def progressDraw() = {
    first.draw()
    second.draw()
    third.draw()
    fourth.draw()
  }
  
  def savesDraw() = {
    
  }
  
  def helpDraw() = {
    
  }
  
  def creditsDraw() = {
    
  }
  
  def startClick() = {
    progressButton.clicking(Progress)
    savesButton.clicking(Saves)
    helpButton.clicking(Help)
    creditsButton.clicking(Credits)
  }
  
  def progressClick() = {
    
  }
  
  def savesClick() = {
    
  }
  
  def helpClick() = {
    
  }
  
  def creditsClick() = {
    
  }
  
  
  def clickingStuff() = {
    currentState match {
      case Start    => startClick()
      case Progress => progressClick()
      case Help     => helpClick()
      case Credits  => creditsClick()
    }
  }
  
  def drawStuff() = {
    currentState match {
      case Start    => startDraw()
      case Progress => progressDraw()
      case Help     => helpDraw()
      case Credits  => creditsDraw()
    }
  }
  
}

class Button(text: String, pos: (Int,Int), size: (Int,Int), i: IntroMenu) 
  extends Helper(i.g) {
  
  def mouseOn = (mouseX > pos._1 * sqSize) && (mouseY > pos._2 * sqSize) &&
                (mouseX < pos._1 * sqSize + size._1 * sqSize) && 
                (mouseY < pos._2 * sqSize + size._2 * sqSize)
  
  def draw() = {
    if (mouseOn) {
      g.noStroke()
      g.fill(0,0,0,100)
      g.rect(pos._1*sqSize - 4,pos._2*sqSize - 4,size._1*sqSize,size._2*sqSize)
      g.fill(255,255,255,150)
      g.rect(pos._1*sqSize,pos._2*sqSize,size._1*sqSize,size._2*sqSize)
      g.fill(0,0,0,100)
      g.text(text,pos._1*sqSize + (size._1*sqSize/6) - 2,pos._2*sqSize + (size._2*sqSize*3/5) - 2)
    } else {
      g.noStroke()
      g.fill(100,100,100,100)
      g.rect(pos._1*sqSize,pos._2*sqSize,size._1*sqSize,size._2*sqSize)
    }
    g.fill(0)
    g.text(text,pos._1*sqSize + (size._1*sqSize/6),pos._2*sqSize + (size._2*sqSize*3/5))
  }
  
  def clicking(state: Int): Unit = {
    if (mouseOn && g.mouseButton == leftMouse) {
      i.currentState = state
    }
  }
  
}

