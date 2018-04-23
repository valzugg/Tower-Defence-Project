package files

import java.io._
import scala.io.Source

class Progress(i: Int = 0) {
  private var saveNumber = 1 // 1 ... 4
  private var unlocked = i   // 0 ... 11
  def available = unlocked
  def unlock() = unlocked += 1
  
  def save(n: Int) = {
    val file = new File("saves/" + n + ".prog")
    val bw = new PrintWriter(new FileWriter(file))
    bw.write(this.available)
    bw.close()
  }
  
  def load(n: Int) = {
    require(n >= 1 && n <= 4,"No such save exists.")
    saveNumber = n
    val file = "saves/" + n + ".prog"
    
    val fileReader = new FileReader(file)
    val lineReader = new BufferedReader(fileReader)
    var line: String = null
    
    try {
      line = lineReader.readLine().trim()
      require(line.toInt >= 0 && line.toInt <= 11,
              "No such level exists, save corrupt.")
      
    } catch {
      case r: IllegalArgumentException => {
        println(r.getMessage)
      }
      case e: Exception => {
        println( " ======== Save file is corrupt ======== " )
      }
    }
    line.toInt
  }
  
}