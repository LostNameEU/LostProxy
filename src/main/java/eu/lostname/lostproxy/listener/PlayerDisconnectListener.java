package eu.lostname.lostproxy.listener;

import eu.lostname.lostproxy.LostProxy;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerDisconnectListener implements Listener {

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player.hasPermission("lostproxy.notify")) {
            LostProxy.getInstance().getTeamManager().disableNotifications(player, aBoolean -> {
            });
        }
    }
}
