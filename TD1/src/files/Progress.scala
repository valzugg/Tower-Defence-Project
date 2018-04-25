/**@author Valtteri Kortteisto */
package files

import java.io._

class Progress(i: Int = 0) {
  private var saveNumber = 1 // 1 ... 4
  private var unlocked = i   // 0 ... 12
  private def nowUnlocked = unlocked
  // indicates whether or not the player has completed all the levels
  def hasFinished = nowUnlocked > lastLevelIndex + 1
  def available = if (hasFinished) unlocked - 1 else unlocked 
  def unlock() = unlocked += 1
  private def unlock(n: Int) = unlocked = n
  
  val lastLevelIndex = 11
  
  private val highscores = Array.fill(lastLevelIndex+1)(0)
  def highscore(i: Int)  = highscores(i)
  def setHighscore(i: Int, score: Int) = {
    if (score > highscores(i))
      highscores(i) = score
  }
  
  /** Saving progress onto the given file number. */
  def save(n: Int) = {
    val pw = new PrintWriter(new File("saves/" + n + ".prog"))
    pw.write(nowUnlocked.toString)
    for (i <- 0 until available) pw.write("\n" + highscore(i))
    pw.close()
  }
  
  /** Loading a save of a given file number. */
  def load(n: Int) = {
    require(n >= 1 && n <= 4,"No such save exists.")
    saveNumber = n
    val file = "saves/" + n + ".prog"
    
    val fileReader = new FileReader(file)
    val lineReader = new BufferedReader(fileReader)
    var line: String = null
    var levels: Int  = 0
    
    try {
      line = lineReader.readLine().trim()
      levels = line.toInt
      require(levels >= 0 && levels <= lastLevelIndex + 1,
              "No such level exists" + " (" + line + ") " + ",save corrupt.")
      // unlocks correct levels
      unlock(levels)
      // sets highscores
      for (i <- 0 until available) {
        highscores(i) = lineReader.readLine().trim().toInt
      }
      if (!hasFinished)
        for (i <- available to lastLevelIndex) highscores(i) = 0
    } catch {
      case n: NullPointerException => {
        println("A highscore missing, save corrupt.")
        n.printStackTrace()
      }
      case f: NumberFormatException => {
        println("Invalid characters used, save corrupt.")
        f.printStackTrace()
      }
      case r: IllegalArgumentException => {
        println(r.getMessage)
        r.printStackTrace()
      }
    }
  }
  
}