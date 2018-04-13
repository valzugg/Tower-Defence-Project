package file_parser

import java.io._
import arena._
import user_interface._
import objects.Wave
import general.Helper

class Level(file: String, g: Game) {
  
  val waves = scala.collection.mutable.Buffer[Wave]()
  val arena  = new Arena(g)
  lazy val path = arena.path
  lazy val pathStart = arena.start
  
  private var index = 0
  
  val fileReader = new FileReader(file)
  val lineReader = new BufferedReader( fileReader )

//  var width  = 0
//  var height = 0
  
  try {
    var line = lineReader.readLine()

//    // sets the width of the arena
//    width = line.trim.split(" ").length
    
    while( line != "WAVES" ) {
      arena.setRow(index, line.trim.split(" "))
      line = lineReader.readLine()
      index += 1
    }
    
    // to be able to access the squares in 
    // a (horizontal,vertical) - axis, needs to be transposed
    arena.squares = arena.squaresTransposed.transpose
    
//    // sets the height of the arena
//    height = index
    
    // skip the line before the wave data
    line = lineReader.readLine()
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