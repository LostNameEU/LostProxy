package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.ETimeUnit;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.utils.MongoCollection;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class BanHistoryCommand extends Command implements TabExecutor {

    public BanHistoryCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length != 1) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/banhistory§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/banhistory <Spieler> §8» §7Listet die gesamte Bannhistory des angegebenen Spielers an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/baninfo ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else {
            UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (targetUUID != null) {
                IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(targetUUID);
                IPlayerSync targetIPlayer = new IPlayerSync(targetUUID);
                if (iBanHistory.getHistory().size() > 0) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bans von " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + "§8:").build());
                    commandSender.sendMessage(new MessageBuilder("§8┃ §7Anzahl §8» §c" + iBanHistory.getHistory().size()).build());
                    AtomicInteger currentEntry = new AtomicInteger(-1);

                    iBanHistory.getHistory().forEach(iBanEntry -> {

                        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iBanEntry.getTimestamp()));
                        String time = new SimpleDateFormat("HH:mm:ss").format(new Date(iBanEntry.getTimestamp()));

                        switch (iBanEntry.getEBanEntryType()) {
                            case BAN_ENTRY:

                                String unbanDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iBanEntry.getEnd()));
                                String unbanTime = new SimpleDateFormat("HH:mm:ss").format(new Date(iBanEntry.getEnd()));

                                boolean banIsPermanent = iBanEntry.getTime() == -1;
                                if (iBanEntry.isInvokerConsole()) {
                                    commandSender.sendMessage(new MessageBuilder("§8┃ §cBan §8» §e" + date + " §7@ §e" + time + " §8» §4Konsole §8» §e" + iBanEntry.getReason() + " §8» §c" + (banIsPermanent ? "permanent" : iBanEntry.getTime() + " " + ETimeUnit.getDisplayName(iBanEntry.getTime(), iBanEntry.getETimeUnit())) + " §8» §a" + (banIsPermanent ? "/" : unbanDate + " §7@ §a" + unbanTime)).build());
                                } else {
                                    IPlayerSync iPlayer = new IPlayerSync(UUID.fromString(iBanEntry.getInvokerId()));
                                    commandSender.sendMessage(new MessageBuilder("§8┃ §cBan §8» §e" + date + " §7@ §e" + time + " §8» " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §8» §e" + iBanEntry.getReason() + " §8» §c" + (banIsPermanent ? "permanent" : iBanEntry.getTime() + " " + ETimeUnit.getDisplayName(iBanEntry.getTime(), iBanEntry.getETimeUnit())) + " §8» §a" + (banIsPermanent ? "/" : unbanDate + " §7@ §a" + unbanTime)).build());
                                }
                                break;
                            case UNBAN_ENTRY:
                                if (iBanEntry.isInvokerConsole()) {
                                    commandSender.sendMessage(new MessageBuilder("§8┃ §aUnban §8» §e" + date + " §7@ §e" + time + " §8» §4Konsole §8» §e" + iBanEntry.getReason()).build());
                                } else {
                                    IPlayerSync iPlayer = new IPlayerSync(UUID.fromString(iBanEntry.getInvokerId()));
                                    commandSender.sendMessage(new MessageBuilder("§8┃ §aUnban §8» §e" + date + " §7@ §e" + time + " §8» " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §8» §e" + iBanEntry.getReason()).build());
                                }
                                break;
                            case BAN_APPEAL_ENTRY:
                                if (iBanEntry.isInvokerConsole()) {
                                    commandSender.sendMessage(new MessageBuilder("§8┃ §eEA §8» §e" + date + " §7@ §e" + time + " §8» §4Konsole §8» §e" + iBanEntry.getReason()).build());
                                } else {
                                    IPlayerSync iPlayer = new IPlayerSync(UUID.fromString(iBanEntry.getInvokerId()));
                                    commandSender.sendMessage(new MessageBuilder("§8┃ §aUnban §8» §e" + date + " §7@ §e" + time + " §8» " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §8» §e" + iBanEntry.getReason()).build());
                                }
                                break;
                        }

                                currentEntry.set(currentEntry.get() + 1);

                                if (iBanHistory.getHistory().size() == currentEntry.get()) {
                                    commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                }
                            });
                        } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7hat §ckeine §7Ban-History§8.").build());
                        }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Zu dem angegebenen Spielernamen konnte §ckeine §7UUID gefunden werden§8.").build());
                }
        }
    }

    @SuppressWarnings("deprecation")
    public String calculateRemainingTime(long millis) {
        int seconds = 0, minutes = 0, hours = 0, days = 0;

        while (millis >= 1000) {
            millis -= 1000;
            seconds++;
        }

        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }

        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }

        while (hours >= 24) {
            hours -= 24;
            days++;
        }

        String estimatedTime = "";

        if (days == 1) {
            estimatedTime += "ein §7Tag§8, ";
        } else if (days > 1) {
            estimatedTime += days + " §7Tage§8, ";
        }

        if (hours == 1) {
            estimatedTime += "§ceine §7Stunde§8, ";
        } else if (hours > 1) {
            estimatedTime += "§c" + hours + " §7Stunden§8, ";
        }

        if (minutes == 1) {
            estimatedTime += "§ceine §7Minute und ";
        } else if (minutes > 1) {
            estimatedTime += "§c" + minutes + " §7Minuten und ";
        }

        if (seconds == 1) {
            estimatedTime += "§ceine §7Sekunde";
        } else if (seconds > 1) {
            estimatedTime += "§c" + seconds + " §7Sekunden";
        }

        return estimatedTime;
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 0) {
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.BAN_HISTORIES).find().forEach(all -> list.add(new IPlayerSync(UUID.fromString(all.getString("_id"))).getPlayerName()));
        }
        return list;
    }
}
