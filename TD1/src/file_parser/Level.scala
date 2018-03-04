package file_parser

import java.io._
import arena._

class Level(file: String) {
  
  val arena = new Arena
  var index = 0
  
  val fileReader = new FileReader(file)
  val lineReader = new BufferedReader( fileReader )

  try {
    var line = lineReader.readLine()

    while( line != null ) {
      arena.setRow(index, line.split(" "))
      line = lineReader.readLine()
      index += 1
    }
  } catch {
    case e:IOException =>
      println( "File is corrupt" )
  }
  
  
}