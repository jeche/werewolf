werewolf
========
Currently the games can only be started by an admin.  There is currently only a single super admin (Forcibly set with the password: admin, and username: admin), however functionality has been added to allow other users to be set as admins and the super admin will be removed once development is finished.

Features
--------

- []    Check for players nearby (if werewolf)
- []    Restart game (if admin)
- []    Get votable players
- []    Place a vote on a person (if townsperson)
- []    Kill a player (if werewolf and night)
- []    Report current position
- []    Get highscore list

The currently viewable links include:
<table>
  <tr>
    <th>Link</th><th>Description</th>
  </tr>
  <tr>
    <td>\</td><td>Shows a form to add a user.</td>
  </tr>
  <tr>
    <td>\player</td><td>prompts for login by a user, also shows forms for allowing the user to participate in a game(assuming a game is running and the user is currently linked with a player in the game).  Forms include a selectable vote list, and a scent list which werewolves can submit on to attack people.</td>
  </tr>
    <tr>
    <td>\admin</td><td>accessible only by the super admin(for now), allows for the creation of a new game and the addition of users</td>
  </tr>
</table>

Post
----
<table>
  <tr>
    <th>Link    </th><th>Description</th>
  </tr>
  <tr>
    <td>\newgame</td><td>starts a new game.  Must have admin credentials to do this.</td>
  </tr>
  <tr>
    <td>\players\location</td><td>updates a player's location based on the post.  Requested params are both of the type double and must be passed in as 'lat' and 'lng'</td>
  </tr>
    <tr>
    <td>\players\kill</td><td>allows a werewolf to kill its specified victim.  Can only occur at night.  Requested param is of type string and must be passed in as 'victim'.</td>
  </tr>
    <tr>
    <td>\players\vote</td><td>allows a player to vote during the daytime.  Requested param is of type string and must be passed in as 'voted'.</td>
  </tr>
</table>



Get(without .jsp views)
-----------------------
<table>
  <tr>
    <th>Link    </th><th>Description</th>
  </tr>
  <tr>
    <td>\game</td><td>returns a boolean value of whether the game is running or not to the requester.</td>
  </tr>
  <tr>
    <td>\highscores</td><td>returns a list of WerewolfUser's scores</td>
  </tr>
    <tr>
    <td>\players\getVotable</td><td>returns a list of players that can be voted for</td>
  </tr>
    <tr>
    <td>\players\all</td><td>returns a list of all Players.  If a game is running the list is sanitized.</td>
  </tr>
      <tr>
    <td>\players\scent</td><td>allows a werewolf to 'scent' the people nearby it.  Returns a list of players where players that are in scent but not kill range have a score of 0, those in kill range have a score of 1, and werewolves in range have a score of 2.</td>
  </tr>
        <tr>
    <td>\players\alive</td><td>returns a list of currently living players.  List is sanitized.</td>
  </tr>
</table>

To use app go to: http://secure-lake-6285.herokuapp.com
