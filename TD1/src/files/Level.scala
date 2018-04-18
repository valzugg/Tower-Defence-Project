package files

import java.io._
import processing.core.PApplet
import map._
import user_interface._
import objects.Wave
import objects.Player
import general.Helper

class Level(file: String, g: Game) {
  
  val waves = scala.collection.mutable.Buffer[Wave]()
  lazy val arena  = new Arena(g,this)
  lazy val player = new Player(g,initMoney)
  lazy val path   = arena.path
  lazy val pathStart = arena.start
  
  private var initMoney = 10
  private var index = 0
  
  val fileReader = new FileReader(file)
  val lineReader = new BufferedReader(fileReader)

  var width  = 0
  var height = 0
  
  try {
    lineReader.readLine() // read the difficulty
    lineReader.readLine() // read MONEY
    var line = lineReader.readLine()
    initMoney = line.toInt
    
    while( line != "ARENA" ) {
      line = lineReader.readLine()
    }
    
    line = lineReader.readLine()
    
    // sets the dimensions of the arena
    width  = line.trim.split("x")(0).toInt
    height = line.trim.split("x")(1).toInt
    
    line = lineReader.readLine()

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
    line = lineReader.readLine()
    
    // create mob waves
    while( line != null ) { 
      val l = line.split("_").map(_.trim)
      waves += new Wave(l(0).toInt,l(1).toFloat,l(2).toFloat,l(3).toInt,
                        l(4).toFloat,l(5).toInt,this,g)
      line = lineReader.readLine()
    }
    
  } catch {
    case r: IllegalArgumentException => {
      println(r.getMessage)
    }
    case e: Exception => {
      println( " ======== File doesnt follow expected format ======== " )
      g.asInstanceOf[PApplet].exit()
    }
  }
  
  
}