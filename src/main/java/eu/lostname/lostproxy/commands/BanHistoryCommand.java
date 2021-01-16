/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 16.01.2021 @ 22:29:30
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * BanHistoryCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.ELocale;
import eu.lostname.lostproxy.enums.ETimeUnit;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.utils.MongoCollection;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class BanHistoryCommand extends Command implements TabExecutor {

    public BanHistoryCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        ELocale locale = ELocale.GERMAN;
        if (commandSender instanceof ProxiedPlayer) {
            locale = LostProxy.getInstance().getLocaleManager().getLocaleData(((ProxiedPlayer) commandSender)).getLocale();
        }
        if (strings.length != 1) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.usage").replaceAll("%cmd%", "§c/banhistory")).build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/banhistory <" + locale.getMessage("player") + "> §8" + Prefix.DASH + " §7" + locale.getMessage("banhistorycommand.usage.description")).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/baninfo ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else {
            UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (targetUUID != null) {
                IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(targetUUID);
                IPlayerSync targetIPlayer = new IPlayerSync(targetUUID);
                if (iBanHistory.getHistory().size() > 0) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banhistorycommand.history.title").replaceAll("%player%", targetIPlayer.getDisplay() + targetIPlayer.getPlayerName())).build());
                    commandSender.sendMessage(new MessageBuilder("§8» §7" + locale.getMessage("amount") + " §8" + Prefix.DASH + " §c" + iBanHistory.getHistory().size()).build());
                    AtomicInteger currentEntry = new AtomicInteger(-1);

                    ELocale finalLocale = locale;
                    iBanHistory.getHistory().forEach(iBanEntry -> {

                        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iBanEntry.getTimestamp()));
                        String time = new SimpleDateFormat("HH:mm:ss").format(new Date(iBanEntry.getTimestamp()));

                        switch (iBanEntry.getEBanEntryType()) {
                            case BAN_ENTRY:

                                String unbanDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iBanEntry.getEnd()));
                                String unbanTime = new SimpleDateFormat("HH:mm:ss").format(new Date(iBanEntry.getEnd()));

                                boolean banIsPermanent = iBanEntry.getTime() == -1;
                                if (iBanEntry.isInvokerConsole()) {
                                    commandSender.sendMessage(new MessageBuilder("§8» §c" + finalLocale.getMessage("ban") + " §8" + Prefix.DASH + " §e" + date + " §7@ §e" + time + " §8» §4" + finalLocale.getMessage("system") + " §8» §e" + iBanEntry.getReason() + " §8» §c" + (banIsPermanent ? finalLocale.getMessage("permanent") : iBanEntry.getTime() + " " + ETimeUnit.getDisplayName(iBanEntry.getTime(), iBanEntry.getETimeUnit(), finalLocale)) + " §8» §a" + (banIsPermanent ? "/" : unbanDate + " §7@ §a" + unbanTime)).build());
                                } else {
                                    IPlayerSync iPlayer = new IPlayerSync(UUID.fromString(iBanEntry.getInvokerId()));
                                    commandSender.sendMessage(new MessageBuilder("§8» §c" + finalLocale.getMessage("ban") + " §8" + Prefix.DASH + " §e" + date + " §7@ §e" + time + " §8» " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §8» §e" + iBanEntry.getReason() + " §8» §c" + (banIsPermanent ? "permanent" : iBanEntry.getTime() + " " + ETimeUnit.getDisplayName(iBanEntry.getTime(), iBanEntry.getETimeUnit(), finalLocale)) + " §8» §a" + (banIsPermanent ? "/" : unbanDate + " §7@ §a" + unbanTime)).build());
                                }
                                break;
                            case UNBAN_ENTRY:
                                if (iBanEntry.isInvokerConsole()) {
                                    commandSender.sendMessage(new MessageBuilder("§8» §a" + finalLocale.getMessage("unban") + " §8" + Prefix.DASH + " §e" + date + " §7@ §e" + time + " §8» §4" + finalLocale.getMessage("system") + " §8» §e" + iBanEntry.getReason()).build());
                                } else {
                                    IPlayerSync iPlayer = new IPlayerSync(UUID.fromString(iBanEntry.getInvokerId()));
                                    commandSender.sendMessage(new MessageBuilder("§8» §a" + finalLocale.getMessage("unban") + " §8" + Prefix.DASH + " §e" + date + " §7@ §e" + time + " §8» " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §8» §e" + iBanEntry.getReason()).build());
                                }
                                break;
                            case BAN_APPEAL_ENTRY:
                                if (iBanEntry.isInvokerConsole()) {
                                    commandSender.sendMessage(new MessageBuilder("§8» §eEA §8" + Prefix.DASH + " §e" + date + " §7@ §e" + time + " §8» §4" + finalLocale.getMessage("system") + " §8» §e" + iBanEntry.getReason()).build());
                                } else {
                                    IPlayerSync iPlayer = new IPlayerSync(UUID.fromString(iBanEntry.getInvokerId()));
                                    commandSender.sendMessage(new MessageBuilder("§8» §eEA §8" + Prefix.DASH + " §e" + date + " §7@ §e" + time + " §8» " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §8» §e" + iBanEntry.getReason()).build());
                                }
                                break;
                        }

                        currentEntry.set(currentEntry.get() + 1);

                        if (iBanHistory.getHistory().size() == currentEntry.get()) {
                            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                        }
                    });
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banhistorycommand.history.empty")).build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("no_uuid_found")).build());
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.BAN_HISTORIES).find().forEach(all -> {
                IPlayerSync iPlayer = new IPlayerSync(UUID.fromString(all.getString("_id")));

                if (iPlayer.getPlayerName().toLowerCase().startsWith(strings[0].toLowerCase())) {
                    list.add(iPlayer.getPlayerName());
                }
            });
        }
        return list;
    }
}
