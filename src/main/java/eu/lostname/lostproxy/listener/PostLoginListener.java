package eu.lostname.lostproxy.listener;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IUnbanEntry;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLoginListener implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        LostProxy.getInstance().getBanManager().getBan(player.getUniqueId().toString(), iBan -> {
            if (iBan != null && iBan.getEnd() < System.currentTimeMillis()) {
                LostProxy.getInstance().getBanManager().deleteBan(iBan, (deleteResult, throwable) -> {
                    if (deleteResult.wasAcknowledged()) {
                        LostProxy.getInstance().getHistoryManager().getBanHistory(player.getUniqueId().toString(), iBanHistory -> {
                            iBanHistory.addEntry(new IUnbanEntry(player.getUniqueId().toString(), "BKM-System", System.currentTimeMillis(), "BAN_EXPIRED_ON_JOIN"));
                            LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory, aBoolean -> {
                                if (aBoolean) {
                                    LostProxy.getInstance().getTeamManager().getNotificationOn().forEach(all -> LostProxy.getInstance().getPlayerManager().getIPlayerAsync(player.getUniqueId(), iPlayer -> {
                                        all.sendMessage(new MessageBuilder(Prefix.BKMS + "§4BKM-System" + " §8➼ " + iPlayer.getPrefix() + player.getName()).build());
                                        all.sendMessage(new MessageBuilder("§8┃ §7Typ §8» §aUnban").build());
                                        all.sendMessage(new MessageBuilder("§8┃ §7Grund §8» §eAbgelaufen beim Beitritt").build());
                                        all.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                    }));
                                } else {
                                    player.sendMessage(new MessageBuilder(Prefix.TMS + "§7Es ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort den §4Support§8!").build());
                                }
                            });
                        });
                    }
                });
            }
        });

        if (player.hasPermission("lostproxy.notify")) {
            LostProxy.getInstance().getTeamManager().enableNotifications(player, aBoolean -> {
                if (aBoolean) {
                    player.sendMessage(new MessageBuilder(Prefix.NOTIFICATIONS + "§a✔").build());
                } else {
                    player.sendMessage(new MessageBuilder(Prefix.NOTIFICATIONS + "Beim Hinzufügen des §eSpielers §7zur §eListe §7ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                }
            });
        }

        if (player.hasPermission("lostproxy.command.team") && player.hasPermission("lostproxy.command.team.login")) {
            LostProxy.getInstance().getTeamManager().login(player, aBoolean -> {
                if (aBoolean) {
                    player.sendMessage(new MessageBuilder(Prefix.TMS + "§a✔").build());

                    LostProxy.getInstance().getPlayerManager().getIPlayerAsync(player.getUniqueId(), iPlayer -> LostProxy.getInstance().getTeamManager().getLoggedIn().forEach(all -> all.sendMessage(new MessageBuilder(Prefix.TMS + iPlayer.getPrefix() + iPlayer.getPlayerName() + " §7hat das Netzwerk §abetreten§8.").build())));
                } else {
                    player.sendMessage(new MessageBuilder(Prefix.TMS + "§7Es ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                }
            });
        }
    }
}