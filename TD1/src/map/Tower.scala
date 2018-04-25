/**@author Valtteri Kortteisto */
package map

import objects.Defence

case class Tower(x: Int, y: Int) extends Square {
  val i = 2
  var mouse = false
  val sqSize = 40 // TODO: how to get a reference to helper here?
  val pos = (x*sqSize + sqSize.toFloat/2,y*sqSize + sqSize.toFloat/2)
  val basic = false
  
  var isChosen = false
  
  // not implemented
  private var version = 0
  def upgrade() = {
    version += 1
  }
  
  private var defence: Option[Defence] = None
  
  def removeDef() = defence = None
  def hasDef = !defence.isEmpty
  def getDef = defence.get
  
  /** Adds a defence to this tower if one doesn't already exist.
   *  Returns a boolean indicating if the addition was successful.*/
  def addDefence(d: Defence) = {
    defence = Some(d)
  }
  
  def doStuff() = {
    if (hasDef) defence.get.doStuff()
  }
  
}