package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.historyandentries.IEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanAppealEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IUnbanEntry;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class BanInfoCommand extends Command {

    public BanInfoCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length != 1) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/baninfo§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/baninfo <Spieler> §8» §7Listet die gesamte Bannhistory des angegebenen Spielers an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/baninfo ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else {
            LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0], targetUUID -> {
                if (targetUUID != null) {
                    LostProxy.getInstance().getHistoryManager().getBanHistory(targetUUID, iBanHistory -> LostProxy.getInstance().getPlayerManager().getIPlayerAsync(targetUUID, targetIPlayer -> {
                        if (iBanHistory.getHistory().size() > 0) {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bans von " + targetIPlayer.getPrefix() + targetIPlayer.getPlayerName() + "§8:").build());
                            commandSender.sendMessage(new MessageBuilder("§8┃ §7Anzahl §8» §c" + iBanHistory.getHistory().size()).build());
                            AtomicInteger currentEntry = new AtomicInteger(-1);

                            iBanHistory.getHistory().stream().sorted(Comparator.comparingLong(IEntry::getTimestamp)).forEach(iBanSpecificEntry -> {

                                String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iBanSpecificEntry.getTimestamp()));
                                String time = new SimpleDateFormat("HH:mm:ss").format(new Date(iBanSpecificEntry.getTimestamp()));

                                switch (iBanSpecificEntry.getIBanEntryType()) {

                                    case BAN_ENTRY:
                                        IBanEntry iBanEntry = (IBanEntry) iBanSpecificEntry;

                                        String unbanDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iBanEntry.getEnd()));
                                        String unbanTime = new SimpleDateFormat("HH:mm:ss").format(new Date(iBanEntry.getEnd()));

                                        if (iBanEntry.isInvokerConsole()) {
                                            commandSender.sendMessage(new MessageBuilder("§8┃ §cBan §8» §e" + date + " §7@ §e" + time + " §8» §4Konsole §8» §e" + iBanEntry.getReason() + " §8» §c" + displayBanDuration(iBanEntry.getDuration()) + " §8» §a" + unbanDate + " §7@ §a" + unbanTime).build());
                                        } else {
                                            LostProxy.getInstance().getPlayerManager().getIPlayerAsync(iBanEntry.getUniqueId(), iPlayer -> commandSender.sendMessage(new MessageBuilder("§8┃ §cBan §8» §e" + date + " §7@ §e" + time + " §8» " + iPlayer.getPrefix() + iPlayer.getPlayerName() + " §8» §e" + iBanEntry.getReason() + " §8» §c" + displayBanDuration(iBanEntry.getDuration()) + " §8» §a" + date + " §7@ §a" + time).build()));
                                        }
                                        break;
                                    case UNBAN_ENTRY:
                                        IUnbanEntry iUnbanEntry = (IUnbanEntry) iBanSpecificEntry;

                                        if (iUnbanEntry.isInvokerConsole()) {
                                            commandSender.sendMessage(new MessageBuilder("§8┃ §aUnban §8» §e" + date + " §7@ §e" + time + " §8» §4Konsole §8» §e" + iUnbanEntry.getReason()).build());
                                        } else {
                                            LostProxy.getInstance().getPlayerManager().getIPlayerAsync(iUnbanEntry.getUniqueId(), iPlayer -> commandSender.sendMessage(new MessageBuilder("§8┃ §aUnban §8» §e" + date + " §7@ §e" + time + " §8» " + iPlayer.getPrefix() + iPlayer.getPlayerName() + " §8» §e" + iUnbanEntry.getReason()).build()));
                                        }
                                        break;
                                    case BAN_APPEAL_ENTRY:
                                        IBanAppealEntry iBanAppealEntry = (IBanAppealEntry) iBanSpecificEntry;

                                        if (iBanAppealEntry.isInvokerConsole()) {
                                            commandSender.sendMessage(new MessageBuilder("§8┃ §eEA §8» §e" + date + " §7@ §e" + time + " §8» §4Konsole §8» §e" + iBanAppealEntry.getReason()).build());
                                        } else {
                                            LostProxy.getInstance().getPlayerManager().getIPlayerAsync(iBanAppealEntry.getUniqueId(), iPlayer -> commandSender.sendMessage(new MessageBuilder("§8┃ §aUnban §8» §e" + date + " §7@ §e" + time + " §8» " + iPlayer.getPrefix() + iPlayer.getPlayerName() + " §8» §e" + iBanAppealEntry.getReason()).build()));
                                        }
                                        break;
                                }
                                currentEntry.set(currentEntry.get() + 1);

                                if (iBanHistory.getHistory().size() == currentEntry.get()) {
                                    commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                }
                            });
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getPrefix() + targetIPlayer.getPlayerName() + " §7hat §ckeine §7Kick-History§8.").build());
                        }
                    }));
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Zu dem angegebenen Spielernamen konnte §ckeine §7UUID gefunden werden§8.").build());
                }
            });
        }
    }

    @SuppressWarnings("deprecation")
    private String displayBanDuration(long duration) {
        Time time = new Time(duration);
        String estimatedTime = "";

        if (time.getDay() == 1) {
            estimatedTime = "ein §7Tag§8, ";
        } else if (time.getDay() > 1) {
            estimatedTime = time.getDay() + " §7Tage§8, ";
        }

        if (time.getHours() == 1) {
            estimatedTime = "§ceine §7Stunde§8, ";
        } else if (time.getHours() > 1) {
            estimatedTime = "§c" + time.getHours() + " §7Stunden§8, ";
        }

        if (time.getMinutes() == 1) {
            estimatedTime = "§ceine §7Minute und ";
        } else if (time.getMinutes() > 1) {
            estimatedTime = "§c" + time.getMinutes() + " §7Minuten und ";
        }

        if (time.getSeconds() == 1) {
            estimatedTime = "§ceine §7Sekunde";
        } else if (time.getSeconds() > 1) {
            estimatedTime = "§c" + time.getSeconds() + " §7Sekunden";
        }

        return estimatedTime;
    }

}
