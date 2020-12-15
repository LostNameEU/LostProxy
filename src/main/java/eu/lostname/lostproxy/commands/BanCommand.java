package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IReason;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import eu.lostname.lostproxy.interfaces.bkms.IBanReason;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanEntry;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BanCommand extends Command {

    public BanCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/ban§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/ban <Spieler> §8» §7Zeigt dir alle verfügbaren Banngründe an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ban ").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/ban <Spieler> <ID> §8» §7Banne einen Spieler direkt").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ban ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            if (!commandSender.getName().equalsIgnoreCase(strings[0])) {
                LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0], uuid -> {
                    if (uuid != null) {
                        LostProxy.getInstance().getPlayerManager().getIPlayerAsync(uuid, targetIPlayer -> LostProxy.getInstance().getBanManager().getBan(uuid, iBan -> {
                            if (iBan == null) {
                                if (commandSender.hasPermission("lostproxy.command.ban.group." + targetIPlayer.getiPermissionGroup().getName().toLowerCase())) {
                                    if (LostProxy.getInstance().getReasonManager().getRegistedBanReasons().size() > 0) {
                                        List<IBanReason> iBanReasons = LostProxy.getInstance().getReasonManager().getRegistedBanReasons().stream().filter(one -> commandSender.hasPermission(one.getPermission())).collect(Collectors.toList());

                                        if (iBanReasons.size() > 0) {
                                            iBanReasons.sort(Comparator.comparingInt(IReason::getId));

                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Verfügbare Banngründe§8:").build());
                                            iBanReasons.forEach(iBanReason -> commandSender.sendMessage(new MessageBuilder("§8┃ §e" + iBanReason.getId() + " §8» §c" + iBanReason.getName() + " §8» ").addExtra(new MessageBuilder("§7[§a§lKlick§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + targetIPlayer.getPlayerName() + " " + iBanReason.getId()).build()).build()));
                                            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                        } else {
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Zurzeit sind §ckeinerlei §7Banngründe für dich verfügbar§8.").build());
                                        }
                                    } else {
                                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Zurzeit sind §ckeinerlei §7Banngründe registriert§8.").build());
                                    }
                                } else {
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §7Rechte§8, §7um " + targetIPlayer.getPrefix() + targetIPlayer.getPlayerName() + " §7zu §ebannen§8.").build());
                                }
                            } else {
                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getPrefix() + targetIPlayer.getPlayerName() + " §7ist §cbereits §7gebannt§8.").build());
                            }
                        }));
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                    }
                });
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du darfst dich §cnicht §7selber bannen§8.").build());
            }
        } else if (strings.length == 2) {
            if (!commandSender.getName().equalsIgnoreCase(strings[0])) {
                LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0], uuid -> {
                    if (uuid != null) {
                        LostProxy.getInstance().getPlayerManager().getIPlayerAsync(uuid, targetIPlayer -> LostProxy.getInstance().getBanManager().getBan(uuid, iBan -> {
                            if (iBan == null) {
                                if (commandSender.hasPermission("lostproxy.command.ban.group." + targetIPlayer.getiPermissionGroup().getName().toLowerCase())) {
                                    try {
                                        int reasonId = Integer.parseInt(strings[1]);

                                        IBanReason iBanReason = LostProxy.getInstance().getReasonManager().getBanReasonByID(reasonId);

                                        if (iBanReason != null) {
                                            if (commandSender.hasPermission(iBanReason.getPermission())) {
                                                long currentTimeMillis = System.currentTimeMillis();
                                                long banDuration = (iBanReason.getTime() == -1 ? -1 : iBanReason.getTimeUnit().toMillis(iBanReason.getTime()));
                                                String invokerId = (commandSender instanceof ProxiedPlayer ? ((ProxiedPlayer) commandSender).getUniqueId().toString() : "console");

                                                IBan ban = new IBan(uuid, targetIPlayer.getPlayerName(), invokerId, iBanReason.getName(), currentTimeMillis, banDuration, true, null);

                                                LostProxy.getInstance().getBanManager().insertBan(ban, (unused, throwable) -> {

                                                    if (targetIPlayer.isOnline()) {
                                                        if (banDuration == -1) {
                                                            ProxyServer.getInstance().getPlayer(uuid).disconnect(new MessageBuilder("§6§o■§r §8┃ §c§lLostName §8● §7the new version of us §8┃ §6§o■§r \n" +
                                                                    "\n" +
                                                                    "§7Du wurdest §4§npermanent§r §7vom Netzwerk §4gebannt§8." +
                                                                    "\n" +
                                                                    "\n" +
                                                                    "§7Grund §8➡ §e" + iBanReason.getName() +
                                                                    "\n" +
                                                                    "\n" +
                                                                    "§7FÜr weitere Fragen oder zum Stellen eines Entbannugsantrag besuche das Forum§8!" +
                                                                    "\n" +
                                                                    " §8» §cforum§8.§clostname§8.§ceu §8«" +
                                                                    "\n" +
                                                                    "\n" +
                                                                    "§8§m--------------------------------------§r").build());
                                                        } else {
                                                            ProxyServer.getInstance().getPlayer(uuid).disconnect(new MessageBuilder("§6§o■§r §8┃ §c§lLostName §8● §7the new version of us §8┃ §6§o■§r \n" +
                                                                    "\n" +
                                                                    "§7Du wurdest §4temporär §7vom Netzwerk §4gebannt§8." +
                                                                    "\n" +
                                                                    "\n" +
                                                                    "§7Grund §8➡ §e" + iBanReason.getName() +
                                                                    "\n" +
                                                                    "§7Verbleibende Zeit §8➡ §c" + LostProxy.getInstance().getBanManager().calculateRemainingTime(ban.getEnd()) +
                                                                    "\n" +
                                                                    "§7Läuft ab am §8➡ §c" + new SimpleDateFormat("dd.MM.yyyy").format(ban.getEnd()) + " §7um §c" + new SimpleDateFormat("HH:mm:ss").format(ban.getEnd()) + " §7Uhr" +
                                                                    "\n" +
                                                                    "§7FÜr weitere Fragen oder zum Stellen eines Entbannugsantrag besuche das Forum§8!" +
                                                                    "\n" +
                                                                    " §8» §cforum§8.§clostname§8.§ceu §8«" +
                                                                    "\n" +
                                                                    "\n" +
                                                                    "§8§m--------------------------------------§r").build());
                                                        }
                                                    }

                                                    if (commandSender instanceof ProxiedPlayer) {
                                                        LostProxy.getInstance().getPlayerManager().getIPlayerAsync(((ProxiedPlayer) commandSender).getUniqueId(), iPlayer -> LostProxy.getInstance().getTeamManager().sendBanNotify(iPlayer.getPrefix() + iPlayer.getPlayerName(), targetIPlayer.getPrefix() + targetIPlayer.getPlayerName(), iBanReason));
                                                    } else {
                                                        LostProxy.getInstance().getTeamManager().sendBanNotify("§4Konsole", targetIPlayer.getPrefix() + targetIPlayer.getPlayerName(), iBanReason);
                                                    }

                                                    LostProxy.getInstance().getHistoryManager().getBanHistory(uuid, iBanHistory -> {
                                                        iBanHistory.addEntry(new IBanEntry(uuid, invokerId, currentTimeMillis, iBanReason.getName(), banDuration, (banDuration == -1 ? -1 : currentTimeMillis + banDuration)));

                                                        LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory, aBoolean -> {
                                                            if (aBoolean) {
                                                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast " + targetIPlayer.getPrefix() + targetIPlayer.getPlayerName() + " §7wegen §e" + iBanReason.getName() + " §7für §c" + iBanReason.displayBanDuration() + " §7gebannt§8.").build());
                                                            } else {
                                                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Beim Speichern der §eHistory §7ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                                                            }
                                                        });
                                                    });
                                                });
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
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §7Rechte§8, §7um " + targetIPlayer.getPrefix() + targetIPlayer.getPlayerName() + " §7zu §ebannen§8.").build());
                                }
                            } else {
                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getPrefix() + targetIPlayer.getPlayerName() + " §7ist §cbereits §7gebannt§8.").build());
                            }
                        }));
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                    }
                });
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du darfst dich §cnicht §7selber bannen§8.").build());
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
        }
    }
}
