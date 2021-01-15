/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 15.01.2021 @ 22:55:46
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
import eu.lostname.lostproxy.interfaces.ILocaleData;
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
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            ILocaleData locale = LostProxy.getInstance().getLocaleManager().getLocaleData(player);

            if (strings.length == 0) {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("bancommand.usage").replaceAll("%cmd%", "/ban")).build());
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
                                        iBanReasons.forEach(iBanReason -> {
                                            TextComponent tc1 = new MessageBuilder("§8» §e" + iBanReason.getId() + " §8" + Prefix.DASH + " §c" + iBanReason.getName() + " §8» ").build();
                                            TextComponent tc2 = new MessageBuilder("§7[§a" + locale.getMessage("click") + "§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + targetIPlayer.getPlayerName() + " " + iBanReason.getId()).build();
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
                                            IBan ban = new IBan(uuid, targetIPlayer.getPlayerName(), "console", iBanReason.getName(), currentTimeMillis, banDuration, true, null);

                                            LostProxy.getInstance().getBanManager().insertBan(ban);

                                            if (targetIPlayer.isOnline()) {
                                                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                                                ILocaleData targetLocale = LostProxy.getInstance().getLocaleManager().getLocaleData(target);
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

                                            LostProxy.getInstance().getTeamManager().sendBanNotify("§4Konsole", targetIPlayer.getDisplay() + targetIPlayer.getPlayerName(), iBanReason);

                                            IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(uuid);
                                            iBanHistory.addEntry(new IBanEntry(EBanEntryType.BAN_ENTRY, uuid, "console", currentTimeMillis, iBanReason.getName(), iBanReason.getTime(), iBanReason.getETimeUnit(), end));

                                            LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory);
                                            player.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("bancommand.ban.successfull").replaceAll("%player%", targetIPlayer.getDisplay() + targetIPlayer.getPlayerName()).replaceAll("%reason%", iBanReason.getName()).replaceAll("%time%", ban.getEnd() == -1 ? locale.getMessage("permanent") : iBanReason.getTime() + " " + ETimeUnit.getDisplayName(iBanReason.getTime(), iBanReason.getETimeUnit(), locale))).build());
                                        } else {
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("bancommand.banreason.no_permission").replaceAll("%reason%", iBanReason.getName())).build());
                                        }
                                    } else {
                                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("bancommand.banreasons.not_found")).build());
                                    }
                                } catch (NumberFormatException numberFormatException) {
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("command.no_number_given")).build());
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
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("command.see_usage")).build());
            }
        } else {
            if (strings.length == 0) {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/ban§8:").build());
                commandSender.sendMessage(new MessageBuilder("§8» §c/ban <Spieler> §8" + Prefix.DASH + " §7Zeigt dir alle verfügbaren Banngründe an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ban ").build());
                commandSender.sendMessage(new MessageBuilder("§8» §c/ban <Spieler> <ID> §8" + Prefix.DASH + " §7Banne einen Spieler direkt").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ban ").build());
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

                                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Verfügbare Banngründe§8:").build());
                                        iBanReasons.forEach(iBanReason -> {
                                            TextComponent tc1 = new MessageBuilder("§8» §e" + iBanReason.getId() + " §8" + Prefix.DASH + " §c" + iBanReason.getName() + " §8» ").build();
                                            TextComponent tc2 = new MessageBuilder("§7[§aKlick§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + targetIPlayer.getPlayerName() + " " + iBanReason.getId()).build();
                                            tc1.addExtra(tc2);
                                            commandSender.sendMessage(tc1);
                                        });
                                        commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                    } else {
                                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Zurzeit sind §ckeinerlei §7Banngründe für dich verfügbar§8.").build());
                                    }
                                } else {
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Zurzeit sind §ckeinerlei §7Banngründe registriert§8.").build());
                                }
                            } else {
                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §7Rechte§8, §7um " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7zu §ebannen§8.").build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7ist §cbereits §7gebannt§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du darfst dich §cnicht §7selber bannen§8.").build());
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
                                            IBan ban = new IBan(uuid, targetIPlayer.getPlayerName(), "console", iBanReason.getName(), currentTimeMillis, banDuration, true, null);

                                            LostProxy.getInstance().getBanManager().insertBan(ban);

                                            if (targetIPlayer.isOnline()) {
                                                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                                                ILocaleData targetLocale = LostProxy.getInstance().getLocaleManager().getLocaleData(target);
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
                                                            "§7" + targetLocale.getMessage("disconnectscreen.remaining_time") + " §8➡ §c" + LostProxy.getInstance().getBanManager().calculateRemainingTime(targetLocale, ban.getEnd()) +
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

                                            LostProxy.getInstance().getTeamManager().sendBanNotify("§4Konsole", targetIPlayer.getDisplay() + targetIPlayer.getPlayerName(), iBanReason);

                                            IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(uuid);
                                            iBanHistory.addEntry(new IBanEntry(EBanEntryType.BAN_ENTRY, uuid, "console", currentTimeMillis, iBanReason.getName(), iBanReason.getTime(), iBanReason.getETimeUnit(), end));

                                            LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory);
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7wegen §e" + iBanReason.getName() + " §7für §c" + (ban.getEnd() == -1 ? "§4permanent" : iBanReason.getTime() + " " + ETimeUnit.getDisplayName(iBanReason.getTime(), iBanReason.getETimeUnit(), new ILocaleData(UUID.randomUUID(), ELocale.GERMAN))) + " §7gebannt§8.").build());
                                        } else {
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast darfst den Banngrund §e" + iBanReason.getName() + " §cnicht §7benutzen§8.").build());
                                        }
                                    } else {
                                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Banngrund wurde §cnicht §7gefunden§8.").build());
                                    }
                                } catch (NumberFormatException numberFormatException) {
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §7Zahl angegeben§8.").build());
                                }
                            } else {
                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §7Rechte§8, §7um " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7zu §ebannen§8.").build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7ist §cbereits §7gebannt§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du darfst dich §cnicht §7selber bannen§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
            }
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
