package objects

import user_interface.Game

class Player(g: Game) {
  val initMoney = 10
  var money = 10
  var hp    = 100
  
  def getPaid() = {
    money += g.currentWave.mobs(0).moneyValue
  }
    
}