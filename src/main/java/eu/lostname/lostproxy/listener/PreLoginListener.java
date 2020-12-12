package eu.lostname.lostproxy.listener;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PreLoginListener implements Listener {

    @EventHandler
    public void onPreLogin(PreLoginEvent event) {
        String uniqueId = event.getConnection().getUniqueId().toString();

        LostProxy.getInstance().getBanManager().getBan(uniqueId, iBan -> {
            if (iBan != null) {
                if (iBan.getEnd() == -1) {
                    event.setCancelled(true);
                    event.setCancelReason(new MessageBuilder("§6§o■§r §8┃ §c§lLostName §8● §7the new version of us §8┃ §6§o■§r \n" +
                            "\n" +
                            "§7Du bist §4§npermanent§r §7vom Netzwerk §4gebannt§8." +
                            "\n" +
                            "\n" +
                            "§7Grund §8➡ §e" + iBan.getReason() +
                            "\n" +
                            "\n" +
                            "§7FÜr weitere Fragen oder zum Stellen eines Entbannugsantrag besuche das Forum§8!" +
                            "\n" +
                            " §8» §cforum§8.§clostname§8.§ceu §8«" +
                            "\n" +
                            "\n" +
                            "§8§m--------------------------------------§r").build());
                }
            }
        });
    }
}
