package user_interface

import general.Helper

class InfoScreen(m: Menu) extends Helper(m.g) {
  val pos = (aWidth*sqSize + sqSize/2, aHeight*sqSize/4) 
  val sizeX = sqSize * mWidth - sqSize
  val sizeY = sqSize *6

  def write(s1: String,s2: String ,s3: String = "",s4: String = "",s5: String = "") = {
    m.g.textFont(m.g.font,13)
    m.g.fill(0)  
    m.g.text(s1,pos._1 + sqSize/2,pos._2+sqSize)
    m.g.textFont(m.g.font,9)
    m.g.text(s2,pos._1 + sqSize/2,pos._2+sqSize*2)
    m.g.text(s3,pos._1 + sqSize/2,pos._2+sqSize*3)
    m.g.text(s4,pos._1 + sqSize/2,pos._2+sqSize*4)
    m.g.text(s5,pos._1 + sqSize/2,pos._2+sqSize*5)
  }
  
  def doStuff() = {
    m.g.fill(100,100,100,150)
    m.g.rect(pos._1,pos._2,sizeX,sizeY)
    m.infoScreenText()
  }
  
}