package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.ETimeUnit;
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

public class MuteHistoryCommand extends Command {

    public MuteHistoryCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length != 1) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/mutehistory§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/mutehistory <Spieler> §8» §7Listet die gesamte Mutehistory des angegebenen Spielers an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/muteinfo ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else {
            UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (targetUUID != null) {
                IMuteHistory iMuteHistory = LostProxy.getInstance().getHistoryManager().getMuteHistory(targetUUID);
                IPlayerSync targetIPlayer = new IPlayerSync(targetUUID);
                if (iMuteHistory.getHistory().size() > 0) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Mutes von " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + "§8:").build());
                    commandSender.sendMessage(new MessageBuilder("§8┃ §7Anzahl §8» §c" + iMuteHistory.getHistory().size()).build());
                    AtomicInteger currentEntry = new AtomicInteger(-1);

                    iMuteHistory.getHistory().forEach(iMuteEntry -> {

                        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iMuteEntry.getTimestamp()));
                        String time = new SimpleDateFormat("HH:mm:ss").format(new Date(iMuteEntry.getTimestamp()));

                        switch (iMuteEntry.getEMuteEntryType()) {
                            case MUTE_ENTRY:

                                String unmuteDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iMuteEntry.getEnd()));
                                String unmuteTime = new SimpleDateFormat("HH:mm:ss").format(new Date(iMuteEntry.getEnd()));

                                boolean muteIsPermanent = iMuteEntry.getTime() == -1;
                                if (iMuteEntry.isInvokerConsole()) {
                                    commandSender.sendMessage(new MessageBuilder("§8┃ §cMute §8» §e" + date + " §7@ §e" + time + " §8» §4Konsole §8» §e" + iMuteEntry.getReason() + " §8» §c" + (muteIsPermanent ? "permanent" : iMuteEntry.getTime() + " " + ETimeUnit.getDisplayName(iMuteEntry.getTime(), iMuteEntry.getETimeUnit())) + " §8» §a" + (muteIsPermanent ? "/" : unmuteDate + " §7@ §a" + unmuteTime)).build());
                                } else {
                                    IPlayerSync iPlayer = new IPlayerSync(UUID.fromString(iMuteEntry.getInvokerId()));
                                    commandSender.sendMessage(new MessageBuilder("§8┃ §cMute §8» §e" + date + " §7@ §e" + time + " §8» " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §8» §e" + iMuteEntry.getReason() + " §8» §c" + (muteIsPermanent ? "permanent" : iMuteEntry.getTime() + " " + ETimeUnit.getDisplayName(iMuteEntry.getTime(), iMuteEntry.getETimeUnit())) + " §8» §a" + (muteIsPermanent ? "/" : unmuteDate + " §7@ §a" + unmuteTime)).build());
                                }
                                break;
                            case UNMUTE_ENTRY:
                                if (iMuteEntry.isInvokerConsole()) {
                                    commandSender.sendMessage(new MessageBuilder("§8┃ §aUnmute §8» §e" + date + " §7@ §e" + time + " §8» §4Konsole §8» §e" + iMuteEntry.getReason()).build());
                                } else {
                                    IPlayerSync iPlayer = new IPlayerSync(UUID.fromString(iMuteEntry.getInvokerId()));
                                    commandSender.sendMessage(new MessageBuilder("§8┃ §aUnmute §8» §e" + date + " §7@ §e" + time + " §8» " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §8» §e" + iMuteEntry.getReason()).build());
                                }
                                break;
                        }

                        currentEntry.set(currentEntry.get() + 1);

                        if (iMuteHistory.getHistory().size() == currentEntry.get()) {
                            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                        }
                    });
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7hat §ckeine §7Mute-History§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Zu dem angegebenen Spielernamen konnte §ckeine §7UUID gefunden werden§8.").build());
            }
        }
    }
}
