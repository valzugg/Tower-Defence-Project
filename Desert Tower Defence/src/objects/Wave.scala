/**@author Valtteri Kortteisto */
package objects

import user_interface.Game
import files.Level
import processing.core.PApplet
import processing.core.PImage
import general.Helper

/** Represents a wave of mobs in a tower defence game.
 *  Waves are created in the initialization of each level.
 *  Extends Helper to get some constants and generally useful methods.
 *  @param size The number of mobs in this wave (Int)
 *  @param distance The distance between the mobs as square sizes (Int)
 *  @param mobValue The amount of money the player gets from killing this kind of mob
 *  @param mobSize The size of the sprite of the mob (0.20 to about 0.5 is normal)
 *  @param spritesIndex The index of the sprites of this waves mobs in the 
 *  'mobSprites' array in Game
 */
class Wave(val size: Int, val distance: Float, val speed: Double,
           mobValue: Int, hp: Int, mobSize: Double, val spritesIndex: Int, lvl: Level, 
           g: Game) extends Helper(g) {
  val game = g.asInstanceOf[PApplet]
  
  // this is set in the doStuff() method
  // has to be null to begin with, because levels 
  // are created before the images are loaded in the Game
  private var sprite: PImage = null 
  
  val mobs = Array.ofDim[Mob](size)
  
  /** Resets the mobs for a new game. */
  def resetMobs() = {
    for (m <- 0 until size)
      mobs(m) = new Mob(this,speed.toFloat,mobValue,hp,g,m,mobSize.toFloat,lvl)
  }
  
  resetMobs() // resets the wave by default
  
  def deadMobs  = mobs.filter(_.dead)
  def aliveMobs = mobs.filter(!_.dead)
  
  override def toString() = mobs.map(_.toString).foldLeft("")(_+_)
  
  /** Returns true if the wave is complete. */
  def isComplete = aliveMobs.isEmpty
  
  def doStuff() {
    for (m <- mobs) {
      // the sprite is changed for the animation
      sprite = g.mobSprites(spritesIndex)((m.distance/7).toInt % 
               g.mobSprites(spritesIndex).size)
      m.doStuff(sprite)
      m.hp.doStuff()
    }
  }
  
}