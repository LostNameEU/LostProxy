package eu.lostname.lostproxy.listener;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerDisconnectListener implements Listener {

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player.hasPermission("lostproxy.notify")) {
            LostProxy.getInstance().getTeamManager().disableNotifications(player);
        }

        if (player.hasPermission("lostproxy.command.team") && player.hasPermission("lostproxy.command.team.logout")) {
            if (LostProxy.getInstance().getTeamManager().logout(player)) {
                IPlayerSync iPlayer = new IPlayerSync(player.getUniqueId());
                LostProxy.getInstance().getTeamManager().getLoggedIn().forEach(all -> all.sendMessage(new MessageBuilder(Prefix.TMS + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §7hat das Netzwerk §cverlassen§8.").build()));
            }
        }
    }
}
