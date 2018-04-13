package arena

import objects.Defence

class Tower(x: Int, y: Int) extends Square {
  val i = 2
  var mouse = false
  val sqSize = 40 // TODO: how to get a reference to helper here?
  val pos = (x*sqSize + sqSize.toFloat/2,y*sqSize + sqSize.toFloat/2)
  val basic = false
  
  private var defence: Option[Defence] = None
  
  /** Adds a defence to this tower if one doesn't already exist.
   *  Returns a boolean indicating if the addition was successful.*/
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