# DishesNeedPacking
Fast-Paced spatial puzzle Android Game published on the Google Play Store.

The base idea of the game is filling in a dishwasher with dishes as efficient as possible.
It's a spin on a jigsaw puzzle, where instead of puzzle pieces you have dishware; plates, cups bowls, tupperware, and utensils.
The faster you fill the dishwasher the higher your score, but you need to fill it in efficiently are you won't have enough space.


The core of my code will be found in app/src/main/java/garlicquasar/babel/matt/.

The game logic can be found in the game directory.
The main file for the start of the game is found in Game/View.
The game map is in the Game/Rack.
The dishware objects are in Game/DishObjects.
Dish placement algorithms are in Game/Placement.
Puzzle generation and solving is found in Game/Puzzle. - These files were my most complex and still need some tweaking. 
Touch handling is found in Game/Input.
Static files for data storage and handling is found in Game/DataHolders.

Other than the core game code I have multiple other files for the UI and highscore data managment. 
I use a local database to store game scores that aren't saved globally, found in Database.
I handle user logins through Google Authentication, once a user logs in the local highscore data is overwritten with highscore data pulled from my server. 
My HTTPHandler file is currently being ignored until I make it secure.

The main screen and splash screen are found in the Activities directory.
The rest of my screens are Fragments and found in the Fragments directory.
I allow custom games to be made and played, the UI is found in Fragments/GameInputFragment.java
The rest of the fragments are pretty self-identifiable. 

I may update this game in the future but currently I am working on other projects.
If this gains a following I'd love to add new artwork to this and tweak/add to my algorithms some more.
Also would like to add some more game mechanics. For now though, this project is on pause. 
