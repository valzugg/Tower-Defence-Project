package user_interface

import scala.swing._
import java.awt.Color

object GameScreen extends Screen {
  val array = "trololololololololololol".toCharArray
  
  background = new Color(43,92,96)
  override def paintComponent(g : Graphics2D) = {
    g.setColor(Color.red)
    g.drawString(array(Game.x%array.length).toString, 10+Game.x*8,250)
    g.drawString(array(Game.x%array.length).toString, 10+Game.x*8,300)
  }
}