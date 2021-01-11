/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 11.01.2021 @ 22:23:16
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * BroadcastCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

public class BroadcastCommand extends Command {

    public BroadcastCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BROADCAST + "Benutzung von §e/broadcast§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8» §e/broadcast [Nachricht] §8" + Prefix.DASH + " §7Sende eine Durchsage an alle Spieler, die sich aktuell auf dem Netzwerk befinden").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else {
            String message = LostProxy.getInstance().formatArrayToString(0, strings).replaceAll("&", "§");

            ProxyServer.getInstance().broadcast(new MessageBuilder("").build());
            ProxyServer.getInstance().broadcast(new MessageBuilder(Prefix.BROADCAST + message).build());
            ProxyServer.getInstance().broadcast(new MessageBuilder("").build());
        }
    }
}
