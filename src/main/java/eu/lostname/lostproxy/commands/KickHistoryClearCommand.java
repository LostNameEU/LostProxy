/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 02.01.2021 @ 23:28:39
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * KickHistoryClearCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.historyandentries.kick.IKickHistory;
import eu.lostname.lostproxy.utils.MongoCollection;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.UUID;

public class KickHistoryClearCommand extends Command implements TabExecutor {

    public KickHistoryClearCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c§l/khclear§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/khclear §l<Spieler> §8» §7Leert die Kick-History des angegebenen Spielers").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/khclear ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (targetUUID != null) {
                IPlayerSync targetiPlayer = new IPlayerSync(targetUUID);
                IKickHistory iKickHistory = LostProxy.getInstance().getHistoryManager().getKickHistory(targetUUID);
                if (iKickHistory.getHistory().size() > 0) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Möchtest du wirklich die §eKick-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §clöschen§8?").build());
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "§7[§a§lKlick§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/khclear " + strings[0] + " confirmed").build());
                    if (!LostProxy.getInstance().getHistoryManager().getKickHistoryClearCommandProcess().contains(commandSender.getName())) {
                        LostProxy.getInstance().getHistoryManager().getKickHistoryClearCommandProcess().add(commandSender.getName());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die §eKick-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §7ist §cleer§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler konnte §cnicht §7gefunden werden§8.").build());
            }
        } else if (strings.length == 2) {
            if (strings[1].equalsIgnoreCase("confirmed")) {
                if (LostProxy.getInstance().getHistoryManager().getKickHistoryClearCommandProcess().contains(commandSender.getName())) {
                    UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
                    if (targetUUID != null) {
                        IPlayerSync targetiPlayer = new IPlayerSync(targetUUID);
                        IKickHistory iKickHistory = LostProxy.getInstance().getHistoryManager().getKickHistory(targetUUID);
                        LostProxy.getInstance().getHistoryManager().getKickHistoryClearCommandProcess().remove(commandSender.getName());
                        if (iKickHistory.getHistory().size() > 0) {
                            iKickHistory.getHistory().clear();
                            LostProxy.getInstance().getHistoryManager().saveKickHistory(iKickHistory);
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §aerfolgreich §7die §eKick-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §cgelöscht§8.").build());
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die §eKick-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §7ist §cleer§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler konnte §cnicht §7gefunden werden§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §eVerifizierung §7für diesen §eProzess §7beantragt§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.KICK_HISTORIES).find().forEach(one -> {
                IKickHistory iKickHistory = LostProxy.getInstance().getGson().fromJson(one.toJson(), IKickHistory.class);
                if (iKickHistory.getHistory().size() > 0) {
                    IPlayerSync iPlayer = new IPlayerSync(iKickHistory.getUniqueId());
                    if (iPlayer.getPlayerName().toLowerCase().startsWith(strings[0].toLowerCase()))
                        list.add(iPlayer.getPlayerName());
                }
            });
        }
        return list;
    }
}
