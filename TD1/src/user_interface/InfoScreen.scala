package user_interface

import general.Helper

class InfoScreen(m: Menu) extends Helper(m.g) {
  val pos = (aWidth*sqSize + sqSize/2, aHeight*sqSize/4) 
  val sizeX = sqSize * mWidth - sqSize
  val sizeY = sqSize *6

  
  def doStuff() = {
    m.g.fill(100,100,100,150)
    m.g.rect(pos._1,pos._2,sizeX,sizeY)
  }
  
}