package user_interface

import general.Helper

class InfoScreen(m: Menu) extends Helper(m.g) {
  val pos = (aWidth*sqSize + sqSize/2, aHeight*sqSize/4) 
  val sizeX = sqSize * mWidth - sqSize
  val sizeY = sqSize *6

  // write method for multiple lines
  def write(title: String, content: Vector[String]) = {
    m.g.textFont(m.g.font,13)
    m.g.fill(0)  
    m.g.text(title,pos._1 + sqSize/2,pos._2+sqSize)
    m.g.textFont(m.g.font,10)
    
    for (i <- 0 until content.size) {
       m.g.text(content(i),pos._1 + sqSize/2,pos._2 + 2*sqSize + sqSize*i/2)
    }
    
  }
  
  // write method for two lines
  def write(title: String, content: String) = {
    m.g.textFont(m.g.font,13)
    m.g.fill(0)  
    m.g.text(title,pos._1 + sqSize/2,pos._2+sqSize)
    m.g.textFont(m.g.font,10)
    
    m.g.text(content,pos._1 + sqSize/2,pos._2+sqSize + sqSize)
    
  }
  
  def doStuff() = {
    m.g.fill(120,120,120,150)
    m.g.rect(pos._1,pos._2,sizeX,sizeY)
    m.infoScreenText()
  }
  
}