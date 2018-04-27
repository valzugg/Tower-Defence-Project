/**@author Valtteri Kortteisto */
package objects

import user_interface.Game

/** Represents simple information about the players state in a game.
 *  Specifically the money and the hitpoints the player has in a level. */
class Player(g: Game, initMoney: Int) {
  var money = initMoney
  var hp    = 100
  
  /** Resets the players money and hp.
   *  called whenever a level is reset. */
  def reset() = {
    money = initMoney
    hp    = 100
  }
    
  /** When the player buys things
   *  c is the cost */
  def canAfford(c: Int) = money >= c
  def pay(c: Int) = if (canAfford(c)) money -= c
  
  
  /** assumes that there can only be one wave at once, 
  * and all the mobs in a wave are of same money value*/
  def getPaid() = {
    money += g.currentWave.mobs(0).moneyValue
  }
    
}