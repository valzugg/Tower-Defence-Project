package objects

import arena.Arena

object Player {
  var money = 10
  var hp    = 100
  
  def buyTower(x: Int, y: Int, a: Arena) = {
    if (money > 4)
      if (a.setTower(x,y)) money -= 5
  }
    
}