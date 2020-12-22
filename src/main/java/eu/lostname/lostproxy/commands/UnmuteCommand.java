package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.EMuteEntryType;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.bkms.IMute;
import eu.lostname.lostproxy.interfaces.historyandentries.mute.IMuteEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.mute.IMuteHistory;
import eu.lostname.lostproxy.utils.MongoCollection;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.UUID;

public class UnmuteCommand extends Command implements TabExecutor {

    public UnmuteCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length <= 1) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/unmute§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/unmute <Spieler> [Grund] §8» §7Entmutet einen gemuteten Spieler").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/unmute ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else {
            UUID uuid = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (uuid != null) {
                IPlayerSync targetIPlayer = new IPlayerSync(uuid);
                IMute iMute = LostProxy.getInstance().getMuteManager().getMute(uuid);
                if (iMute != null) {
                    LostProxy.getInstance().getMuteManager().deleteMute(iMute);
                    IMuteHistory iMuteHistory = LostProxy.getInstance().getHistoryManager().getMuteHistory(uuid);
                    String reason = LostProxy.getInstance().formatArrayToString(1, strings);
                    String invokerId = (commandSender instanceof ProxiedPlayer ? ((ProxiedPlayer) commandSender).getUniqueId().toString() : "console");

                    iMuteHistory.addEntry(new IMuteEntry(EMuteEntryType.UNMUTE_ENTRY, uuid, invokerId, System.currentTimeMillis(), reason, 0, 0));
                    LostProxy.getInstance().getHistoryManager().saveMuteHistory(iMuteHistory);
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7wurde §aentmutet§8.").build());
                    LostProxy.getInstance().getTeamManager().sendUnmuteNotify(commandSender instanceof ProxiedPlayer ? new IPlayerSync(((ProxiedPlayer) commandSender).getUniqueId()).getDisplay() + commandSender.getName() : "§4Konsole", targetIPlayer.getDisplay() + targetIPlayer.getPlayerName(), reason);
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7ist §cnicht §7gemutet§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();

        if (strings.length == 0) {
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_MUTES).find().forEach(one -> {
                IMute iMute = LostProxy.getInstance().getGson().fromJson(one.toJson(), IMute.class);
                list.add(new IPlayerSync(iMute.getUniqueId()).getPlayerName());
            });
        }
        return list;
    }
}