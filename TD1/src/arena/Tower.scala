package arena

import objects.Defence

class Tower(x: Int, y: Int) extends Square {
  val i = 2
  var mouse = false
  val sqSize = arena.Square.size
  val pos = (x*sqSize + sqSize.toFloat/2,y*sqSize + sqSize.toFloat/2)
  val basic = false
  
  private var defence: Option[Defence] = None
  
  /** Adds a defence to this tower if one doesn't already exist.
   *  Returns a boolean indicating if the addition was succeseful.*/
  def addDefence(d: Defence) = {
    if (defence.isEmpty) {
      defence = Some(d)
      true
    } else {
      false
    }
  }
  
  def doStuff() = {
    if (defence.isDefined) defence.get.doStuff()
  }
  
}