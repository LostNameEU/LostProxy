package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.EBanEntryType;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class UnbanCommand extends Command {

    public UnbanCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length <= 1) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/unban§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/unban <Spieler> [Grund] §8» §7Entbannt einen gebannten Spieler").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/unban ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else {
            UUID uuid = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (uuid != null) {
                IPlayerSync targetIPlayer = new IPlayerSync(uuid);
                IBan iBan = LostProxy.getInstance().getBanManager().getBan(uuid);
                if (iBan != null) {
                    LostProxy.getInstance().getBanManager().deleteBan(iBan);
                    IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(uuid);
                    String reason = LostProxy.getInstance().formatArrayToString(1, strings);
                    String invokerId = (commandSender instanceof ProxiedPlayer ? ((ProxiedPlayer) commandSender).getUniqueId().toString() : "console");

                    iBanHistory.addEntry(new IBanEntry(EBanEntryType.UNBAN_ENTRY, uuid, invokerId, System.currentTimeMillis(), reason, 0, 0));
                    LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory);
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7wurde §aentbannt§8.").build());
                    LostProxy.getInstance().getTeamManager().sendUnbanNotify(commandSender instanceof ProxiedPlayer ? new IPlayerSync(((ProxiedPlayer) commandSender).getUniqueId()).getPrefix() + commandSender.getName() : "§4Konsole", targetIPlayer.getDisplay() + targetIPlayer.getPlayerName(), reason);
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7ist §cnicht §7gebannt§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
            }
        }
    }
}