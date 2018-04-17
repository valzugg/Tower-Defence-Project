package file_parser

import java.io._
import arena._
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
      arena.setRow(index, line.trim.split(" "))
      line = lineReader.readLine()
      index += 1
    }
    
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
    case e:IOException =>
      println( "File doesnt follow expected format." )
  }
  
  
}