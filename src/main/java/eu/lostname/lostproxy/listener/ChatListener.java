package eu.lostname.lostproxy.listener;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.bkms.IMute;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.text.SimpleDateFormat;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(ChatEvent event) {
        if (event.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) event.getSender();

            if (!player.hasPermission("lostproxy.bkms.bypasschat")) {
                IMute iMute = LostProxy.getInstance().getMuteManager().getMute(player.getUniqueId());
                if (iMute != null) {
                    event.setCancelled(true);

                    if (iMute.getDuration() == -1) {
                        player.sendMessage(new MessageBuilder(Prefix.BKMS + "Du bist §4permanent §7gemutet§8.").build());
                        player.sendMessage(new MessageBuilder("§8┃ §7Grund §8» §c" + iMute.getReason()).build());
                        player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                    } else {
                        player.sendMessage(new MessageBuilder(Prefix.BKMS + "Du bist §ctemporär §7gemutet§8.").build());
                        player.sendMessage(new MessageBuilder("§8┃ §7Grund §8» §c" + iMute.getReason()).build());
                        player.sendMessage(new MessageBuilder("§8┃ §7Verleibende Zeit §8» §c" + LostProxy.getInstance().getMuteManager().calculateRemainingTime(iMute.getEnd())).build());
                        player.sendMessage(new MessageBuilder("§8┃ §7Läuft ab am §8» §c" + new SimpleDateFormat("dd.MM.yyyy").format(iMute.getEnd()) + " §7um §c" + new SimpleDateFormat("HH:mm:ss").format(iMute.getEnd()) + " §7Uhr").build());
                        player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                    }
                }
            }
        }
    }
}
