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
  val woop     = loadFile("sound/woop.wav")
  val gunShot  = loadFile("sound/gunShot.wav")
  
  val arr = Vector[AudioPlayer](arrow,antDead,bg,build,crossbow,woop,gunShot)
  
  def play(a: AudioPlayer) = {
    if (!muted) {
      a.play()
      a.rewind()
    }
  }
  
  def loop(a: AudioPlayer) = { a.loop() }
  
  def toggleMute() = {
    if (muted) {
      muted = false
      arr.foreach(_.unmute())
    } else {
      muted = true
      arr.foreach(_.mute())
    }
  }
  
}