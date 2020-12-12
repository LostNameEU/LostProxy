# Übersicht aller Permissions in LostNameEU/LostProxy

| Datei | Command | Usage | Alias | Permission | Beschreibung |  
| :-: | :-: | :-: | :-: | :-: | :-:  
| KickCommand.java | /kick | /kick `Spielername` | / | lostproxy.command.kick | To use /kick |  
| ↑ | ↑ | ↑ | ↑ | lostproxy.command.kick.`group` | Permission to kick players with the permission group `group` |  
| KickHistoryClearCommand.java | /kickhistoryclear | /kickhistoryclear `Spielername` | /khclear <br\> /khc |
lostproxy.command.kickhistoryclear | To use /kickhistoryclear |  
| KickInfoCommand.java | /kickinfo | /kickinfo `Spielername` | /ki | lostproxy.command.kickinfo | To use /kickinfo |  
| NotifyCommand.java | /notify | /notify | /benachrichtigung | lostproxy.command.notify | To use /notify  
| PingCommand.java | /ping | /ping | / | lostproxy.command.ping | To use /ping  
| ↑ | ↑ | /ping `Spielername` | / | lostproxy.command.ping.other | To see the ping from other players  
| TCCommand.java | /tc | /tc `Nachricht` | /teamchat | lostproxy.command.tc | To use /tc  
| TeamCommand.java | /team | /team | / | lostproxy.command.team | To use /team  
| ↑ | ↑ | /team login | / | lostproxy.command.team.login | To login to the tm-system  
| ↑ | ↑ | /team logout | / | lostproxy.command.team.logout | To logout from the tm-system  
| ↑ | ↑ | /team list | / | lostproxy.command.team.list | To list all online team members  
| TSCommand.java | /ts | /ts | / | lostproxy.command.ts | To use /ts  
| ↑ | ↑ | /ts set | / | lostproxy.command.ts | To link a teamspeak identity  
| ↑ | ↑ | /ts unlink | / | lostproxy.command.ts | To unlink a linked teamspeak identity  
| ↑ | ↑ | /ts info | / | lostproxy.command.ts | To display teamspeak linkage specific information  
| ↑ | ↑ | /ts iinfo `Identität` | / | lostproxy.command.ts.iinfo | To show information specified on a given teamspeak
identity  
| ↑ | ↑ | /ts ninfo `Spielername` | / | lostproxy.command.ts.ninfo | To show information specified on a given
playername  
| ↑ | ↑ | /ts delete `Spielername` | / | lostproxy.command.ts.delete | To delete the teamspeak linkage of the given
playername  
| ↑ | ↑ | /ts set `Rang` `ID` | / | lostproxy.command.ts.set | To link a teamspeak server group to a permission group  
| PlayerDisconnectListener.java | / | / | / | lostproxy.command.notify | To disable the notification when player left
the network  
| ↑ | / | / | / | lostproxy.command.team <br\> lostproxy.command.team.logout | To logout the player when he lefts the
network and notify other team members that this player left the network