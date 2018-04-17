package objects

import user_interface.Game

class Player(g: Game, initMoney: Int) {
  var money = initMoney
  var hp    = 100
  
  // assumes that there can only be one wave at once, 
  // and all the mobs in a wave are of same money value
  def getPaid() = {
    money += g.currentWave.mobs(0).moneyValue
  }
    
}