package file_parser

import java.io._
import arena._
import user_interface._
import objects.Wave

class Level(file: String, p: Game) {
  
  val waves = scala.collection.mutable.Buffer[Wave]()
  val arena = new Arena(p)
  lazy val path = arena.path
  lazy val pathStart = arena.start
  
  private var index = 0
  
  val fileReader = new FileReader(file)
  val lineReader = new BufferedReader( fileReader )

  try {
    var line = lineReader.readLine()

    while( line != "WAVES" ) {
      arena.setRow(index, line.trim.split(" "))
      line = lineReader.readLine()
      index += 1
    }
    arena.squares = arena.squaresTransposed.transpose
    
    // skip the line before the wave data
    line = lineReader.readLine()
    line = lineReader.readLine()
    
    // create mob waves
    while( line != null ) { // TODO: Problem
      val l = line.split("_").map(_.trim)
      waves += new Wave(l(0).toInt,l(1).toFloat,l(2).toFloat,l(3).toInt,
                        l(4).toFloat,l(5).toInt,this,p)
      line = lineReader.readLine()
    }
    
  } catch {
    case e:IOException =>
      println( "File doesnt follow expected format." )
  }
  
  
}