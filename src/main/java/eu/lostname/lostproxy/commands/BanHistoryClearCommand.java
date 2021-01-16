/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 16.01.2021 @ 22:29:30
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * BanHistoryClearCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.ELocale;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.utils.MongoCollection;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.UUID;

public class BanHistoryClearCommand extends Command implements TabExecutor {

    public BanHistoryClearCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        ELocale locale = ELocale.GERMAN;
        if (commandSender instanceof ProxiedPlayer) {
            locale = LostProxy.getInstance().getLocaleManager().getLocaleData(((ProxiedPlayer) commandSender)).getLocale();
        }

        if (strings.length == 0 || strings.length >= 3) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.usage").replaceAll("%cmd%", "§c/bhclear")).build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/bhclear <" + locale.getMessage("player") + "> §8" + Prefix.DASH + " §7" + locale.getMessage("banhistoryclearcommand.usage.description")).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/bhclear ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (targetUUID != null) {
                IPlayerSync targetiPlayer = new IPlayerSync(targetUUID);
                IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(targetUUID);
                if (iBanHistory.getHistory().size() > 0) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banhistoryclearcommand.verify.request").replaceAll("%player%", targetiPlayer.getDisplay() + targetiPlayer.getPlayerName())).build());
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "§7[§a" + locale.getMessage("click") + "§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/bhclear " + strings[0] + " confirmed").build());
                    if (!LostProxy.getInstance().getHistoryManager().getBanHistoryClearCommandProcess().contains(commandSender.getName())) {
                        LostProxy.getInstance().getHistoryManager().getBanHistoryClearCommandProcess().add(commandSender.getName());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banhistoryclearcommand.banhistory.empty").replaceAll("%player%", targetiPlayer.getDisplay() + targetiPlayer.getPlayerName())).build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("player_not_found")).build());
            }
        } else {
            if (strings[1].equalsIgnoreCase("confirmed")) {
                if (LostProxy.getInstance().getHistoryManager().getBanHistoryClearCommandProcess().contains(commandSender.getName())) {
                    UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
                    if (targetUUID != null) {
                        IPlayerSync targetiPlayer = new IPlayerSync(targetUUID);
                        IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(targetUUID);
                        LostProxy.getInstance().getHistoryManager().getBanHistoryClearCommandProcess().remove(commandSender.getName());
                        if (iBanHistory.getHistory().size() > 0) {
                            iBanHistory.getHistory().clear();
                            LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory);
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banhistoryclearcommand.banhistory.cleared").replaceAll("%player%", targetiPlayer.getDisplay() + targetiPlayer.getPlayerName())).build());
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banhistoryclearcommand.banhistory.empty").replaceAll("%player%", targetiPlayer.getDisplay() + targetiPlayer.getPlayerName())).build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("player_not_found")).build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("historyclearcommands.verify.not_requested")).build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.see_usage")).build());
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.BAN_HISTORIES).find().forEach(one -> {
                IBanHistory iBanHistory = LostProxy.getInstance().getGson().fromJson(one.toJson(), IBanHistory.class);
                if (iBanHistory.getHistory().size() > 0) {
                    IPlayerSync iPlayer = new IPlayerSync(iBanHistory.getUniqueId());
                    if (iPlayer.getPlayerName().toLowerCase().startsWith(strings[0].toLowerCase()))
                        list.add(iPlayer.getPlayerName());
                }
            });
        }
        return list;
    }
}
