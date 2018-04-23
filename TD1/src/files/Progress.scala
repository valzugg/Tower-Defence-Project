package files

class Progress(i: Int = 0) {
  private var unlocked = i
  def available = unlocked
  def unlock() = unlocked += 1
}