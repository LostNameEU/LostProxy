package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.historyandentries.IEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.kick.IKickHistory;
import eu.lostname.lostproxy.utils.MongoCollection;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class KickHistoryCommand extends Command implements TabExecutor {
    public KickHistoryCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/kickhistory§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/kickhistory <Spieler> §8» §7Zeigt dir die History-Einträge eines Spielers").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/kickinfo ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (targetUUID != null) {
                IKickHistory iKickHistory = LostProxy.getInstance().getHistoryManager().getKickHistory(targetUUID);
                IPlayerSync iPlayer = new IPlayerSync(targetUUID);
                if (iKickHistory.getHistory().size() > 0) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Kicks von " + iPlayer.getDisplay() + iPlayer.getPlayerName() + "§8:").build());
                    commandSender.sendMessage(new MessageBuilder("§8┃ §7Anzahl §8» §c" + iKickHistory.getHistory().size()).build());
                    AtomicInteger currentEntry = new AtomicInteger(-1);

                    iKickHistory.getHistory().stream().sorted(Comparator.comparingLong(IEntry::getTimestamp)).forEach(iKickEntry -> {
                        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iKickEntry.getTimestamp()));
                        String time = new SimpleDateFormat("HH:mm:ss").format(new Date(iKickEntry.getTimestamp()));

                        if (iKickEntry.getInvokerId().equalsIgnoreCase("console")) {
                            commandSender.sendMessage(new MessageBuilder("§8┃ §e" + date + " §7@ §e" + time + " §8» §4Konsole §8» §c" + iKickEntry.getReason()).build());
                        } else {
                            IPlayerSync invokerIPlayer = new IPlayerSync(UUID.fromString(iKickEntry.getInvokerId()));
                            commandSender.sendMessage(new MessageBuilder("§8┃ §e" + date + " §7@ §e" + time + " §8» " + invokerIPlayer.getDisplay() + invokerIPlayer.getPlayerName() + " §8» §c" + iKickEntry.getReason()).build());
                        }

                        currentEntry.set(currentEntry.get() + 1);

                        if (iKickHistory.getHistory().size() == currentEntry.get()) {
                            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                        }
                    });
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §7hat §ckeine §7Kick-History§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Zu dem angegebenen Spielernamen konnte §ckeine §7UUID gefunden werden§8.").build());
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 0) {
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.KICK_HISTORIES).find().forEach(one -> {
                IKickHistory iKickHistory = LostProxy.getInstance().getGson().fromJson(one.toJson(), IKickHistory.class);
                if (iKickHistory.getHistory().size() > 1) {
                    list.add(new IPlayerSync(iKickHistory.getUniqueId()).getPlayerName());
                }
            });
        }
        return list;
    }
}
