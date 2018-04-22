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
  
  // numbers which represent states of the menu
  val Start    = 0
  val Progress = 1
  val Saves    = 2
  val Help     = 3
  val Credits  = 4
  
  
  var currentState = Start
  def changeState(state: Int) = currentState = state
  
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
  
  // saves menu buttons
  val save1 = new Button("Save 1",(8,3 ),(12,3),this)
  val save2 = new Button("Save 2",(8,7 ),(12,3),this)
  val save3 = new Button("Save 3",(8,11),(12,3),this)
  val save4 = new Button("Save 4",(8,15),(12,3),this)
  
  val goBack = new Button("< Back to Main Menu",(1,1),(5,2),this)
  
  def startDraw() = {
    g.textFont(g.font,34)
    g.fill(0,0,0,100)
    g.text("Desert Tower Defence",10.toFloat*sqSize-2,2*sqSize-2)
    g.fill(0)
    g.text("Desert Tower Defence",10.toFloat*sqSize,2*sqSize)
    g.textFont(g.font,24)
    progressButton.draw()
    savesButton.draw()
    helpButton.draw()
    creditsButton.draw()
  }
  
  def progressDraw() = {
    g.textFont(g.font,34)
    g.fill(0,0,0,100)
    g.text("Pick A Level",11.5.toFloat*sqSize-2,2*sqSize-2)
    g.fill(0)
    g.text("Pick A Level",11.5.toFloat*sqSize,2*sqSize)
    g.textFont(g.font,14)
    goBack.draw()
    g.textFont(g.font,24)
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
    g.textFont(g.font,14)
    goBack.draw()
    g.textFont(g.font,24)
    save1.draw()
    save2.draw()
    save3.draw()
    save4.draw()
  }
  
  def helpDraw() = {
    g.textFont(g.font,14)
    goBack.draw()
    g.textFont(g.font,24)
  }
  
  def creditsDraw() = {
    g.textFont(g.font,14)
    goBack.draw()
    g.textFont(g.font,24)
  }
  
  def startClick() = {
    progressButton.clicking(Progress)
    savesButton.clicking(Saves)
    helpButton.clicking(Help)
    creditsButton.clicking(Credits)
  }
  
  def progressClick() = {
    goBack.clicking(Start)
    // The numbers as the parameters are the indexes 
    // of the levels vector in Game, with 100 added.
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
    goBack.clicking(Start)
  }
  
  def helpClick() = {
    goBack.clicking(Start)
  }
  
  def creditsClick() = {
    goBack.clicking(Start)
  }
  
  
  def clickingStuff() = {
    currentState match {
      case Start    => startClick()
      case Progress => progressClick()
      case Saves    => savesClick()
      case Help     => helpClick()
      case Credits  => creditsClick()
      case _        => // level starting has to happen in draw 
    }                  // for it to take effect
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
      g.fill(0,0,100,100)
      g.rect(pos._1*sqSize - 2,pos._2*sqSize - 2,size._1*sqSize,size._2*sqSize)
      g.fill(200,200,255,150)
      g.rect(pos._1*sqSize,pos._2*sqSize,size._1*sqSize,size._2*sqSize)
      g.fill(0,0,0,100)
      g.text(text,pos._1*sqSize + (size._1*sqSize/6) - 2,pos._2*sqSize + (size._2*sqSize*3/5) - 2)
    } else {
      g.noStroke()
      g.fill(100,100,150,100)
      g.rect(pos._1*sqSize,pos._2*sqSize,size._1*sqSize,size._2*sqSize)
    }
    g.fill(0)
    g.text(text,pos._1*sqSize + (size._1*sqSize/6),pos._2*sqSize + (size._2*sqSize*3/5))
  }
  
  def clicking(state: Int): Unit = {
    if (mouseOn && g.mouseButton == leftMouse) {
      i.changeState(state)
    }
  }
  
}

