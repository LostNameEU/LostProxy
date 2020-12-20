package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.historyandentries.IEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.mute.IMuteEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.mute.IUnmuteEntry;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class MuteInfoCommand extends Command {

    public MuteInfoCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/muteinfo§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/muteinfo <Spieler> §8» §7Listet die gesamte Mutehistory des angegebenen Spielers an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/muteinfo ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0], targetUUID -> {
                if (targetUUID != null) {
                    LostProxy.getInstance().getHistoryManager().getMuteHistory(targetUUID, iMuteHistory -> LostProxy.getInstance().getPlayerManager().getIPlayerAsync(targetUUID, targetIPlayer -> {
                        if (iMuteHistory.getHistory().size() > 0) {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Mutes von " + targetIPlayer.getPrefix() + targetIPlayer.getPlayerName() + "§8:").build());
                            commandSender.sendMessage(new MessageBuilder("§8┃ §7Anzahl §8» §c" + iMuteHistory.getHistory().size()).build());
                            AtomicInteger currentEntry = new AtomicInteger(-1);

                            iMuteHistory.getHistory().stream().sorted(Comparator.comparingLong(IEntry::getTimestamp)).forEach(iMuteSpecificEntry -> {

                                String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iMuteSpecificEntry.getTimestamp()));
                                String time = new SimpleDateFormat("HH:mm:ss").format(new Date(iMuteSpecificEntry.getTimestamp()));

                                switch (iMuteSpecificEntry.getIMuteEntryType()) {

                                    case MUTE_ENTRY:
                                        IMuteEntry iMuteEntry = (IMuteEntry) iMuteSpecificEntry;

                                        String unmuteDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iMuteEntry.getEnd()));
                                        String unmuteTime = new SimpleDateFormat("HH:mm:ss").format(new Date(iMuteEntry.getEnd()));

                                        if (iMuteEntry.isInvokerConsole()) {
                                            commandSender.sendMessage(new MessageBuilder("§8┃ §cMute §8» §e" + date + " §7@ §e" + time + " §8» §4Konsole §8» §e" + iMuteEntry.getReason() + " §8» §c" + displayMuteDuration(iMuteEntry.getDuration()) + " §8» §a" + unmuteDate + " §7@ §a" + unmuteTime).build());
                                        } else {
                                            LostProxy.getInstance().getPlayerManager().getIPlayerAsync(iMuteEntry.getUniqueId(), iPlayer -> commandSender.sendMessage(new MessageBuilder("§8┃ §cMute §8» §e" + date + " §7@ §e" + time + " §8» " + iPlayer.getPrefix() + iPlayer.getPlayerName() + " §8» §e" + iMuteEntry.getReason() + " §8» §c" + displayMuteDuration(iMuteEntry.getDuration()) + " §8» §a" + date + " §7@ §a" + time).build()));
                                        }
                                        break;
                                    case UNMUTE_ENTRY:
                                        IUnmuteEntry iUnmuteEntry = (IUnmuteEntry) iMuteSpecificEntry;

                                        if (iUnmuteEntry.isInvokerConsole()) {
                                            commandSender.sendMessage(new MessageBuilder("§8┃ §aUnmute §8» §e" + date + " §7@ §e" + time + " §8» §4Konsole §8» §e" + iUnmuteEntry.getReason()).build());
                                        } else {
                                            LostProxy.getInstance().getPlayerManager().getIPlayerAsync(iUnmuteEntry.getUniqueId(), iPlayer -> commandSender.sendMessage(new MessageBuilder("§8┃ §aUnmute §8» §e" + date + " §7@ §e" + time + " §8» " + iPlayer.getPrefix() + iPlayer.getPlayerName() + " §8» §e" + iUnmuteEntry.getReason()).build()));
                                        }
                                        break;
                                }
                                currentEntry.set(currentEntry.get() + 1);

                                if (iMuteHistory.getHistory().size() == currentEntry.get()) {
                                    commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                }
                            });
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getPrefix() + targetIPlayer.getPlayerName() + " §7hat §ckeine §7Mute-History§8.").build());
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

    @SuppressWarnings("deprecation")
    private String displayMuteDuration(long duration) {
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
