/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 18.01.2021 @ 23:14:55
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * EACommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.EBanEntryType;
import eu.lostname.lostproxy.enums.ELocale;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanEntry;
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
import java.util.concurrent.TimeUnit;

public class EACommand extends Command implements TabExecutor {
    public EACommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        ELocale locale = ELocale.GERMAN;
        if (commandSender instanceof ProxiedPlayer) {
            locale = LostProxy.getInstance().getLocaleManager().getLocaleData(((ProxiedPlayer) commandSender)).getLocale();
        }

        if (strings.length != 1) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.usage").replaceAll("%cmd%", "§c/ea")).build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/ea <" + locale.getMessage("player") + "> §8" + Prefix.DASH + " §7" + locale.getMessage("eacommand.usage.description")).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ea ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else {
            UUID uuid = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (uuid != null) {
                IPlayerSync iPlayer = new IPlayerSync(uuid);
                IBan iBan = LostProxy.getInstance().getBanManager().getBan(uuid);
                if (iBan != null) {
                    if (iBan.getBanAppeal() == null) {
                        if (iBan.getDuration() != -1) {
                            if ((iBan.getEnd() - System.currentTimeMillis()) >= TimeUnit.DAYS.toMillis(3)) {
                                iBan.setEnd(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(3));

                                IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(uuid);
                                iBanHistory.addEntry(new IBanEntry(EBanEntryType.BAN_APPEAL_ENTRY, uuid, (commandSender instanceof ProxiedPlayer ? ((ProxiedPlayer) commandSender).getUniqueId().toString() : "console"), System.currentTimeMillis(), "EA-Command", 0, null, 0));

                                LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory);
                                LostProxy.getInstance().getBanManager().saveBan(iBan);
                                if (commandSender instanceof ProxiedPlayer) {
                                    IPlayerSync invoker = new IPlayerSync(((ProxiedPlayer) commandSender).getUniqueId());
                                    LostProxy.getInstance().getTeamManager().sendEANotify(invoker.getDisplay() + invoker.getPlayerName(), iPlayer.getDisplay() + iPlayer.getPlayerName());
                                } else {
                                    LostProxy.getInstance().getTeamManager().sendEANotify("§4System", iPlayer.getDisplay() + iPlayer.getPlayerName());
                                }

                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("eacommand.successfully").replaceAll("%player%", iPlayer.getDisplay() + iPlayer.getPlayerName())).build());
                            } else {
                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("eacommand.too_short")).build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("eacommand.is_permanent")).build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("eacommand.already_shorten").replaceAll("%player%", iPlayer.getDisplay() + iPlayer.getPlayerName())).build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("player.not_banned").replaceAll("%player%", iPlayer.getDisplay() + iPlayer.getPlayerName())).build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("player.not_banned")).build());
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).find().forEach(one -> {
                IBan iBan = LostProxy.getInstance().getGson().fromJson(one.toJson(), IBan.class);
                if (iBan.getDuration() != -1) {
                    IPlayerSync iPlayer = new IPlayerSync(iBan.getUniqueId());

                    if (iPlayer.getPlayerName().toLowerCase().startsWith(strings[0].toLowerCase()))
                        list.add(iPlayer.getPlayerName());
                }
            });
        }
        return list;
    }
}
