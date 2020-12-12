package eu.lostname.lostproxy.listener;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
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
            LostProxy.getInstance().getTeamManager().disableNotifications(player, aBoolean -> {
            });
        }

        if (player.hasPermission("lostproxy.command.team") && player.hasPermission("lostproxy.command.team.logout")) {
            LostProxy.getInstance().getTeamManager().logout(player, aBoolean -> {
                if (aBoolean)
                    LostProxy.getInstance().getPlayerManager().getIPlayerAsync(player.getUniqueId(), iPlayer -> {
                        LostProxy.getInstance().getTeamManager().getLoggedIn().forEach(all -> all.sendMessage(new MessageBuilder(Prefix.TMS + iPlayer.getPrefix() + iPlayer.getPlayerName() + " §7hat das Netzwerk §cverlassen§8.").build()));
                    });
            });
        }
    }
}
