package file_parser

import java.io._
import arena._
import user_interface._
import objects.Wave
import general.Helper

class Level(file: String, g: Game) {
  
  val waves = scala.collection.mutable.Buffer[Wave]()
  lazy val arena  = new Arena(g,this)
  lazy val path   = arena.path
  lazy val pathStart = arena.start
  
  private var index = 0
  
  val fileReader = new FileReader(file)
  val lineReader = new BufferedReader(fileReader)

  var width  = 0
  var height = 0
  
  try {
    lineReader.readLine() // read the difficulty
    
    var line = lineReader.readLine() // read the size of arena

    // sets the dimensions of the arena
    width  = line.trim.split("x")(0).toInt
    height = line.trim.split("x")(1).toInt
    
    line = lineReader.readLine()

    while( line != "MONEY" ) {
      arena.setRow(index, line.trim.split(" "))
      line = lineReader.readLine()
      index += 1
    }
    
    while( line != "WAVES" ) {
      line = lineReader.readLine()
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