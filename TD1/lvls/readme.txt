The '.lvl' files follow the following format:

1. First line isn't used for anything, 
   but can be for example the name of the level, or note on difficulty.

2. After line "MONEY", as indicated, comes the money the player starts with in this level.
   This is just a single non-negative integer on one line.

3. After line "ARENA", comes first the dimensions of the arena, 
   for example 10 wide and 20 high would be "10x20".

4. After the dimensions comes the actual information about the arena's content.
   The arena's information must be as specified in the dimensions, and only use
   the characters '0','1','x' and 't' separated by single spaces.
   
   '0' indicates empty space on an arena.
   '1' indicates path on an arena.
   'x' indicates an obstacle on an arena.
   't' indicates an empty tower on an arena.
   
   The path of the arena must also follow some rules:
   4.1 It must begin from the left side of the arena, and it must be unambigious. 
       (only starts from one spot)
   4.2 It must lead out of the top or the bottom of the arena.
   4.3 It musn't go in an infinite loop or be over 2000 tiles long.
       (The mobs prefer turning right over left, which can sometimes 
       lead in this happening accidentally.)
   4.4 It must end unambigiously on the right side of of the arena.
   
5. After the line "WAVES", (and the line after) come the mob waves in order of occurance.
   The format of the numbers here must be:
   waveSize_ (space) distance_ (space) speed_ (space)  health_ (space) mobSize_ (space) spriteIndex
   Only distance, speed and mobSize can be decimals.
   For example: 5_ 1.8_ 1.5_ 100_ 0.50_ 1