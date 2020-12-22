package eu.lostname.lostproxy.listener;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.EBanEntryType;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLoginListener implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        IPlayerSync iPlayer = new IPlayerSync(player.getUniqueId());

        IBan iBan = LostProxy.getInstance().getBanManager().getBan(player.getUniqueId());
        if (iBan != null && iBan.getEnd() < System.currentTimeMillis()) {
            LostProxy.getInstance().getBanManager().deleteBan(iBan);
            IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(player.getUniqueId());
            iBanHistory.addEntry(new IBanEntry(EBanEntryType.UNBAN_ENTRY, player.getUniqueId(), "console", System.currentTimeMillis(), "BAN_EXPIRED", 0, 0));
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
    }
}