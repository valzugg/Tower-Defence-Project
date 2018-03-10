package objects

import user_interface.Game

class Player(g: Game) {
  val initMoney = 10
  var money = 0
  var hp    = 100
  
  def buyTower(x: Int, y: Int) = {
    if (money > 4)
      if (g.arena.setTower(x,y)) money -= 5
  }
  
  //TODO: Rahasysteemi
  def getMoney() = {
    money = (g.wave.deadMobs.length*g.wave.mobs(0).moneyValue) + initMoney
  }
    
}