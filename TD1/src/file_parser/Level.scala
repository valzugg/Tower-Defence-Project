package file_parser

import java.io._
import arena._
import user_interface._

class Level(file: String, p: Game) {
  
  val arena = new Arena(p)
  private var index = 0
  
  val fileReader = new FileReader(file)
  val lineReader = new BufferedReader( fileReader )

  try {
    var line = lineReader.readLine()

    while( line != "MOBS" ) {
      arena.setRow(index, line.trim.split(" "))
      line = lineReader.readLine()
      index += 1
    }
    arena.squares = arena.squaresTransposed.transpose
    
    while( line != null ) {
      //create mob waves
      line = lineReader.readLine()
      index += 1
    }
    
  } catch {
    case e:IOException =>
      println( "File is corrupt" )
  }
  
  
}