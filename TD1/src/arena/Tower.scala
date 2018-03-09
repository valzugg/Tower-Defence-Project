package arena

import objects.Defence

class Tower(x: Int, y: Int) extends Square {
  val i = 2
  var mouse = false
  val sqSize = arena.Square.size
  val pos = (x*sqSize + sqSize.toFloat/2,y*sqSize + sqSize.toFloat/2)
  
  private var defence: Option[Defence] = None
  
  def addDefence(d: Defence) = {
    defence = Some(d)
  }
  
  def doStuff() = {
    if (defence.isDefined) defence.get.doStuff()
  }
  
}