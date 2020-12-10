package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.historyandentries.IEntry;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class KickInfoCommand extends Command {
    public KickInfoCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/kickinfo§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/kickinfo <Spieler> §8» §7Zeigt dir die History-Einträge eines Spielers").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/kickinfo ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0], targetUUID -> {
                if (targetUUID != null) {
                    LostProxy.getInstance().getHistoryManager().getKickHistory(targetUUID.toString(), iKickHistory -> LostProxy.getInstance().getPlayerManager().getIPlayerAsync(targetUUID, iPlayer -> {
                        if (iKickHistory.getHistory().size() > 0) {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Kicks von " + iPlayer.getPrefix() + iPlayer.getPlayerName() + "§8:").build());


                            AtomicInteger currentEntry = new AtomicInteger(-1);

                            iKickHistory.getHistory().stream().sorted(Comparator.comparingLong(IEntry::getTimestamp)).forEach(iKickEntry -> {
                                String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iKickEntry.getTimestamp()));
                                String time = new SimpleDateFormat("HH:mm:ss").format(new Date(iKickEntry.getTimestamp()));

                                if (iKickEntry.getInvokerId().equalsIgnoreCase("console")) {
                                    commandSender.sendMessage(new MessageBuilder("§8┃ §e" + date + " §7@ §e" + time + " §8» §4Konsole §8» §c" + iKickEntry.getReason()).build());
                                } else {
                                    LostProxy.getInstance().getPlayerManager().getIPlayerAsync(UUID.fromString(iKickEntry.getInvokerId()), invokerIPlayer -> commandSender.sendMessage(new MessageBuilder("§8┃ §e" + date + " §7@ §e" + time + " §8» " + invokerIPlayer.getPrefix() + invokerIPlayer.getPlayerName() + " §8» §c" + iKickEntry.getReason()).build()));
                                }

                                currentEntry.set(currentEntry.get() + 1);

                                if (iKickHistory.getHistory().size() == currentEntry.get()) {
                                    commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                }
                            });
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + iPlayer.getPrefix() + iPlayer.getPlayerName() + " §7hat §ckeine §7Kick-History§8.").build());
                        }
                    }));
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Zu dem angegebenen Spielernamen konnte §ckeine §7UUID gefunden werden§8.").build());
                }
            });
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
        }
    }
}
