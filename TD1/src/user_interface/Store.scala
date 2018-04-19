package user_interface

import map._
import objects._
import general.Helper
import processing.core.PImage

/** A menu which is created whenever a tower is clicked. */
class StoreMenu(val t: Tower, s: Store) extends Helper(s.g) {
  val pos = (t.pos._1 + sqSize/2,t.pos._2 - sqSize/2)
  var toggled = true
  
  val size = 2
  
  def toggle() = {
    if (toggled)
      toggled = false
    else
      toggled = true
  }
  
  def mouseOn(n: Int) = {
    toggled &&
    (mouseX > pos._1 && mouseY > pos._2 + (n - 1) * sqSize) &&
    (mouseX < pos._1 + sqSize && mouseY < pos._2  + n * sqSize)
  }
  
  def mouseOn = {
    toggled &&
    (mouseX > pos._1 && mouseY > pos._2) &&
    (mouseX < pos._1 + sqSize && mouseY < pos._2  + size * sqSize)
  }
  
  def setImg(img: PImage, spot: Int) = {
    s.g.image(img,pos._1,pos._2 + (sqSize* spot),sqSize,sqSize)
  }
  
  def doStuff() = {
    s.g.fill(150,150,150,100)
    s.g.rect(pos._1,pos._2,sqSize,size*sqSize)
    setImg(s.g.defences(0),0)
    setImg(s.g.defences(1),1)
  }
  
  def whenClicked() = {
    if (this.mouseOn(1)) {
      if (s.buyDef(this.t,s.basicDef(this.t)))
        this.toggle()
    } else if (this.mouseOn(2)) {
      if (s.buyDef(this.t,s.iceDef(this.t)))
        this.toggle()
    }
  }
  
  
}


class Store(m: Menu) extends Helper(m.g) {
  val g = m.g      //game
  
  def basicDef(t: Tower) = new BasicDefence(t,110,60,3,5,m.g)
  def iceDef(t: Tower)   = new IceDefence(t,150,20,2,5,m.g,0.5.toFloat)
  def fireDef   = new FireDefence(m.arena.towers(mSqX)(mSqY),100,10,3,5,m.g)
  
  def buyDef(t: Tower, d: Defence): Boolean = {
    if (player.money >= d.cost)
      if (t.addDefence(d)) {
        g.sounds.play(g.sounds.crossbow)
        player.money -= d.cost
        true
      } else {
        false
      }
    else 
      false
  }

  def buyTower(x: Int, y: Int) = {
    if (player.money > 4)
      if (arena.setTower(x,y)) {
        g.sounds.play(g.sounds.build)
        player.money -= 5
      }
  }
  
}