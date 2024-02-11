# Tower Defense Game
A solo project for Ohjelmointistudio 2 Course in spring 2018. Graded 5/5.  

![Game GIF](https://github.com/valzugg/Tower-Defence-Project/blob/master/tdthing.gif)

The program can be executed via `Desert%20Tower%20Defence/src/user_interface/Game.scala`.  

## References
Implemented with [**Scala**](https://www.scala-lang.org/) and the [**Processing graphical library**](https://processing.org/), and [**Minim**](http://code.compartmental.net/tools/minim/) for playing sound.  
Sound effects are royalty free sounds from [**freesound**](https://freesound.org/), most graphical assets from [**Kenney**](https://www.kenney.nl/assets/tower-defense-top-down).  
  
An extensive documentation in Finnish can be found [**here**](https://github.com/valzugg/Tower-Defence-Project/blob/master/OS2%20Projekti%20Dokumentti.pdf).

## Known bugs in the game
- the range is limited by the  projectile's 'age', if the range exceeds something around 320, the projectiles just dissappear
- the fastforwarding function sometimes moves the mobs sideways and prevents bullets from hitting when they should
- the mobs walking animation freezes in the last square on the path
