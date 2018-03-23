package objects

import arena._
import user_interface._
import processing.core.PImage
import processing.core.PApplet

/** Represents an enemy in a tower defence game 
 *  
 */
class Mob(w: Wave ,var speed: Float, hitpoints: Int, g: Game, val i: Int) {
  val game = g.asInstanceOf[PApplet]
  val hp = new HealthBar(this,hitpoints)
  val sqSize  = Square.size
  val halfPi  = (scala.math.Pi.toFloat/2)
  val moneyValue = ((hitpoints/10)*speed).toInt
  
  val r = scala.util.Random.nextFloat() 
  
  //keep track of the mob's location
  var x = (-sqSize * (i + 1) * w.distance) - (r * w.distance * 20)
  var y = g.startPath*sqSize.toFloat
  
  def pos = (x + sqSize/2,y + sqSize/2)
  
  //keeps track of the mob's current direction
  var dir = (0,0)
  
  //vector directions defined for ease of use
  val right = (1, 0)
  val left  = (-1,0)
  val down  = (0, 1)
  val up    = (0,-1)
  
  //returns the index of the mob in the wave
  override def toString() = i.toString
  
  /** Checks if mobs hitpoints are up. */
  def dead = hp.amount < 1

  /** Removes this mob from the wave it belongs if it is dead. */
  def kill() = {
    g.player.money += moneyValue
  }
  
  // assists hp in knowing when to display itself
  var hasBeenDamaged = false
  
  /** Removes given amount of hitpoints. */
  def damage(by: Double) = {
    this.hp.damage(by)
    if (dead) { this.kill() }
    hasBeenDamaged = true
  }
  
  /** Returns the direction to the right of the given direction*/
  private def rightOf(d: (Int,Int)) = {
    if (d._2==0) (d._2,d._1) else (-d._2,d._1)
  }
  
  /** Returns the direction to the left of the given direction*/
  private def leftOf(d: (Int,Int)) = {
    if (d._1==0) (d._2,d._1) else (d._2,-d._1)
  }
  
  
  //the mob's current square
  def square = {
    g.arena.squares(x.toInt/sqSize)(y.toInt/sqSize)
  }
  
  //a square of given arena coordinates relative to the mob
  private def nextSq(d: (Int, Int)) = {
    g.arena.squares((x.toInt/sqSize) + d._1)((y.toInt/sqSize) + d._2)
  } 
  
  
  /** The core of the act() method's algorithm.
   *  Checks the next square in the given direction and 
   *  moves the mob in the direction where possible.
   *  Note that it is assumed that a mob cannot be in a situation 
   *  where it would have to go back to where it came from.
   *  @param d The direction in which the mob is moving as a vector.*/
  private def checkDir(d: (Int, Int)) = {
    nextSq(d) match {                  //checks the square in the front
      case Path(_,_) => move(d)
      case _ => {
        nextSq(rightOf(d)) match {     //checks the square on the right
          case Path(_,_) => move(rightOf(d))
          case _         => move(leftOf(d)) } }
    } 
  }
  
  /** Moves the mob in the given direction and sets it's dir variable correct*/
  private def move(d: (Int, Int)) = {
    x += d._1*speed
    y += d._2*speed
    dir = (d._1,d._2)
  }
  
  
  private def moveToStart() = {
      this.x = -sqSize
      this.y = sqSize*g.startPath
  }
  
  //TODO: Algoritmi joka tekee reitin valmiiksi, tai tarkistaa eri tavalla, ongelma suuremilla nopeuksilla
  /**The algorithm by which the mobs finds its way on the path*/
  private def act() = {
    if (x >= sqSize*(g.arena.sizeX)) moveToStart()
    if (x < sqSize*(g.arena.sizeX - 1)) {
      rotate()
      if (x < 0) { //when at start
        move(right)
      } else if (if (dir != left) {    
          x.toInt % 40 == 20 || y.toInt % 40 == 0 
        } else {     //checks only at every square
          x.toInt % 40 ==  0
        }) { 
        checkDir(dir)
      } else {
        move(dir)
      }
    } else {
      move(dir) // in the case the mob finishes the path
    }
  }
 
  /** Rotates the mob to the direction it is moving. 
   *  See act()*/
  private def rotate() = {
    dir match {
      case this.right => 
      case this.down  => game.rotate(halfPi)
      case this.left  => game.rotate(2*halfPi)
      case _          => game.rotate(3*halfPi)
    }
  }
  
  /** The concrete stuff that is sent to be done at the Game class.
   *  @param img The sprite of the mob as a PImage already loaded.*/
  def doStuff(img: PImage) = {
    if (!dead) {
      game.pushMatrix()
      game.translate(sqSize/2,sqSize/2)
      game.translate(this.x,this.y) // the 'axis' of the mob is being moved
      this.act()
      game.image(img,-sqSize/4,-sqSize/4,sqSize/2,sqSize/2)
      game.popMatrix()
    }
  }

}


/** Represents the health bar of a mob.
 *  A mob's health bar follows it, going above it and showing in
 *  green and red how much health a mob has. 
 *  If health is up, mob dies. The health bar should only show up
 *  after the mob has been damaged.
 * 	@param m The mob to which the health bar belongs
 *  @param fullAmount the original amount of health the mob spawns with.*/
class HealthBar(val m: Mob, fullAmount: Double) {
  // keep track of the mob's coordinates
  def x = m.x + 2
  def y = m.y - 5
  
  // the size of the health bar
  val xSize = 35
  val ySize = 3
  
  var amount = fullAmount
  
  def damage(by: Double) = { amount -= by }
  
  /** Drawing of the health bar when called in Game's draw().
   *  Drawn only if mob is alive.*/
  def doStuff() = {
    if (!m.dead && m.hasBeenDamaged) {
      m.game.noStroke()
      m.game.fill(255,0,0)
      m.game.rect(x,y,xSize,ySize)
      m.game.noStroke()
      m.game.fill(0,255,0)
      m.game.rect(x,y,(amount/fullAmount).toFloat*xSize,ySize)
    }
  }
  
}