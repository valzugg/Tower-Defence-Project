# Tower Defence Game
A Project for Programming Studio 2 Course of Aalto University.  
Implemented with [**Scala**](https://www.scala-lang.org/) and the [**Processing graphical library**](https://processing.org/).

![Game GIF](https://github.com/valzugg/Tower-Defence-Project/blob/master/tdthing.gif)

The program can be executed via Desert%20Tower%20Defence/src/user_interface/Game.scala.  
An extensive documentation in Finnish can be found [**here**](https://github.com/valzugg/Tower-Defence-Project/blob/master/OS2%20Projekti%20Dokumentti.pdf)

## Known bugs in the game
- the range is limited by the  projectile's 'age',
  if the range exceeds something around 320, the projectiles just dissappear
- the fastforwarding function sometimes work to the players disadvantage
  by reducing the chance the bullets hit the target, especially with
  fast defence speeds
- the mobs walking animation freezes in the last square on the path
