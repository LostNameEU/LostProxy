package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.historyandentries.kick.IKickEntry;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickCommand extends Command {

    public KickCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/kick§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/kick <Spieler> [Grund] §8» §7Kicke den angegebenen Spieler mit einem Grund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/kick ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
        } else {

            ProxiedPlayer target = null;
            for (ProxiedPlayer one : ProxyServer.getInstance().getPlayers()) {
                if (one.getName().equalsIgnoreCase(strings[0]))
                    target = one;
            }

            if (target != null) {
                if (!commandSender.getName().equalsIgnoreCase(target.getName())) {
                    ProxiedPlayer finalTarget = target;

                    LostProxy.getInstance().getPlayerManager().getIPlayerAsync(target.getUniqueId(), targetIPlayer -> {
                        if (commandSender.hasPermission("lostproxy.command.kick." + targetIPlayer.getiPermissionGroup().getName().toLowerCase())) {
                            String reason = LostProxy.getInstance().formatArrayToString(1, strings);

                            finalTarget.disconnect(new MessageBuilder("§6§o■§r §8┃ §c§lLostName §8● §7the new version of us §8┃ §6§o■§r \n" +
                                    "\n" +
                                    "§7Deine bestehende Verbindung zum Netzwerk wurde §egetrennt§8." +
                                    "\n" +
                                    "\n" +
                                    "§7Grund §8➡ §e" + reason +
                                    "\n" +
                                    "\n" +
                                    "§7Bei weiteren Fragen besuche unser §eForum§8!" +
                                    "\n" +
                                    " §8» §cforum§8.§clostname§8.§ceu §8«" +
                                    "\n" +
                                    "\n" +
                                    "§8§m--------------------------------------§r").build());

                            String uniqueId = finalTarget.getUniqueId().toString();
                            LostProxy.getInstance().getHistoryManager().getKickHistory(uniqueId, iKickHistory -> {
                                iKickHistory.addEntry(new IKickEntry(uniqueId, (commandSender instanceof ProxiedPlayer ? ((ProxiedPlayer) commandSender).getUniqueId().toString() : "console"), reason, System.currentTimeMillis()));
                                LostProxy.getInstance().getHistoryManager().saveKickHistory(uniqueId, iKickHistory, aBoolean -> {
                                    if (aBoolean) {
                                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast " + targetIPlayer.getPrefix() + targetIPlayer.getPlayerName() + " §7wegen §e" + reason + "§7gekickt§8.").build());
                                    } else {
                                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Beim Speichern der §eHistory §7ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                                    }
                                });
                            });

                            if (commandSender instanceof ProxiedPlayer) {
                                LostProxy.getInstance().getPlayerManager().getIPlayerAsync(((ProxiedPlayer) commandSender).getUniqueId(), invoker -> LostProxy.getInstance().getTeamManager().getNotificationOn().forEach(all -> {
                                    all.sendMessage(new MessageBuilder(Prefix.BKMS + invoker.getPrefix() + commandSender.getName() + " §8➼ " + targetIPlayer.getPrefix() + finalTarget.getName()).build());
                                    all.sendMessage(new MessageBuilder("§8┃ §7Typ §8» §cKick").build());
                                    all.sendMessage(new MessageBuilder("§8┃ §7Grund §8» §e" + reason).build());
                                    all.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                }));
                            } else {
                                LostProxy.getInstance().getTeamManager().getNotificationOn().forEach(all -> {
                                    all.sendMessage(new MessageBuilder(Prefix.BKMS + "§4" + commandSender.getName() + " §8➼ " + targetIPlayer.getPrefix() + finalTarget.getName()).build());
                                    all.sendMessage(new MessageBuilder("§8┃ §7Typ §8» §cKick").build());
                                    all.sendMessage(new MessageBuilder("§8┃ §7Grund §8» §e" + reason).build());
                                    all.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                });
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §7Rechte§8, §7um " + targetIPlayer.getPrefix() + targetIPlayer.getPlayerName() + " §7zu §ekicken§8.").build());
                        }
                    });
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du darfst dich §cnicht §7selber §ekicken§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler konnte §cnicht §7gefunden werden§8.").build());
            }
        }
    }
}
