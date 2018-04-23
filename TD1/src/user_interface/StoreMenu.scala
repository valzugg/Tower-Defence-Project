/**@author Valtteri Kortteisto */
package user_interface

import map._
import objects._
import general.Helper
import processing.core.PImage

/** A menu which is created or toggled whenever a tower is clicked. */
class StoreMenu(val t: Tower, s: Store) extends Helper(s.g) {
  private val position = (t.pos._1 + sqSize/2,t.pos._2 - sqSize/2)
  // the position has to be different near the edges
  private val posi = {
    if (t.pos._1 <= (aWidth - 2)*sqSize && t.pos._2 <= (aHeight - 4)*sqSize)
      position
    else if (t.pos._1 <= (aWidth - 1)*sqSize)
      (position._1,position._2 - 2*sqSize)
    else if (t.pos._2 <= (aHeight - 2)*sqSize)
      (position._1 - 2*sqSize,position._2)
    else
      (position._1 - 2*sqSize,position._2 - 2*sqSize)
  }
  
  // has to take into account ice defence with only one upgrade possibility
  def pos = {
    if (t.hasDef && t.getDef.isInstanceOf[IceDefence])
      if (t.pos._2 > (aHeight - 2)*sqSize)
        (posi._1,posi._2 + 2*sqSize)
      else
        posi
    else
      posi
  }
  
  private var toggled = true
  def isToggled = toggled
  
  // contents                    sprite          title          description        stats        price        
  lazy val emptyTower = Vector( (s.g.defences(0),s.crossbowTitle,s.crossbowDesc,s.crossbowStats,s.crossbowPrice),
                                (s.g.defences(3),s.machineTitle,s.machineDesc,s.machineStats,s.machinePrice),
                                (s.g.defences(1),s.iceDefTitle,s.iceDefDesc,s.iceDefStats,s.iceDefPrice) )
  lazy val crossbow   = Vector( (s.g.defences(0),s.crossbowTitle,"Upgrade:\n+ 10 Damage.",(0,0,0),s.upgradeCost),
                                (s.g.defences(0),s.crossbowTitle,"Upgrade:\n+ 20 Range.",(0,0,0),s.upgradeCost),
                                (s.g.defences(0),s.crossbowTitle,"Upgrade:\n+ 1 Speed.",(0,0,0),s.upgradeCost) )
  lazy val iceDef     = Vector( (s.g.defences(1),s.iceDefTitle,"Upgrade:\n+ 20 Range.",(0,0,0),s.upgradeCost) )
  lazy val machineGun = Vector( (s.g.defences(3),s.machineTitle,"Upgrade:\n+ 10 Damage.",(0,0,0),s.upgradeCost),
                                (s.g.defences(3),s.machineTitle,"Upgrade:\n+ 20 Range.",(0,0,0),s.upgradeCost),
                                (s.g.defences(3),s.machineTitle,"Upgrade:\n+ 1 Speed.",(0,0,0),s.upgradeCost) )
  
  def size = currentCells.size
  
  def isUpgradable = t.hasDef
  
  def currentCells = {
    if (t.hasDef) {
      val d = t.getDef.title
      d match {
        case s.crossbowTitle => crossbow
        case s.machineTitle  => machineGun
        case s.iceDefTitle   => iceDef
      }
        
    } else {
      emptyTower
    }
  }
  
  def mouseCell = {
    var res = emptyTower(0)
    (0 until size) foreach { i =>
      if (mouseOn(i))
        res = currentCells(i)
    }
    res
  }
  
  def toggle() = {
    if (toggled) {
      toggled = false
      t.isChosen = false
    } else {
      toggled = true
      t.isChosen = true
    }
  }
  
  /**Gives boolean telling if the mouse is on the given cell of the StoreMenu. */
  def mouseOn(i: Int) = {
    toggled &&
    (mouseX >= pos._1 && mouseY >= pos._2 + i * sqSize) &&
    (mouseX < pos._1 + sqSize && mouseY < pos._2  + (i + 1) * sqSize)
  }
  
  def mouseOn = {
    toggled &&
    (mouseX >= pos._1 && mouseY >= pos._2) &&
    (mouseX < pos._1 + sqSize && mouseY < pos._2  + size * sqSize)
  }
  
  def mouseIndex = {
    if (mouseOn(0)) 0
    else if (mouseOn(1)) 1
    else if (mouseOn(2)) 2
    else 3
  }
  
  def setImg(img: PImage, spot: Int) = {
    s.g.image(img,pos._1,pos._2 + (sqSize* spot),sqSize,sqSize)
  }
  
  def doStuff() = {
    s.g.fill(150,150,150,100)
    s.g.rect(pos._1,pos._2,sqSize,size*sqSize)
    s.g.noFill()
    s.g.rect(t.pos._1 - sqSize/2,t.pos._2 - sqSize/2,sqSize,sqSize)
    
    if (mouseOn) {
      s.g.noStroke
      s.g.fill(150,150,150)
      s.g.rect(pos._1 + 1,pos._2 + mouseIndex*sqSize + 1,sqSize - 1,sqSize - 1)
      s.g.stroke(0)
    }
    
    (0 until size) foreach { i =>
      setImg(currentCells(i)._1,i)
      if (isUpgradable) {
        s.g.fill(0,255,0)
        if (currentCells.size == 1) {
          s.g.text("R",pos._1+sqSize/2,pos._2 + sqSize*3/4)
        } else {
          s.g.fill(255,0,0)
          s.g.text("D",pos._1+sqSize/2,pos._2+sqSize*3/4)
          s.g.fill(0,255,0)
          s.g.text("R",pos._1+sqSize/2,pos._2 + (sqSize*7/4))
          s.g.fill(0,0,255)
          s.g.text("S",pos._1+sqSize/2,pos._2 + (sqSize*11/4))
        }
      }
    }
  }
  
  def whenClicked() = {
    (0 until size) foreach { i =>
      if (this.mouseOn(i)) {
        if (this.isUpgradable) {
          // ice defence can only get range upgrades
          if (this.t.getDef.isInstanceOf[IceDefence]) {
            if (s.buyUpgrade(t,'r')) this.toggle()
          } else {
            i match {
              case 0 => if (s.buyUpgrade(t,'d')) this.toggle()
              case 1 => if (s.buyUpgrade(t,'r')) this.toggle()
              case 2 => if (s.buyUpgrade(t,'s')) this.toggle()
            }
          }
        } else if (s.buyDef(this.t,s.getDefence(this.t,i)))
          this.toggle()
      }
    }
  }
  
  
}