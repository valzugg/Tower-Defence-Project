/**@author Valtteri Kortteisto */
package user_interface

import general.Helper
import files.Progress
import files.Level

/** Represents the main menu of the game, and handles all the content there.
 *  Also contains the pause menu within game.
 *  Extends Helper to get some constants and generally useful methods. */
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
  
  private var currentState = Start
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
  
  /**Checks if level of the given index is unlocked. */
  def isUnlocked(i: Int) = p.available >= i
  
  /**Returns the highscore of the given level index */
  def highscore(i: Int) = {
    val hs = p.highscore(i)
    if (p.highscore(i) == 0) "" 
    else "\nHighscore: " + hs.toString
  }
  
  val progressMapButtons = Vector(first,second,third,fourth,fifth,
                           sixth,seventh,eigth,ninth,tenth,eleventh,twelveth)
  val allMapTexts  = Vector("Entering the Desert",
                            "Zigzag",
                            "Going in Circles",
                            "Many Mountains",
                            "They won't stop",
                            "Happy Times",
                            "Broke.",
                            "Red Ones",
                            "Stuff 1.0",
                            "Stuff 2.0",
                            "Strange Sightings",
                            "???")
  /**map text with its highscore if there is one*/          
  def mapTexts(i: Int) = allMapTexts(i) + highscore(i)
                           
  // saves menu buttons
  val saves = Vector(new Button("Save 1",(8,3 ),(12,3),this),
                     new Button("Save 2",(8,7 ),(12,3),this),
                     new Button("Save 3",(8,11),(12,3),this),
                     new Button("Save 4",(8,15),(12,3),this))
  
  val goBack = new Button("< Back to Main Menu",(1,1),(5,2),this)
  
  def startDraw() = {
    g.textFont(g.font,34)
    g.fill(0,0,0,100)
    g.text("Desert Tower Defence",10.toFloat*sqSize-1,2*sqSize-1)
    g.fill(0)
    g.text("Desert Tower Defence",10.toFloat*sqSize,2*sqSize)
    g.textFont(g.font,24)
    progressButton.draw()
    savesButton.draw()
    helpButton.draw()
    creditsButton.draw()
  }
  
  def progressDraw() = {
    g.textFont(g.font,14)
    goBack.draw()
    for (i <- 0 until progressMapButtons.size) {
      if (isUnlocked(i)) {
        val hs = p.highscore(i)
        if (hs != 0) {
          progressMapButtons(i).draw(0,0,hs/4)
          progressMapButtons(i).progressMapText(mapTexts(i))
        } else {
          progressMapButtons(i).draw(200,100,0)
          progressMapButtons(i).progressMapText(mapTexts(i))
        }
      }
    }
  }
  
  def savesDraw() = {
    g.textFont(g.font,14)
    goBack.draw()
    g.textFont(g.font,24)
    saves.foreach(_.draw())
  }
  
  def textRectangle(title: String, content: String) = {
    g.fill(150,150,200,150)
    g.rect(7*sqSize,sqSize,14*sqSize,18*sqSize)
    g.textFont(g.font,34)
    g.fill(0,0,0,100)
    g.text(title, 12*sqSize-1, (sqSize*7/2)-1)
    g.fill(0)
    g.text(title, 12*sqSize, sqSize*7/2)
    g.textFont(g.font,14)
    g.text("\n\n" + content, 9*sqSize, sqSize*7/2)
  }
  
  def helpDraw() = {
    g.textFont(g.font,14)
    goBack.draw()
    textRectangle("    Help", 
                  "Start a game by going into the 'progression map' from\n" +
                  "the main menu, and clicking a level there.\n\n" +
                  "In this Tower Defence Game, your goal is to prevent\n" +
	                "the enemies, or 'mobs' from getting across the game's\n" + 
	                "arena on their path.\n" +
	                "You do this by eliminating the mobs with various defences, \n" +
	                "which can be bought on towers. Towers can be bought by clicking\n" +
	                "on any empty place in the arena that is not the mobs path.\n" +
	                "Defences damage, range and speed can also be upgraded.\n\n" +
	                "The gray area on the right can be helpful in telling you about\n" + 
	                "whatever your cursor is currently on.\n\n" +
	                "The default save the game loads is the first slot (Save 1).\n\n" +
	                "Information about the game's level and save file formats\n" + 
	                "can be found in the game files in \nlvls/readme.txt and saves/readme.txt.\n\n" +
	                "If any other questions remain or you find a bug, contact:\nvaltteri.kortteisto@gmail.com")
  }
  
  def creditsDraw() = {
    g.textFont(g.font,14)
    goBack.draw()
    textRectangle("  Credits", 
                  "Author Valtteri Kortteisto.\n\n" +
                  "Programming language:  Scala\nIDE:  Eclipse\n" +
	                "Graphical Library:  Processing 3.0\n" + 
	                "Reference:  processing.org/reference/\n" +
	                "Tutorials:  youtube.com/thecodingtrain\n\n" +
	                "Arena Sprites: \nTop-Down Tower Defence Assets by Kenney\n" + 
	                "(opengameart.org/content/tower-defense-300-tilessprites)\n\n" +
	                "Other Sprites: \nrainyislesketch.blogspot.fi/\n" + 
	                "giphy.com/explore/bug-sticker\n" +
	                "ianparberry.wordpress.com/2013/02/01/ant-walk-cycles/\n\n" + 
	                "Sound Library:  Minim Sound Library for Processing\n" +
	                "Reference: code.compartmental.net/minim/javadoc/ddf/minim\n" + 
	                "Individual Sounds From:  freesound.org\n\n" + 
	                "Hospitality: Otto Laitinen")
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
    for (i <- 0 until progressMapButtons.size) {
      if (isUnlocked(i))
        progressMapButtons(i).clicking(i+100)
    }
  }
  
  def savesClick() = {
    goBack.clicking(Start)
    for (i <- 0 until 4) {
      if (saves(i).mouseOn) {
        p.load(i + 1)
        saves(i).clicking(Start)
      }
    }
  }
  
  def helpClick() = {
    goBack.clicking(Start)
  }
  
  def creditsClick() = {
    goBack.clicking(Start)
  }
  
  /** Decides what happen when clicking stuff based 
   *  on the menu's current state. */
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
  
  /** Decides what is drawn based 
   *  on the menu's current state. */
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
  
  // PAUSE MENU ///////////////////////////////////////////////////////
  /** The pause menu is somewhat separate from the intro menu, since it
   *  is an ingame pause menu, but it uses the same buttons as the main menu,
   *  so it is easy to handle here. A new class could have been made for this.*/
  
  private var pauseMenu = false
  def pauseIsOn = pauseMenu
  def togglePause() = {
    if (pauseMenu) {
      pauseMenu = false
    } else {
      pauseMenu = true
    }
  }
  
  // pause menu buttons
  val continueButton = new Button("Continue Game",(6,3 ),(16,3),this)
  val progMapButton  = new Button("Back to Menu", (6,7 ),(16,3),this)
  val restartButton  = new Button("Restart Level",(6,11),(16,3),this)
  val helpButton2    = new Button("Help",         (6,15),(16,3),this)
  
  def drawPauseMenu() = {
    g.textFont(g.font,24)
    continueButton.draw()
    progMapButton.draw()
    restartButton.draw()
    helpButton2.draw()
  }
  
  def clickingPauseMenu() = {
    if (g.mouseButton == leftMouse) {
      if (continueButton.mouseOn)
        g.togglePause()
      else if (restartButton.mouseOn) {
        g.level.reset()
        g.togglePause()
      } else if (progMapButton.mouseOn || helpButton2.mouseOn) {
        g.togglePause()
        this.toggle()
        progMapButton.clicking(Progress)
        helpButton2.clicking(Help)
      }
    }
  }
  //////////////////////////////////////////////////////////////////////
  
}


