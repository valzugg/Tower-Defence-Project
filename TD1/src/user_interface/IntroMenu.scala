/**@author Valtteri Kortteisto */
package user_interface

import general.Helper
import files.Progress
import files.Level
import processing.core.PApplet

class IntroMenu(g: Game, p: Progress) extends Helper(g) {
  private var toggled = true
  def isOn = toggled
  def toggle() = {
    if (toggled)
      toggled = false
    else
      toggled = true
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
  
  
  // start menu buttons
  val progressButton = new Button("Progression Map",(6,3 ),(16,3),this)
  val savesButton    = new Button("Load Save",      (6,7 ),(16,3),this)
  val helpButton     = new Button("Help",           (6,11),(16,3),this)
  val creditsButton  = new Button("Credits",        (6,15),(16,3),this)
  
  // progress map buttons
  val first  = new Button("1",(7,5 ),(1,1),this)
  val second = new Button("2",(7,8 ),(1,1),this)
  val third  = new Button("3",(11,8),(1,1),this)
  val fourth = new Button("4",(11,2),(1,1),this)
  val fifth  = new Button("5",(16,2),(1,1),this)
  val sixth  = new Button("6",(16,12),(1,1),this)
  val seventh= new Button("7",(4,12),(1,1),this)
  val eigth  = new Button("8",(4,17),(1,1),this)
  val ninth  = new Button("9",(19,17),(1,1),this)
  val tenth  = new Button("10",(19,6),(1,1),this)
  val eleventh= new Button("11",(22,6),(1,1),this)
  val twelveth= new Button("12",(22,17),(1,1),this)
  
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
    fifth.draw()
    sixth.draw()
    seventh.draw()
    eigth.draw()
    ninth.draw()
    tenth.draw()
    eleventh.draw()
    twelveth.draw()
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
    first.clicking(100)
    second.clicking(101)
    third.clicking(102)
    fourth.clicking(103)
    fifth.clicking(104)
    sixth.clicking(105)
    seventh.clicking(106)
    eigth.clicking(107)
    ninth.clicking(108)
    tenth.clicking(109)
    eleventh.clicking(110)
    twelveth.clicking(111)
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
      case Saves    => savesClick()
      case Help     => helpClick()
      case Credits  => creditsClick()
      case _        => 
    }
  }
  
  def drawStuff() = {
    currentState match {
      case Start    => startDraw()
      case Progress => progressDraw()
      case Saves    => savesDraw()
      case Help     => helpDraw()
      case Credits  => creditsDraw()
      case _        => g.startLevel(g.levels(currentState - 100))
    }                  // starts the level of the index
  }
  
}

/**A Button class to simplify the menu.
 * Parameters pos and size and given in multipliers of sqSizes. */
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

