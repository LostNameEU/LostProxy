package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IUnbanEntry;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnbanCommand extends Command {

    public UnbanCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length <= 1) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/unban§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/unban <Spieler> [Grund] §8» §7Entbannt einen gebannten Spieler").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/unban ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else {
            LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0], uuid -> {
                if (uuid != null) {
                    LostProxy.getInstance().getPlayerManager().getIPlayerAsync(uuid, targetIPlayer -> LostProxy.getInstance().getBanManager().getBan(uuid, iBan -> {
                        if (iBan != null) {
                            LostProxy.getInstance().getBanManager().deleteBan(iBan, (deleteResult, throwable) -> {
                                if (deleteResult.wasAcknowledged()) {
                                    LostProxy.getInstance().getHistoryManager().getBanHistory(uuid, iBanHistory -> {
                                        String reason = LostProxy.getInstance().formatArrayToString(1, strings);
                                        String invokerId = (commandSender instanceof ProxiedPlayer ? ((ProxiedPlayer) commandSender).getUniqueId().toString() : "console");

                                        iBanHistory.addEntry(new IUnbanEntry(uuid, invokerId, System.currentTimeMillis(), reason));
                                        LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory, aBoolean -> {
                                            if (aBoolean) {
                                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getPrefix() + targetIPlayer.getPlayerName() + " §7wurde §aentbannt§8.").build());
                                                LostProxy.getInstance().getTeamManager().sendUnbanNotify(invokerId, targetIPlayer.getPrefix() + targetIPlayer.getPlayerName(), reason);
                                            } else {
                                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Beim Speichern des §eBanhistory §7ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                                            }
                                        });
                                    });
                                } else {
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Beim Löschen des §eBans §7ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                                }
                            });
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getPrefix() + targetIPlayer.getPlayerName() + " §7ist §cnicht §7gebannt§8.").build());
                        }
                    }));
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                }
            });
        }
    }
}