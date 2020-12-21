package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.historyandentries.mute.IMuteHistory;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MuteInfoCommand extends Command {

    public MuteInfoCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length != 1) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/muteinfo§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/muteinfo <Spieler> §8» §7Listet die gesamte Mutehistory des angegebenen Spielers an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/muteinfo ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else {
            UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (targetUUID != null) {
                IMuteHistory iMuteHistory = LostProxy.getInstance().getHistoryManager().getMuteHistory(targetUUID);
                IPlayerSync targetIPlayer = new IPlayerSync(targetUUID);
                if (iMuteHistory.getHistory().size() > 0) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Mutes von " + targetIPlayer.getPrefix() + targetIPlayer.getPlayerName() + "§8:").build());
                    commandSender.sendMessage(new MessageBuilder("§8┃ §7Anzahl §8» §c" + iMuteHistory.getHistory().size()).build());
                    AtomicInteger currentEntry = new AtomicInteger(-1);

                    iMuteHistory.getHistory().forEach(iMuteEntry -> {

                        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iMuteEntry.getTimestamp()));
                        String time = new SimpleDateFormat("HH:mm:ss").format(new Date(iMuteEntry.getTimestamp()));

                        switch (iMuteEntry.getEMuteEntryType()) {
                            case MUTE_ENTRY:

                                String unmuteDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iMuteEntry.getEnd()));
                                String unmuteTime = new SimpleDateFormat("HH:mm:ss").format(new Date(iMuteEntry.getEnd()));

                                if (iMuteEntry.isInvokerConsole()) {
                                    commandSender.sendMessage(new MessageBuilder("§8┃ §cMute §8» §e" + date + " §7@ §e" + time + " §8» §4Konsole §8» §e" + iMuteEntry.getReason() + " §8» §c" + calculateRemainingTime(iMuteEntry.getDuration()) + " §8» §a" + (iMuteEntry.getDuration() == -1 ? "/" : unmuteDate + " §7@ §a" + unmuteTime)).build());
                                } else {
                                    IPlayerSync iPlayer = new IPlayerSync(UUID.fromString(iMuteEntry.getInvokerId()));
                                    commandSender.sendMessage(new MessageBuilder("§8┃ §cMute §8» §e" + date + " §7@ §e" + time + " §8» " + iPlayer.getPrefix() + iPlayer.getPlayerName() + " §8» §e" + iMuteEntry.getReason() + " §8» §c" + (iMuteEntry.getDuration() == -1 ? "permanent" : calculateRemainingTime(iMuteEntry.getDuration())) + " §8» §a" + (iMuteEntry.getDuration() == -1 ? "/" : unmuteDate + " §7@ §a" + unmuteTime)).build());
                                }
                                break;
                            case UNMUTE_ENTRY:
                                if (iMuteEntry.isInvokerConsole()) {
                                    commandSender.sendMessage(new MessageBuilder("§8┃ §aUnmute §8» §e" + date + " §7@ §e" + time + " §8» §4Konsole §8» §e" + iMuteEntry.getReason()).build());
                                } else {
                                    IPlayerSync iPlayer = new IPlayerSync(UUID.fromString(iMuteEntry.getInvokerId()));
                                    commandSender.sendMessage(new MessageBuilder("§8┃ §aUnmute §8» §e" + date + " §7@ §e" + time + " §8» " + iPlayer.getPrefix() + iPlayer.getPlayerName() + " §8» §e" + iMuteEntry.getReason()).build());
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

}
