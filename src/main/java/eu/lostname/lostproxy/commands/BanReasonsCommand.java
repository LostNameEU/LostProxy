/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 18.01.2021 @ 23:00:34
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * BanReasonsCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.ELocale;
import eu.lostname.lostproxy.enums.ETimeUnit;
import eu.lostname.lostproxy.interfaces.IReason;
import eu.lostname.lostproxy.interfaces.bkms.IBanReason;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BanReasonsCommand extends Command implements TabExecutor {
    public BanReasonsCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        ELocale locale = ELocale.GERMAN;
        if (commandSender instanceof ProxiedPlayer) {
            locale = LostProxy.getInstance().getLocaleManager().getLocaleData(((ProxiedPlayer) commandSender)).getLocale();
        }
        if (strings.length == 0 || strings.length == 5 || strings.length >= 7) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.usage").replaceAll("%cmd%", "§c/banreasons")).build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/banreasons list §8" + Prefix.DASH + " §7" + locale.getMessage("banreasons.list.usage.description")).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons list").build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/banreasons add <" + locale.getMessage("id") + "> <" + locale.getMessage("name") + "> <" + locale.getMessage("time") + "> <" + locale.getMessage("timeunit") + "> <" + locale.getMessage("permission") + "> §8" + Prefix.DASH + " §7" + locale.getMessage("banreasonscommand.add.usage.description")).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons add ID NAME TIME TIMEUNIT PERMISSION").build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/banreasons <ID> §8" + Prefix.DASH + " §7" + locale.getMessage("banreasonscommand.id.usage.description")).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons ID").build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/banreasons <ID> set <id,name,time,timeunit,permission> <" + locale.getMessage("value") + "> §8" + Prefix.DASH + " §7" + locale.getMessage("banreasonscommand.set.usage.description")).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons ID set ").build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/banreasons <ID> delete §8" + Prefix.DASH + " §7" + locale.getMessage("banreasonscommand.delete.usage.description")).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons ID set ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            if ("list".equals(strings[0])) {
                if (commandSender.hasPermission("lostproxy.command.banreasons.list")) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banreasonscommand.listing.title")).build());
                    ELocale finalLocale = locale;
                    LostProxy.getInstance().getReasonManager().getRegistedBanReasons().stream().sorted(Comparator.comparingInt(IReason::getId)).forEach(iBanReason -> commandSender.sendMessage(new MessageBuilder("§8» §e" + iBanReason.getId() + " §8» §e" + iBanReason.getName()).addClickEvent(ClickEvent.Action.RUN_COMMAND, "/banreasons " + iBanReason.getId()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §7" + finalLocale.getMessage("banreasonscommand.listing.hover")).build()));
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("no_permission")).build());
                }
            } else {
                try {
                    IBanReason iBanReason = LostProxy.getInstance().getReasonManager().getBanReasonByID(Integer.parseInt(strings[0]));

                    if (iBanReason != null) {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banreasonscommand.id.title")).build());
                        commandSender.sendMessage(new MessageBuilder("§8» §7" + locale.getMessage("name") + " §8" + Prefix.DASH + " §c" + iBanReason.getName()).build());
                        commandSender.sendMessage(new MessageBuilder("§8» §7" + locale.getMessage("id") + " §8" + Prefix.DASH + " §c" + iBanReason.getId()).build());
                        commandSender.sendMessage(new MessageBuilder("§8» §7" + locale.getMessage("time") + " §8" + Prefix.DASH + " §c" + (iBanReason.getTime() == -1 ? locale.getMessage("permanent") : iBanReason.getTime() + " " + ETimeUnit.getDisplayName(iBanReason.getTime(), iBanReason.getETimeUnit(), locale))).build());
                        commandSender.sendMessage(new MessageBuilder("§8» §7" + locale.getMessage("permission") + " §8" + Prefix.DASH + " §c" + iBanReason.getPermission()).build());
                        commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());

                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banreason.not_found")).build());
                    }
                } catch (NumberFormatException numberFormatException) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.see_usage")).build());
                }
            }
        } else if (strings.length == 2) {
            try {
                IBanReason iBanReason = LostProxy.getInstance().getReasonManager().getBanReasonByID(Integer.parseInt(strings[0]));

                if (iBanReason != null) {
                    if (strings[1].equalsIgnoreCase("delete")) {
                        if (commandSender.hasPermission("lostproxy.command.banreasons.delete")) {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banreasonscommand.delete.confirmation").replaceAll("%banreason%", iBanReason.getName()).replaceAll("%id%", String.valueOf(iBanReason.getId()))).build());
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "§7[§a§l" + locale.getMessage("click") + "§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/banreasons " + iBanReason.getId() + " delete confirmed").build());

                            if (!LostProxy.getInstance().getReasonManager().getBanReasonCommandProcess().contains(commandSender.getName())) {
                                LostProxy.getInstance().getReasonManager().getBanReasonCommandProcess().add(commandSender.getName());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("no_permission")).build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.see_usage")).build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banreason.not_found")).build());
                }
            } catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.see_usage")).build());
            }
        } else if (strings.length == 3) {
            try {
                IBanReason iBanReason = LostProxy.getInstance().getReasonManager().getBanReasonByID(Integer.parseInt(strings[0]));

                if (iBanReason != null) {
                    if (strings[1].equalsIgnoreCase("delete")) {
                        if (commandSender.hasPermission("lostproxy.command.banreasons.delete")) {
                            if (LostProxy.getInstance().getReasonManager().getBanReasonCommandProcess().contains(commandSender.getName())) {
                                if (strings[2].equalsIgnoreCase("confirmed")) {
                                    LostProxy.getInstance().getReasonManager().getBanReasonCommandProcess().remove(commandSender.getName());
                                    LostProxy.getInstance().getReasonManager().deleteBanReason(iBanReason);
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banreasonscommand.delete.confirmed").replaceAll("%name%", iBanReason.getName()).replaceAll("%id%", String.valueOf(iBanReason.getId()))).build());
                                } else {
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.see_usage")).build());
                                }
                            } else {
                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("verify.not_requested")).build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("no_permission")).build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.see_usage")).build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banreason.not_found")).build());
                }
            } catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.see_usage")).build());
            }
        } else if (strings.length == 4) {
            try {
                IBanReason iBanReason = LostProxy.getInstance().getReasonManager().getBanReasonByID(Integer.parseInt(strings[0]));

                if (iBanReason != null) {
                    if (strings[1].equalsIgnoreCase("set")) {
                        if (commandSender.hasPermission("lostproxy.command.banreasons.set")) {
                            switch (strings[2]) {
                                case "id":
                                    int newId = Integer.parseInt(strings[3]);
                                    iBanReason.setId(newId);

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason);
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banreasonscommand.set.id.successfully").replaceAll("%name%", iBanReason.getName()).replaceAll("%id%", String.valueOf(iBanReason.getId()))).build());
                                    break;
                                case "name":
                                    iBanReason.setName(strings[3].replaceAll("_", " "));

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason);
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banreasonscommand.set.name.successfully").replaceAll("%name%", iBanReason.getName()).replaceAll("%id%", String.valueOf(iBanReason.getId()))).build());
                                    break;
                                case "time":
                                    iBanReason.setTime(Long.parseLong(strings[3]));

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason);
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banreasonscommand.set.time.successfully").replaceAll("%name%", iBanReason.getName()).replaceAll("%time%", String.valueOf(iBanReason.getTime()))).build());
                                    break;
                                case "timeunit":
                                    ETimeUnit eTimeUnit = Arrays.stream(ETimeUnit.values()).filter(one -> one.toString().equalsIgnoreCase(strings[3])).findFirst().orElse(null);
                                    if (eTimeUnit == null) {
                                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("timeunit.not_found")).build());
                                        return;
                                    }

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason);
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banreasonscommand.set.timeunit.successfully").replaceAll("%name%", iBanReason.getName()).replaceAll("%timeunit%", ETimeUnit.getDisplayName(0, eTimeUnit, locale))).build());
                                    break;
                                case "permission":
                                    iBanReason.setPermission(strings[3]);

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason);
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banreasonscommand.set.permission.successfully").replaceAll("%name%", iBanReason.getName()).replaceAll("%permission%", iBanReason.getPermission())).build());
                                    break;
                                default:
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.see_usage")).build());
                                    break;
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("no_permission")).build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.see_usage")).build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banreason.not_found")).build());
                }
            } catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.see_usage")).build());
            }
        } else {
            if (strings[0].equalsIgnoreCase("add")) {
                try {
                    int id = Integer.parseInt(strings[1]);

                    if (LostProxy.getInstance().getReasonManager().getBanReasonByID(id) == null) {
                        String name = strings[2].replaceAll("_", " ");
                        int time = Integer.parseInt(strings[3]);
                        ETimeUnit timeUnit = Arrays.stream(ETimeUnit.values()).filter(one -> one.name().equalsIgnoreCase(strings[4])).findFirst().orElse(null);

                        if (timeUnit != null) {
                            String permission = strings[5];
                            IBanReason iBanReason = new IBanReason(id, name, time, timeUnit, permission);

                            LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason);
                            LostProxy.getInstance().getReasonManager().reloadBanReasons();
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banreasonscommand.add.successfully").replaceAll("%name%", iBanReason.getName()).replaceAll("%id%", String.valueOf(iBanReason.getId()))).build());
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("timeunit.not_found")).build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("banreason.already_exist").replaceAll("%id%", String.valueOf(id))).build());
                    }
                } catch (NumberFormatException numberFormatException) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + locale.getMessage("commands.see_usage")).build());
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
            list.addAll(Arrays.asList("list", "add"));
            LostProxy.getInstance().getReasonManager().getRegistedBanReasons().forEach(one -> list.add(String.valueOf(one.getId())));
            list.removeIf(s -> !s.toLowerCase().startsWith(strings[0].toLowerCase()));
        } else if (strings.length == 2) {
            try {
                int check = Integer.parseInt(strings[0]);
                list.addAll(Arrays.asList("set", "delete"));
                list.removeIf(s -> !s.toLowerCase().startsWith(strings[1].toLowerCase()));
            } catch (NumberFormatException ignored) {
            }
        } else if (strings.length == 3) {
            try {
                int check = Integer.parseInt(strings[0]);
                if (strings[1].equalsIgnoreCase("set")) {
                    list.addAll(Arrays.asList("id", "name", "time", "timeunit", "permission"));
                    list.removeIf(s -> !s.toLowerCase().startsWith(strings[2].toLowerCase()));
                }
            } catch (NumberFormatException ignored) {
            }
        } else if (strings.length == 4) {
            if (strings[2].equalsIgnoreCase("timeunit")) {
                Arrays.stream(ETimeUnit.values()).forEach(one -> list.add(one.name()));
                list.removeIf(s -> !s.toLowerCase().startsWith(strings[3].toLowerCase()));
            }
        } else if (strings.length == 5) {
            Arrays.stream(ETimeUnit.values()).forEach(one -> list.add(one.name()));
            list.removeIf(s -> !s.toLowerCase().startsWith(strings[4].toLowerCase()));
        }
        return list;
    }
}