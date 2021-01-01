/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:40:04
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * PlayerDisconnectListener.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.listener;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IFriendData;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class PlayerDisconnectListener implements Listener {

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player.hasPermission("lostproxy.notify")) {
            LostProxy.getInstance().getTeamManager().disableNotifications(player);
        }

        IPlayerSync iPlayer = new IPlayerSync(player.getUniqueId());
        if (player.hasPermission("lostproxy.command.team") && player.hasPermission("lostproxy.command.team.logout")) {
            if (LostProxy.getInstance().getTeamManager().logout(player)) {
                LostProxy.getInstance().getTeamManager().getLoggedIn().forEach(all -> all.sendMessage(new MessageBuilder(Prefix.TMS + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §7hat das Netzwerk §cverlassen§8.").build()));
            }
        }

        IFriendData iFriendData = LostProxy.getInstance().getFriendManager().getFriendData(player.getUniqueId());
        iFriendData.setLastLogoutTimestamp(System.currentTimeMillis());
        iFriendData.save();

        if (iFriendData.canFriendsSeeOnlineStatusAllowed()) {
            iFriendData.getFriends().keySet().stream().filter(filter -> new IPlayerSync(UUID.fromString(filter)).isOnline()).forEach(one -> {
                UUID friendUUID = UUID.fromString(one);
                if (LostProxy.getInstance().getFriendManager().getFriendData(friendUUID).areNotifyMessagesEnabled())
                    ProxyServer.getInstance().getPlayer(friendUUID).sendMessage(new MessageBuilder(Prefix.FRIENDS + iPlayer.getDisplay() + player.getName() + " §7ist nun §coffline§8.").build());
            });
        }
    }
}
