package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TeamCommand extends Command {
    public TeamCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            if (strings.length == 0) {
                player.sendMessage(new MessageBuilder(Prefix.TMS + "Benutzung von §a/team§8:").build());
                player.sendMessage(new MessageBuilder("§8┃ §a/team login §8» §7Loggt dich in das Team Management System ein").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/team login").build());
                player.sendMessage(new MessageBuilder("§8┃ §a/team logout §8» §7Loggt dich aus dem Team Management System aus").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/team logout").build());
                player.sendMessage(new MessageBuilder("§8┃ §a/team list §8» §7Liste dir alle verfügbaren Teammitglieder auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/team list").build());
                player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
            } else if (strings.length == 1) {
                IPlayerSync iPlayer = new IPlayerSync(player.getUniqueId());
                switch (strings[0]) {
                    case "login":
                        if (player.hasPermission("lostproxy.command.team.login")) {
                            if (!LostProxy.getInstance().getTeamManager().isLoggedIn(player)) {
                                if (LostProxy.getInstance().getTeamManager().login(player)) {
                                    LostProxy.getInstance().getTeamManager().getLoggedIn().forEach(all -> all.sendMessage(new MessageBuilder(Prefix.TMS + iPlayer.getPrefix() + player.getName() + " §7hat sich §aeingeloggt§8.").build()));
                                } else {
                                    player.sendMessage(new MessageBuilder(Prefix.TMS + "§7Es ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                                }
                            } else {
                                player.sendMessage(new MessageBuilder(Prefix.TMS + "Du bist §cbereits §7eingeloggt§8.").build());
                            }
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.TMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§8.").build());
                        }
                        break;
                    case "logout":
                        if (player.hasPermission("lostproxy.command.team.logout")) {
                            if (LostProxy.getInstance().getTeamManager().isLoggedIn(player)) {
                                if (LostProxy.getInstance().getTeamManager().logout(player)) {
                                    player.sendMessage(new MessageBuilder(Prefix.TMS + "Du wurdest §causgeloggt§8.").build());
                                    LostProxy.getInstance().getTeamManager().getLoggedIn().forEach(all -> all.sendMessage(new MessageBuilder(Prefix.TMS + iPlayer.getPrefix() + player.getName() + " §7hat sich §causgeloggt§8.").build()));
                                } else {
                                    player.sendMessage(new MessageBuilder(Prefix.TMS + "§7Es ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                                }
                            } else {
                                player.sendMessage(new MessageBuilder(Prefix.TMS + "Du bist §cnicht §7eingeloggt§8.").build());
                            }
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.TMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§8.").build());
                        }
                        break;
                    case "list":
                        if (player.hasPermission("lostproxy.command.team.list")) {
                            player.sendMessage(new MessageBuilder(Prefix.TMS + "Übersicht der Teammitglieder§8:").build());
                            ProxyServer.getInstance().getPlayers().stream().filter(filter -> filter.hasPermission("lostproxy.command.team")).forEach(all -> {
                                IPlayerSync allIPlayer = new IPlayerSync(all.getUniqueId());
                                player.sendMessage(new MessageBuilder("§8┃ " + allIPlayer.getPrefix() + allIPlayer.getPlayerName() + " §8» " + (LostProxy.getInstance().getTeamManager().isLoggedIn(all) ? "§a✔" : "§c✖") + " §8» §7" + all.getServer().getInfo().getName()).build());
                            });
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.TMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§8.").build());
                        }
                        break;
                    default:
                        player.sendMessage(new MessageBuilder(Prefix.TMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                        break;
                }
            } else {
                player.sendMessage(new MessageBuilder(Prefix.TMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.TMS + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§8.").build());
        }
    }
}