werewolf
========
Currently the games can only be started by an admin.  There is currently only a single super admin (Forcibly set with the password: admin, and username: admin), however functionality has been added to allow other users to be set as admins and the super admin will be removed once development is finished.

The currently viewable links include:
+ \           :: Shows a form to add a user.
+ \player     :: prompts for login by a user, also shows forms for allowing the user to participate in a game(assuming a game is running and the user is currently linked with a player in the game).  Forms include a selectable vote list, and a scent list which werewolves can submit on to attack people.
+ \admin      :: accessible only by the super admin(for now), allows for the creation of a new game and the addition of users

Post:
+ \newgame    :: starts a new game.  Must have admin credentials to do this.
+ \

Get(without .jsp views)
+ \game       :: returns a boolean value of whether the game is running or not to the requester.
+ \highscores :: returns a list of WerewolfUser's scores
+ \players\getVotable

