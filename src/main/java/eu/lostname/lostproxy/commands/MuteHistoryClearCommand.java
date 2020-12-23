package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.historyandentries.mute.IMuteHistory;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class MuteHistoryClearCommand extends Command {

    public MuteHistoryClearCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0 || strings.length >= 3) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/mhclear§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/mhclear <Spieler> §8» §7Leert die Mute-History des angegebenen Spielers").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mhclear ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (targetUUID != null) {
                IPlayerSync targetiPlayer = new IPlayerSync(targetUUID);
                IMuteHistory iMuteHistory = LostProxy.getInstance().getHistoryManager().getMuteHistory(targetUUID);
                if (iMuteHistory.getHistory().size() > 0) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Möchtest du wirklich die §eMute-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §clöschen§8?").build());
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "§7[§a§lKlick§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/mhclear " + strings[0] + " confirmed").build());
                    if (!LostProxy.getInstance().getHistoryManager().getMuteHistoryClearCommandProcess().contains(commandSender.getName())) {
                        LostProxy.getInstance().getHistoryManager().getMuteHistoryClearCommandProcess().add(commandSender.getName());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die §eMute-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §7ist §cleer§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler konnte §cnicht §7gefunden werden§8.").build());
            }
        } else {
            if (strings[1].equalsIgnoreCase("confirmed")) {
                if (LostProxy.getInstance().getHistoryManager().getMuteHistoryClearCommandProcess().contains(commandSender.getName())) {
                    UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
                    if (targetUUID != null) {
                        IPlayerSync targetiPlayer = new IPlayerSync(targetUUID);
                        IMuteHistory iMuteHistory = LostProxy.getInstance().getHistoryManager().getMuteHistory(targetUUID);
                        LostProxy.getInstance().getHistoryManager().getMuteHistoryClearCommandProcess().remove(commandSender.getName());
                        if (iMuteHistory.getHistory().size() > 0) {
                            iMuteHistory.getHistory().clear();
                            LostProxy.getInstance().getHistoryManager().saveMuteHistory(iMuteHistory);
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §aerfolgreich §7die §eMute-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §cgelöscht§8.").build());
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die §eMute-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §7ist §cleer§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler konnte §cnicht §7gefunden werden§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §eVerifizierung §7für diesen §eProzess §7beantragt§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
            }
        }
    }
}