/**A Button class to simplify the menu. Only used in IntroMenu class.
 * Parameters pos and size and given in multipliers of sqSizes. */
class Button(text: String, pos: (Int,Int), size: (Int,Int), i: IntroMenu) 
  extends Helper(i.g) {
  
  def mouseOn = (mouseX > pos._1 * sqSize) && (mouseY > pos._2 * sqSize) &&
                (mouseX < pos._1 * sqSize + size._1 * sqSize) && 
                (mouseY < pos._2 * sqSize + size._2 * sqSize)
                
  def draw() = {
    if (mouseOn) {
      mouseOnDraw()
    } else {
      g.noStroke()
      g.fill(100,100,150,100)
      g.rect(pos._1*sqSize,pos._2*sqSize,size._1*sqSize,size._2*sqSize)
    }
    g.fill(0)
    g.text(text,pos._1*sqSize + (size._1*sqSize/6),pos._2*sqSize + (size._2*sqSize*3/5))
  }
  
  /** Draw method except with a specified color when the mouse isn't on the button. */
  def draw(col: (Int,Int,Int)) = {
    if (mouseOn) {
      mouseOnDraw()
    } else {
      g.noStroke()
      g.fill(col._1,col._2,col._3,100)
      g.rect(pos._1*sqSize,pos._2*sqSize,size._1*sqSize,size._2*sqSize)
    }
    g.fill(0)
    g.text(text,pos._1*sqSize + (size._1*sqSize/6),pos._2*sqSize + (size._2*sqSize*3/5))
  }
  
  /** The "if (mouseOn)" section of the draw() method for the sake of saving lines. */
  private def mouseOnDraw() = {
    g.noStroke()
    g.fill(0,0,100,100)
    g.rect(pos._1*sqSize - 1,pos._2*sqSize - 1,size._1*sqSize,size._2*sqSize)
    g.fill(250,250,255,150)
    g.rect(pos._1*sqSize,pos._2*sqSize,size._1*sqSize,size._2*sqSize)
    g.fill(0,0,0,100)
    g.text(text,pos._1*sqSize + (size._1*sqSize/6) - 1,pos._2*sqSize + (size._2*sqSize*3/5) - 1)
  }
  
  def clicking(state: Int): Unit = {
    if (mouseOn && g.mouseButton == leftMouse) {
      i.changeState(state)
    }
  }
  
  def progressMapText(t: String) = {
    if (mouseOn) {
      g.text(t,pos._1*sqSize+sqSize,pos._2*sqSize+sqSize*2/3)
    }
  }
  
}

