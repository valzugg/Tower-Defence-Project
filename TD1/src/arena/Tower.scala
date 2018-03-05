package arena

import objects.Defence

class Tower(x: Int, y: Int) extends Square {
  val i = 2
  var mouse = false
  
  var defence: Option[Defence] = None
}