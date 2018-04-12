package objects

import user_interface.Game
import file_parser.Level
import processing.core.PApplet
import processing.core.PImage

/** Represents a wave of mobs in a tower defence game.
 *  @param size The number of mobs in this wave (Int)
 *  @param distance The distance between the mobs as square sizes (Int)
 *  @param speed The speed of the mobs (max 2.0) (Float)
 *  @param hp The full hp of the mobs (Int)
 *  @param spriteArr
 *  @param g The game in which this wave belongs (Game)
 */
class Wave(val size: Int, val distance: Float, val speed: Double, 
           hp: Int, mobSize: Double, val spritesIndex: Int, lvl: Level, g: Game) {
  val game = g.asInstanceOf[PApplet]
  val sqSize = arena.Square.size
  
  var sprite: PImage = null
  
  val mobs = Array.ofDim[Mob](size)
  for (m <- 0 until size) {
    mobs(m) = new Mob(this,speed.toFloat,hp,g,m,mobSize.toFloat,lvl)
  }
  
  def deadMobs  = mobs.filter(_.dead)
  def aliveMobs = mobs.filter(!_.dead)
  
  override def toString() = mobs.map(_.toString).foldLeft("")(_+_)
  
  /** Returns true if the wave is complete. */
  def isComplete = aliveMobs.isEmpty
  
  /** Returns the index of the mob in queue.*/
  def inQueAs(m: Mob) = {
    val map = mobs.zipWithIndex.toMap
    map(m)
  }
  
  def doStuff() {
    for (m <- mobs) {
      // the sprite is changed for the animation
      sprite = g.mobSprites(spritesIndex)((m.dist/7).toInt % 
               g.mobSprites(spritesIndex).size)
      m.doStuff(sprite)
    }
    for (m <- mobs) {
      m.hp.doStuff()
    }
  }
  
}