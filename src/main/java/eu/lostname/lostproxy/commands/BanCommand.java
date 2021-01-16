/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 16.01.2021 @ 22:29:30
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * BanCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.EBanEntryType;
import eu.lostname.lostproxy.enums.ELocale;
import eu.lostname.lostproxy.enums.ETimeUnit;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.IReason;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import eu.lostname.lostproxy.interfaces.bkms.IBanReason;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.utils.CloudServices;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BanCommand extends Command implements TabExecutor {

    public BanCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        ELocale locale = ELocale.GERMAN;
        if (commandSender instanceof ProxiedPlayer) {
            locale = LostProxy.getInstance().getLocaleManager().getLocaleData(((ProxiedPlayer) commandSender)).getLocale();
        }

        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.usage").replaceAll("%cmd%", "/ban")).build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/ban <" + locale.getMessage("player") + "> §8" + Prefix.DASH + " §7" + locale.getMessage("bancommand.usage.description")).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ban ").build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/ban <" + locale.getMessage("player") + "> <" + locale.getMessage("id") + "> §8" + Prefix.DASH + " §7" + locale.getMessage("bancommand.usage.direct.description")).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ban ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            if (!commandSender.getName().equalsIgnoreCase(strings[0])) {
                UUID uuid = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
                if (uuid != null) {
                    IPlayerSync targetIPlayer = new IPlayerSync(uuid);
                    IBan iBan = LostProxy.getInstance().getBanManager().getBan(uuid);
                    if (iBan == null) {
                        if (commandSender.hasPermission("lostproxy.command.ban.group." + targetIPlayer.getIPermissionGroup().getName().toLowerCase())) {
                            if (LostProxy.getInstance().getReasonManager().getRegistedBanReasons().size() > 0) {
                                List<IBanReason> iBanReasons = LostProxy.getInstance().getReasonManager().getRegistedBanReasons().stream().filter(one -> commandSender.hasPermission(one.getPermission())).collect(Collectors.toList());

                                if (iBanReasons.size() > 0) {
                                    iBanReasons.sort(Comparator.comparingInt(IReason::getId));

                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("bancommand.banreasons.available")).build());
                                    ELocale finalLocale = locale;
                                    iBanReasons.forEach(iBanReason -> {
                                        TextComponent tc1 = new MessageBuilder("§8» §e" + iBanReason.getId() + " §8" + Prefix.DASH + " §c" + iBanReason.getName() + " §8» ").build();
                                        TextComponent tc2 = new MessageBuilder("§7[§a" + finalLocale.getMessage("click") + "§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + targetIPlayer.getPlayerName() + " " + iBanReason.getId()).build();
                                        tc1.addExtra(tc2);
                                        commandSender.sendMessage(tc1);
                                    });
                                    commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                } else {
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("bancommand.banreasons.no_available")).build());
                                }
                            } else {
                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("bancommand.banreasons.no_registered")).build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("bancommand.no_permission_to_kick").replaceAll("%player%", targetIPlayer.getDisplay() + targetIPlayer.getPlayerName())).build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("bancommand.ban.already_banned").replaceAll("%player%", targetIPlayer.getDisplay() + targetIPlayer.getPlayerName())).build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("player_not_found")).build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("bancommand.ban.itself")).build());
            }
        } else if (strings.length == 2) {
            if (!commandSender.getName().equalsIgnoreCase(strings[0])) {
                UUID uuid = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
                if (uuid != null) {
                    IPlayerSync targetIPlayer = new IPlayerSync(uuid);
                    IBan iBan = LostProxy.getInstance().getBanManager().getBan(uuid);
                    if (iBan == null) {
                        if (commandSender.hasPermission("lostproxy.command.ban.group." + targetIPlayer.getIPermissionGroup().getName().toLowerCase())) {
                            try {
                                int reasonId = Integer.parseInt(strings[1]);

                                IBanReason iBanReason = LostProxy.getInstance().getReasonManager().getBanReasonByID(reasonId);

                                if (iBanReason != null) {
                                    if (commandSender.hasPermission(iBanReason.getPermission())) {
                                        long currentTimeMillis = System.currentTimeMillis();
                                        long banDuration = (iBanReason.getTime() == -1 ? -1 : iBanReason.getETimeUnit().toMillis(iBanReason.getTime()));
                                        long end = (banDuration == -1 ? -1 : currentTimeMillis + banDuration);
                                        String invoker = (commandSender instanceof ProxiedPlayer ? ((ProxiedPlayer) commandSender).getUniqueId().toString() : "console");
                                        IBan ban = new IBan(uuid, targetIPlayer.getPlayerName(), invoker, iBanReason.getName(), currentTimeMillis, banDuration, true, null);

                                        LostProxy.getInstance().getBanManager().insertBan(ban);

                                        if (targetIPlayer.isOnline()) {
                                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                                            ELocale targetLocale = LostProxy.getInstance().getLocaleManager().getLocaleData(target).getLocale();
                                            if (banDuration == -1) {
                                                target.disconnect(new MessageBuilder("§6§o■§r §8" + Prefix.DASH + " §cLostName §8● §7the new version of us §8" + Prefix.DASH + " §6§o■§r \n" +
                                                        "\n" +
                                                        targetLocale.getMessage("disconnectscreen.ban.permanent") +
                                                        "\n" +
                                                        "\n" +
                                                        "§7" + targetLocale.getMessage("disconnectscreen.reason") + " §8➡ §e" + iBanReason.getName() +
                                                        "\n" +
                                                        "\n" +
                                                        targetLocale.getMessage("disconnectscreen.forum") +
                                                        "\n" +
                                                        " §8» §cforum§8.§clostname§8.§ceu §8«" +
                                                        "\n" +
                                                        "\n" +
                                                        "§8§m--------------------------------------§r").build());
                                            } else {
                                                target.disconnect(new MessageBuilder("§6§o■§r §8" + Prefix.DASH + " §cLostName §8● §7the new version of us §8" + Prefix.DASH + " §6§o■§r \n" +
                                                        "\n" +
                                                        targetLocale.getMessage("disconnectscreen.ban.temporary") +
                                                        "\n" +
                                                        "\n" +
                                                        "§7" + targetLocale.getMessage("disconnectscreen.reason") + " §8➡ §e" + iBanReason.getName() +
                                                        "\n" +
                                                        "§7" + targetLocale.getMessage("disconnectscreen.remaining_time") + " §8➡ §c" + LostProxy.getInstance().getBanManager().calculateRemainingTime(locale, ban.getEnd()) +
                                                        "\n" +
                                                        "§7" + targetLocale.getMessage("disconnectscreen.expires_on") + " §8➡ §c" + new SimpleDateFormat("dd.MM.yyyy").format(ban.getEnd()) + " " + targetLocale.getMessage("disconnectscreen.at") + " §c" + new SimpleDateFormat("HH:mm:ss").format(ban.getEnd()) + " §7" + targetLocale.getMessage("clock") +
                                                        "\n" +
                                                        "\n" +
                                                        targetLocale.getMessage("disconnectscreen.forum") +
                                                        "\n" +
                                                        " §8» §cforum§8.§clostname§8.§ceu §8«" +
                                                        "\n" +
                                                        "\n" +
                                                        "§8§m--------------------------------------§r").build());
                                            }
                                        }

                                        if (commandSender instanceof ProxiedPlayer) {
                                            ProxiedPlayer sender = (ProxiedPlayer) commandSender;
                                            IPlayerSync iPlayerSync = new IPlayerSync(sender.getUniqueId());
                                            LostProxy.getInstance().getTeamManager().sendBanNotify(iPlayerSync.getDisplay() + sender.getName(), targetIPlayer.getDisplay() + targetIPlayer.getPlayerName(), iBanReason);
                                        } else {
                                            LostProxy.getInstance().getTeamManager().sendBanNotify(locale.getMessage("system"), targetIPlayer.getDisplay() + targetIPlayer.getPlayerName(), iBanReason);
                                        }


                                        IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(uuid);
                                        iBanHistory.addEntry(new IBanEntry(EBanEntryType.BAN_ENTRY, uuid, invoker, currentTimeMillis, iBanReason.getName(), iBanReason.getTime(), iBanReason.getETimeUnit(), end));

                                        LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory);
                                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("bancommand.ban.successfull").replaceAll("%player%", targetIPlayer.getDisplay() + targetIPlayer.getPlayerName()).replaceAll("%reason%", iBanReason.getName()).replaceAll("%time%", ban.getEnd() == -1 ? "§4" + locale.getMessage("permanent") : iBanReason.getTime() + " " + ETimeUnit.getDisplayName(iBanReason.getTime(), iBanReason.getETimeUnit(), locale))).build());
                                    } else {
                                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("bancommand.banreason.no_permission").replaceAll("%reason%", iBanReason.getName())).build());
                                    }
                                } else {
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("bancommand.banreasons.not_found")).build());
                                }
                            } catch (NumberFormatException numberFormatException) {
                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.no_number_given")).build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("bancommand.ban.no_permission").replaceAll("%player%", targetIPlayer.getDisplay() + targetIPlayer.getPlayerName())).build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("bancommand.ban.already_banned")).build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("player_not_found")).build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("bancommand.ban.itself")).build());
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.see_usage")).build());
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            CloudServices.PLAYER_MANAGER.getOnlinePlayers().stream().filter(one -> one.getName().startsWith(strings[0])).forEach(one -> list.add(one.getName()));
        } else if (strings.length == 2) {
            LostProxy.getInstance().getReasonManager().getRegistedBanReasons().stream().filter(one -> commandSender.hasPermission(one.getPermission()) && String.valueOf(one.getId()).startsWith(strings[1])).forEach(one -> list.add(String.valueOf(one.getId())));
        }
        return list;
    }
}
