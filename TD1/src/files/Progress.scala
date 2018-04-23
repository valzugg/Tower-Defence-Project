package files

import java.io._

class Progress(i: Int = 0) {
  private var saveNumber = 1 // 1 ... 4
  private var unlocked = i   // 0 ... 11
  def available = unlocked
  def unlock() = unlocked += 1
  def unlock(n: Int) = unlocked = n
  
  private val lastLevelIndex = 11
  
  private val highscores = Array.fill(lastLevelIndex+1)(0)
  def highscore(i: Int)  = highscores(i)
  def setHighscore(i: Int, score: Int) = {
    if (score > highscores(i))
      highscores(i) = score
  }
  
  /** Saving progress onto the given file number. */
  def save(n: Int) = {
    val file = new File("saves/" + n + ".prog")
    val pw = new PrintWriter(file)
    pw.write(this.available.toString)
    for (i <- 0 until available) {
      pw.write("\n" + highscore(i))
    }
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
      require(levels >= 0 && levels <= lastLevelIndex,
              "No such level exists" + " (" + line + ") " + ", save corrupt.")
      unlock(line.toInt)
      for (i <- 0 until available) {
        line = lineReader.readLine().trim()
        highscores(i) = line.toInt
      }
      for (i <- available to lastLevelIndex) {
        highscores(i) = 0
      }
    } catch {
      case r: IllegalArgumentException => {
        println(r.getMessage)
      }
    }
    levels
  }
  
}