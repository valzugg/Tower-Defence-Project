/**@author Valtteri Kortteisto */
package user_interface

import general.Helper

class InfoScreen(m: Menu) extends Helper(m.g) {
  val pos = (aWidth*sqSize + sqSize/2, 3*sqSize) 
  val sizeX = sqSize * mWidth - sqSize
  val sizeY = sqSize * 7

  val shadowX = pos._1 + sqSize/2 + 1
  val shadowY = pos._2 + sqSize + 1
  val textX   = pos._1 + sqSize/2
  val textY   = pos._2 + sqSize
  
  def title(t: String) = {
    m.g.textFont(m.g.font,14)
    m.g.fill(0)  
    m.g.text(t,shadowX,shadowY)
    m.g.fill(255) 
    m.g.text(t,textX,textY)
  }
  
  // write method for multiple lines
  def description(t: String) = {
    m.g.textFont(m.g.font,11)
    m.g.fill(0)  
    m.g.text("\n\n\n" + t,shadowX-sqSize/8,shadowY)
    m.g.fill(255) 
    m.g.text("\n\n\n" + t,textX-sqSize/8,textY)
  }
  
  def stats(damage: Int, range: Int, speed: Int) = {
    m.g.textFont(m.g.font,13)
    if (range != 0) {
      m.g.fill(0)
      m.g.text("\n\n\n\n\n\n\nDamage: " + damage + "\nRange: " + 
               range + "\nSpeed: " + speed,shadowX,shadowY)
      m.g.fill(0,255,0) 
      m.g.text("\n\n\n\n\n\n\nDamage: " + damage + "\nRange: " + 
               range + "\nSpeed: " + speed,textX,textY)
    }
  }
  
  def cost(amount: Int) = {
    val c = { // text color depending on if the player can afford the price
      if (m.g.level.player.canAfford(amount)) 
        (255,255,0)
      else
        (255,0,0)
    }
    m.g.textFont(m.g.font,13)
    m.g.fill(0)
    m.g.text("\n\n\n\n\n\n\n\n\n\n\nCost: " + amount,shadowX,shadowY)
    m.g.fill(c._1,c._2,c._3)
    m.g.text("\n\n\n\n\n\n\n\n\n\n\nCost: " + amount,textX,textY)
  }
  
  def doStuff() = {
    m.g.fill(120,120,120,150)
    m.g.rect(pos._1,pos._2,sizeX,sizeY)
    m.infoScreenText()
  }
  
}