package general

import user_interface.Game
import ddf.minim._

class Sounds(g: Game) extends Minim(g) {
  
  var muted = false
  
  val arrow    = loadFile("sound/arrow.wav")
  val antDead  = loadFile("sound/antDead.wav")
  val bg       = loadFile("sound/wind.wav")
  val build    = loadFile("sound/build.wav")
  val crossbow = loadFile("sound/crossbow.wav")
  
  def play(a: AudioPlayer) = {
    a.play()
    a.rewind()
  }
  
}