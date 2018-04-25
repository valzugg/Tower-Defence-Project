/**@author Valtteri Kortteisto */
package user_interface

import map._
import objects._
import general.Helper
import processing.core.PImage

class Store(m: Menu) extends Helper(m.g) {
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
  
  
  def buyUpgrade(t: Tower, i: Char): Boolean = {
    val p = m.g.player
    if (p.canAfford(upgradeCost)) {
      p.pay(upgradeCost)
      g.sounds.play(g.sounds.crossbow)
      val d = t.getDef
      i match {
        case 'r' => t.addDefence(upgradeDef(d,t,i)) // improve range
        case 'd' => t.addDefence(upgradeDef(d,t,i)) // improve damage
        case 's' => t.addDefence(upgradeDef(d,t,i)) // improve speed
      }
      true
    } else {
      false
    }
  }
  
  def upgradeDef(d: Defence, t: Tower, i: Char) = {
    if (d.isInstanceOf[GunDefence])
      machineDef(t,upgradedStats(i,(d.range,d.damage.toInt,d.speed)))
    else if (d.isInstanceOf[BasicDefence])
      crossbowDef(t,upgradedStats(i,(d.range,d.damage.toInt,d.speed)))
    else if (d.isInstanceOf[IceDefence])
      iceDef(t,upgradedStats(i,(d.range,d.damage.toInt,d.speed)))
    else
      fireDef(t,upgradedStats(i,(d.range,d.damage.toInt,d.speed)))
  }
  
  //the initial stats    r   d  s
  val crossbowStats   = (110,50,5)
  val machineStats    = (90, 50,12)
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
  
  // store items
  def crossbowDef(t: Tower, s: (Int,Int,Int)) = 
    new BasicDefence(t,s._1,s._2,s._3,m.g)
  def machineDef(t: Tower, s: (Int,Int,Int))  = 
    new GunDefence(t,s._1,s._2,s._3,m.g)
  def iceDef(t: Tower, s: (Int,Int,Int))      = 
    new IceDefence  (t,s._1,s._2,s._3,m.g,0.5.toFloat)
  def fireDef(t: Tower, s: (Int,Int,Int))     = 
    new FireDefence (t,s._1,s._2,s._3,m.g)
  
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