package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;

public class KickHistoryClearCommand extends Command {

    public KickHistoryClearCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/khclear§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/khclear <Spieler> §8» §7Leert die Kick-History des angegebenen Spielers").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/khclear ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0], targetUUID -> {
                if (targetUUID != null) {
                    LostProxy.getInstance().getPlayerManager().getIPlayerAsync(targetUUID, targetiPlayer -> LostProxy.getInstance().getHistoryManager().getKickHistory(targetUUID, iKickHistory -> {
                        if (iKickHistory.getHistory().size() > 0) {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Möchtest du wirklich die §eKick-History §7von " + targetiPlayer.getPrefix() + targetiPlayer.getPlayerName() + " §clöschen§8?").build());
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "§7[§a§lKlick§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/khclear " + strings[0] + " confirmed").build());
                            if (!LostProxy.getInstance().getHistoryManager().getKickHistoryClearCommandProcess().contains(commandSender.getName())) {
                                LostProxy.getInstance().getHistoryManager().getKickHistoryClearCommandProcess().add(commandSender.getName());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die §eKick-History §7von " + targetiPlayer.getPrefix() + targetiPlayer.getPlayerName() + " §7ist §cleer§8.").build());
                        }
                    }));
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler konnte §cnicht §7gefunden werden§8.").build());
                }
            });
        } else if (strings.length == 2) {
            if (strings[1].equalsIgnoreCase("confirmed")) {
                if (LostProxy.getInstance().getHistoryManager().getKickHistoryClearCommandProcess().contains(commandSender.getName())) {
                    LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0], targetUUID -> {
                        if (targetUUID != null) {
                            LostProxy.getInstance().getPlayerManager().getIPlayerAsync(targetUUID, targetiPlayer -> LostProxy.getInstance().getHistoryManager().getKickHistory(targetUUID, iKickHistory -> {
                                LostProxy.getInstance().getHistoryManager().getKickHistoryClearCommandProcess().remove(commandSender.getName());
                                if (iKickHistory.getHistory().size() > 0) {
                                    iKickHistory.getHistory().clear();
                                    LostProxy.getInstance().getHistoryManager().saveKickHistory(iKickHistory, aBoolean -> {
                                        if (aBoolean) {
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §aerfolgreich §7die §eKick-History §7von " + targetiPlayer.getPrefix() + targetiPlayer.getPlayerName() + " §cgelöscht§8.").build());
                                        } else {
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Beim Löschen der §eHistory §7ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                                        }
                                    });
                                } else {
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die §eKick-History §7von " + targetiPlayer.getPrefix() + targetiPlayer.getPlayerName() + " §7ist §cleer§8.").build());
                                }
                            }));
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler konnte §cnicht §7gefunden werden§8.").build());
                        }
                    });
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §eVerifizierung §7für diesen §eProzess §7beantragt§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
        }
    }
}
