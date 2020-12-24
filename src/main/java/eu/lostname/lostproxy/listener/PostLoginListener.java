package eu.lostname.lostproxy.listener;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.EBanEntryType;
import eu.lostname.lostproxy.interfaces.IFriendData;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PostLoginListener implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        IPlayerSync iPlayer = new IPlayerSync(player.getUniqueId());

        IBan iBan = LostProxy.getInstance().getBanManager().getBan(player.getUniqueId());
        if (iBan != null && iBan.getEnd() < System.currentTimeMillis()) {
            LostProxy.getInstance().getBanManager().deleteBan(iBan);
            IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(player.getUniqueId());
            iBanHistory.addEntry(new IBanEntry(EBanEntryType.UNBAN_ENTRY, player.getUniqueId(), "console", System.currentTimeMillis(), "BAN_EXPIRED", 0, null, 0));
            LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory);
            LostProxy.getInstance().getTeamManager().getNotificationOn().forEach(all -> {
                all.sendMessage(new MessageBuilder(Prefix.BKMS + "§4BKM-System" + " §8➼ " + iPlayer.getDisplay() + player.getName()).build());
                all.sendMessage(new MessageBuilder("§8┃ §7Typ §8» §aUnban").build());
                all.sendMessage(new MessageBuilder("§8┃ §7Grund §8» §eAbgelaufen").build());
                all.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
            });
        }

        if (player.hasPermission("lostproxy.notify")) {
            if (LostProxy.getInstance().getTeamManager().enableNotifications(player)) {
                player.sendMessage(new MessageBuilder(Prefix.NOTIFICATIONS + "§a✔").build());
            }
        }

        if (player.hasPermission("lostproxy.command.team") && player.hasPermission("lostproxy.command.team.login")) {
            if (LostProxy.getInstance().getTeamManager().login(player)) {
                player.sendMessage(new MessageBuilder(Prefix.TMS + "§a✔").build());
                LostProxy.getInstance().getTeamManager().getLoggedIn().forEach(all -> all.sendMessage(new MessageBuilder(Prefix.TMS + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §7hat das Netzwerk §abetreten§8.").build()));
            }
        }

        IFriendData iFriendData = LostProxy.getInstance().getFriendManager().getFriendData(player.getUniqueId());
        final List<String> onlineFriends = iFriendData.getFriends().keySet().stream().filter(filter -> new IPlayerSync(UUID.fromString(filter)).isOnline()).collect(Collectors.toList());

        if (onlineFriends.size() > 0) {
            if (iFriendData.canFriendsSeeOnlineStatusAllowed())
                onlineFriends.forEach(one -> {
                    UUID friendUUID = UUID.fromString(one);
                    if (LostProxy.getInstance().getFriendManager().getFriendData(friendUUID).areNotifyMessagesEnabled())
                        ProxyServer.getInstance().getPlayer(friendUUID).sendMessage(new MessageBuilder(Prefix.FRIENDS + iPlayer.getDisplay() + player.getName() + " §7ist nun §aonline§8.").build());
                });


            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Zurzeit " + (onlineFriends.size() == 1 ? "ist" : "sind") + " §e" + onlineFriends.size() + " §7" + (onlineFriends.size() == 1 ? "Freund" : "Freunde") + " online§8.").build());
        }
    }
}