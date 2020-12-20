package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.bkms.IMute;
import eu.lostname.lostproxy.interfaces.historyandentries.mute.IMuteHistory;
import eu.lostname.lostproxy.interfaces.historyandentries.mute.IUnmuteEntry;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class UnmuteCommand extends Command {

    public UnmuteCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/unmute§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/unmute <Spieler> [Grund] §8» §7Entmutet einen gebannten Spieler").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/unmute ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length >= 2) {
            UUID uuid = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (uuid != null) {
                IPlayerSync targetIPlayer = new IPlayerSync(uuid);
                IMute iMute = LostProxy.getInstance().getMuteManager().getMute(uuid);
                if (iMute != null) {
                    LostProxy.getInstance().getMuteManager().deleteMute(iMute);
                    IMuteHistory iMuteHistory = LostProxy.getInstance().getHistoryManager().getMuteHistory(uuid);
                    String reason = LostProxy.getInstance().formatArrayToString(1, strings);
                    String invokerId = (commandSender instanceof ProxiedPlayer ? ((ProxiedPlayer) commandSender).getUniqueId().toString() : "console");

                    iMuteHistory.addEntry(new IUnmuteEntry(uuid, invokerId, System.currentTimeMillis(), reason));
                    LostProxy.getInstance().getHistoryManager().saveMuteHistory(iMuteHistory);
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getPrefix() + targetIPlayer.getPlayerName() + " §7wurde §aentmutet§8.").build());
                    LostProxy.getInstance().getTeamManager().sendUnmuteNotify(invokerId, targetIPlayer.getPrefix() + targetIPlayer.getPlayerName(), reason);
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getPrefix() + targetIPlayer.getPlayerName() + " §7ist §cnicht §7gemutet§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
        }
    }
}