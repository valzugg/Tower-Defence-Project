package user_interface

import map._
import objects._
import general.Helper
import processing.core.PImage

/** A menu which is created whenever a tower is clicked. */
class StoreMenu(val t: Tower, s: Store) extends Helper(s.g) {
  val pos = (t.pos._1 + sqSize/2,t.pos._2 - sqSize/2)
  private var toggled = true
  def isToggled = toggled
  
  // contents                    sprite          title          description        stats        price        
  lazy val emptyTower = Vector( (s.g.defences(0),s.crossbowTitle,s.crossbowDesc,s.crossbowStats,s.crossbowPrice),
                                (s.g.defences(3),s.machineTitle,s.machineDesc,s.machineStats,s.machinePrice),
                                (s.g.defences(1),s.iceDefTitle,s.iceDefDesc,s.iceDefStats,s.iceDefPrice) )
  def crossbow   = Vector( (s.g.defences(0),s.crossbowTitle,"+ 10 Damage.",(0,0,0),s.upgradeCost),
                                (s.g.defences(0),s.crossbowTitle,"+ 20 Range.",(0,0,0),s.upgradeCost),
                                (s.g.defences(0),s.crossbowTitle,"+ 1 Speed.",(0,0,0),s.upgradeCost) )
  def iceDef     = Vector( (s.g.defences(1),s.crossbowTitle,"+ 20 Range.",(0,0,0),s.upgradeCost) )
  def machineGun = Vector( (s.g.defences(3),s.machineTitle,"+ 10 Damage.",(0,0,0),s.upgradeCost),
                                (s.g.defences(3),s.machineTitle,"+ 10 Range.",(0,0,0),s.upgradeCost),
                                (s.g.defences(3),s.machineTitle,"+ 1 Speed.",(0,0,0),s.upgradeCost) )
  
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
    if (toggled)
      toggled = false
    else
      toggled = true
  }
  
  /**Gives boolean telling if the mouse is on the given cell of the StoreMenu. */
  def mouseOn(n: Int) = {
    toggled &&
    (mouseX >= pos._1 && mouseY >= pos._2 + n * sqSize) &&
    (mouseX < pos._1 + sqSize && mouseY < pos._2  + (n + 1) * sqSize)
  }
  
  def mouseOn = {
    toggled &&
    (mouseX >= pos._1 && mouseY >= pos._2) &&
    (mouseX < pos._1 + sqSize && mouseY < pos._2  + size * sqSize)
  }
  
  
  
  def setImg(img: PImage, spot: Int) = {
    s.g.image(img,pos._1,pos._2 + (sqSize* spot),sqSize,sqSize)
  }
  
  def doStuff() = {
    s.g.fill(150,150,150,100)
    s.g.rect(pos._1,pos._2,sqSize,size*sqSize)
    (0 until size) foreach { i =>
      setImg(currentCells(i)._1,i)
    }
  }
  
  def whenClicked() = {
    (0 until size) foreach { i =>
      if (this.mouseOn(i)) {
        if (this.isUpgradable) {
          i match {
            case 0 => if (s.buyUpgrade(t,'d')) this.toggle()
            case 1 => if (s.buyUpgrade(t,'r')) this.toggle()
            case 2 => if (s.buyUpgrade(t,'s')) this.toggle()
          }
        } else if (s.buyDef(this.t,s.getDefence(this.t,i)))
          this.toggle()
      }
    }
  }
  
  
}


class Store(m: Menu) extends Helper(m.g) {
  val g = m.g      //game
  val towerCost = 5
  val upgradeCost = 5
  
  val crossbowTitle = "Crossbow\nDefence"
  val crossbowDesc  = "A Basic Defence."
  val crossbowPrice = 5
  val machineTitle  = "Machinegun\nDefence"
  val machineDesc   = "A more effective\nBasic Defence."
  val machinePrice  = 10
  val iceDefTitle   = "Ice Defence"
  val iceDefDesc    = "Slows the mobs\nwithin range.\nDoes no damage."
  val iceDefPrice   = 10
  
  def getDefence(t: Tower, i: Int) = {
    i match {
      case 0 => crossbowDef(t,crossbowStats)
      case 1 => machineDef(t,machineStats)
      case 2 => iceDef(t,iceDefStats)
      case 3 => fireDef(t,iceDefStats)
    }
  }
  
  // TODO: only uses crossbow defs for now
  def buyUpgrade(t: Tower, i: Char): Boolean = {
    val p = m.g.player
    if (p.canAfford(upgradeCost)) {
      p.pay(upgradeCost)
      g.sounds.play(g.sounds.crossbow)
      val d = t.getDef
      i match {
        case 'r' => t.addDefence(crossbowDef(t,
                    upgradedStats(i,(d.range,d.damage.toInt,d.speed)))) // improve range
        case 'd' => t.addDefence(crossbowDef(t,
                    upgradedStats(i,(d.range,d.damage.toInt,d.speed)))) // improve damage
        case 's' => t.addDefence(crossbowDef(t,
                    upgradedStats(i,(d.range,d.damage.toInt,d.speed)))) // improve speed
      }
      true
    } else {
      false
    }
  }
  
  //the initial stats    r   d  s
  val crossbowStats   = (110,60,3)
  val machineStats    = (90, 50,9)
  val iceDefStats     = (150,0, 0)
  
  /**@param i symbol of the thing that is upgraded */
  def upgradedStats(i: Char, s: (Int,Int,Int)) = {
    require(i == 'r' || i == 'd' || i == 's')
    i match {
      case 'r' => (s._1 + 20,s._2,s._3) // improve range
      case 'd' => (s._1,s._2 + 10,s._3) // improve damage
      case 's' => (s._1,s._2,s._3 + 1 ) // improve speed
    }
  }
  
  // store items // TODO: what if machine gun is its own class?
  def crossbowDef(t: Tower, s: (Int,Int,Int)) = 
    new BasicDefence(t,s._1,s._2,
                     s._3,crossbowPrice,m.g,0,0,
                     crossbowTitle,
                     crossbowDesc)
  def machineDef(t: Tower, s: (Int,Int,Int))  = 
    new BasicDefence(t,s._1,s._2,
                     s._3,machinePrice,m.g,3,6,
                     machineTitle,
                     machineDesc)
  def iceDef(t: Tower, s: (Int,Int,Int))      = 
    new IceDefence  (t,s._1,s._2,
                     s._3,iceDefPrice,m.g,0.5.toFloat,1,5,
                     iceDefTitle,
                     iceDefDesc)
  def fireDef(t: Tower, s: (Int,Int,Int))     = 
    new FireDefence (t,s._1,s._2,s._3,5,m.g,2,0, "","")
  
  def buyDef(t: Tower, d: Defence): Boolean = {
    if (player.money >= d.cost) {
      t.addDefence(d)
      g.sounds.play(g.sounds.crossbow)
      player.money -= d.cost
      true
    } else {
      false
    }
  }

  def buyTower(x: Int, y: Int) = {
    if (player.money > 4)
      if (arena.setTower(x,y)) {
        g.sounds.play(g.sounds.build)
        player.money -= towerCost
      }
  }
  
}