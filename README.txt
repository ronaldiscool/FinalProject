Objective: The game is divided into two teams, town and anti-town. The goal of the town is to uncover the members of the anti-town hidden amongst them in order to lynch them. They have access to the information provided by the cop and subtle hints that they can ascertain from other players’ actions. The goal of the mafia is to create chaos amongst members of the town in order to mask their identities while they kill the villagers(one per night). 


Gameplay:
The game is split into two phases, day and night. During the day phase, the town holds a meeting so that they can discuss who they will lynch. The person who receives the most votes will be lynched and will be removed from the game. During the night, only players with special abilities will be allowed to act, so the villagers will have to wait until the other players have completed their actions. 


When all of the members of the mafia have been lynched the town wins the game. If the anti-town is able to kill all the villagers then they are declared the winners.


Roles
There are several roles in this game, each with their own powers, with the exception of the villager. 


Town:
Villager - The villager’s role is to interpret the chat during the daytime and vote for a player they believe is part of the mafia. They have no special powers, but with an ideal numbers of players they should compose the majority of the players. 


Doctor - The doctor’s role is to protect a character who he/she believes is part of the town. They can protect anyone they wish, but in order to reach the objective it is beneficial to them to protect their team members. 


Cop - The cop’s role is to investigate player’s roles to uncover the members of the mafia. They will share this information in the daytime where the rest of the players will decide how they will act. All cops must agree on a person to investigate or else their power will have no effect. 


Anti-Town:
Mafia - The mafia’s role is to conceal their identity and cause confusion amongst the other players so that they vote for innocent players. During the night, the mafia can kill a player of their choosing, unless he/she is being protected by the doctor. In this situation, the mafia’s power would be nullified. 


Strategy:
Town - The town’s objective is to identify the members of the mafia, but they must careful to not be mislead by the mafia. During the first day, it will be difficult to determine which teams the players are on because they have not seen any patterns or have had as much interaction with everyone else. They will try to weed out the anti-town by questioning each motives and seeing who was killed and why. 


Anti-town - The anti-town wants to be a wolf in sheep’s clothing. They want to trick the villagers into believing they are innocent and seamlessly blend in with the rest. The anti-town can do this by pretending to not anything and play dumb. 




Setup
1. Run the createDB.sql script for accurate statistics. You will still be able to play the game without running the script, but you will not have statistics.
2. One player will start the server and act as the host. The host will also be a player in the game.
3. The host will declare the number of villagers, cops, doctors, and mafia.
4. All other players will start a client will be given the option to input the host’s IP address and port number (6789).
5. The players will be sent to a waiting room while waiting for the rest of the players to connect.
6. After all players have connected, they will be randomly assigned roles, and the clients will transition into the chatroom and the game will begin.
7. On the first day, players can chat and talk to each other. Normally, the players would vote for nobody, since they have no information to act upon, but if they do decide to vote for someone they may incriminating themselves and leave themselves open to future suspicion.
8. During the night, the members with special powers will awaken. Each respective role will vote who they want to save, kill, or identify.
9. The next day, the players will be able to see what action happened, specifically if anyone was saved or killed.
10. They will engage in conversation, and see who they think is part of the anti-town and lynch the player that has been voted for the most. 
11. The game will turn to night again, and the cycle will repeat.
12. This will happen until either the mafia has killed all the villagers, or all the anti-town has been lynched.