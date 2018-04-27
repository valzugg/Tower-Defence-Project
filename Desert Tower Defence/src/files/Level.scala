/**@author Valtteri Kortteisto */
package files

import java.io._
import processing.core.PApplet
import map._
import user_interface._
import objects.Wave
import objects.Player
import general.Helper
import scala.collection.mutable.Buffer

/** A Level represents a tower defence game with a specific arena and mob waves. 
 *  Levels are loaded from .lvl files, in which the information about the arena
 *  and the waves are given. For information about the level file format, look
 *  at lvls/readme.txt.*/
class Level(file: String, g: Game) {
  // the number in the name of the file
  // (used to determine which level is in question)
  lazy val fileIndex = {
    if (file.exists(_.isDigit))
      file.filter(_.isDigit).toInt
    else
      0
  }
  
  lazy val waves = Buffer[Wave](new Wave(1,0,0,0,0,0,0,this,g))
  private var waveIndex = 0
  def currentWave = this.waves(waveIndex)
  def isComplete = waves.forall(_.isComplete)
  def nextWave()= {
    if (currentWave.isComplete)
      waveIndex += 1
  }
  
  lazy val arena  = new Arena(g,this)
  lazy val player = new Player(g,initMoney)
  lazy val path   = arena.path
  lazy val pathLength = path.size * g.sqSize
  lazy val pathStart  = arena.start
  
  private var initMoney = 10
  private var index = 0
  
  /**Resets this level so that it can be played from scratch again.*/
  def reset() = {
    waveIndex = 0
    arena.resetSquares()
    arena.resetTowers()
    player.reset()
    waves.foreach(_.resetMobs())
  }
  
  override def toString = file
  
  private val fileReader = new FileReader(file)
  private val lineReader = new BufferedReader(fileReader)

  var width  = 0
  var height = 0
  
  // the file reading starts
  try {
    lineReader.readLine() // read the difficulty
    lineReader.readLine() // read MONEY
    var line = lineReader.readLine() // read money amount
    initMoney = line.toInt
    
    lineReader.readLine()        // read ARENA
    line = lineReader.readLine() // read Dimensions
    
    // sets the dimensions of the arena
    width  = line.trim.split("x")(0).toInt
    height = line.trim.split("x")(1).toInt
    
    line = lineReader.readLine() // load up the first line of the arena data

    // create the arena
    while( line != "WAVES" ) {
      val row = line.trim.split(" ")
      require(row.length == width, "The arena's width isnt of the given dimensions.")
      arena.setRow(index, row)
      line = lineReader.readLine()
      index += 1
    }
    require(index == height, "The arena's height isnt of the given dimensions.")
    
    // skip the line before the wave data
    lineReader.readLine()
    line = lineReader.readLine() // load up the first line of the wave data
    
    // create mob waves
    while( line != null && line != "//") { 
      val l = line.split("_").map(_.trim)
      waves += new Wave(l(0).toInt,l(1).toFloat,l(2).toFloat,l(3).toInt,l(4).toInt,l(5).toFloat,l(6).toInt,this,g)
      line = lineReader.readLine()
    }
    
  } catch {
    case r: IllegalArgumentException => {
      println(r.getMessage)
      r.printStackTrace()
    }
    case e: Exception => {
      println( " ======== File doesnt follow expected format ======== " )
      e.printStackTrace()
      g.asInstanceOf[PApplet].exit()
    }
  }
  
  
}