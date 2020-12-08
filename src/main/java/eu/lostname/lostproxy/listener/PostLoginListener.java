package eu.lostname.lostproxy.listener;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLoginListener implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player.hasPermission("lostproxy.notify")) {
            LostProxy.getInstance().getTeamManager().enableNotifications(player, aBoolean -> {
                if (aBoolean) {
                    player.sendMessage(new MessageBuilder(Prefix.NOTIFICATIONS + "§a✔").build());
                } else {
                    player.sendMessage(new MessageBuilder(Prefix.NOTIFICATIONS + "§7Es ist ein §4Fehler §7aufgetreten§8. §7Bitte wende dich an den zuständigen §eSachbearbeiter§8.").build());
                }
            });
        }
    }
}