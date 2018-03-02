package user_interface

import scala.swing._
import scala.swing.event._
import scala.io._
import java.awt.image.BufferedImage
import java.awt.Color
import java.awt.RenderingHints
import java.awt.event.ActionListener

object Game extends SimpleSwingApplication {
  var x = 0
  
  def top = Window

  top.contents = GameScreen
}

object Window extends MainFrame {
  
  val width  = 800
  val height = 600
  
  title     = "Val's Tower Defence"
  size      = new Dimension(width,height)
  resizable = false
  
  val listener = new ActionListener(){
      def actionPerformed(e : java.awt.event.ActionEvent) = {
        GameScreen.repaint()  
        Game.x += 1
      }
  }
  
  val timer = new javax.swing.Timer(50, listener)
  timer.start()
  
}