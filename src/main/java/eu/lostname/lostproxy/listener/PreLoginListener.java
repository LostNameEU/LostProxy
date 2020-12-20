package eu.lostname.lostproxy.listener;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.text.SimpleDateFormat;

public class PreLoginListener implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPreLogin(PreLoginEvent event) {
        LostProxy.getInstance().getBanManager().getBan(event.getConnection().getUniqueId(), iBan -> {
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
                } else if (iBan.getEnd() > System.currentTimeMillis()) {
                    ProxyServer.getInstance().getScheduler().runAsync(LostProxy.getInstance(), () -> {

                        String remainingTime = LostProxy.getInstance().getBanManager().calculateRemainingTime(iBan.getEnd());

                        event.setCancelled(true);
                        event.setCancelReason(new MessageBuilder("§6§o■§r §8┃ §c§lLostName §8● §7the new version of us §8┃ §6§o■§r \n" +
                                "\n" +
                                "§7Du bist §4temporär §7vom Netzwerk §4gebannt§8." +
                                "\n" +
                                "\n" +
                                "§7Grund §8➡ §e" + iBan.getReason() +
                                "\n" +
                                "§7Verbleibende Zeit §8➡ §c" + remainingTime +
                                "\n" +
                                "§7Läuft ab am §8➡ §c" + new SimpleDateFormat("dd.MM.yyyy").format(iBan.getEnd()) + " §7um §c" + new SimpleDateFormat("HH:mm:ss").format(iBan.getEnd()) + " §7Uhr" +
                                "\n" +
                                "§7FÜr weitere Fragen oder zum Stellen eines Entbannugsantrag besuche das Forum§8!" +
                                "\n" +
                                " §8» §cforum§8.§clostname§8.§ceu §8«" +
                                "\n" +
                                "\n" +
                                "§8§m--------------------------------------§r").build());
                    });
                }
            }
        });
    }
}
