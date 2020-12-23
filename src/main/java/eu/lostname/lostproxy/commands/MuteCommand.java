package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.EMuteEntryType;
import eu.lostname.lostproxy.enums.ETimeUnit;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.IReason;
import eu.lostname.lostproxy.interfaces.bkms.IMute;
import eu.lostname.lostproxy.interfaces.bkms.IMuteReason;
import eu.lostname.lostproxy.interfaces.historyandentries.mute.IMuteEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.mute.IMuteHistory;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MuteCommand extends Command {

    public MuteCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/mute§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/mute <Spieler> §8» §7Zeigt dir alle verfügbaren Mutegründe an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mute ").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/mute <Spieler> <ID> §8» §7Mute einen Spieler direkt").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mute ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            if (!commandSender.getName().equalsIgnoreCase(strings[0])) {
                UUID uuid = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
                if (uuid != null) {
                    IPlayerSync targetIPlayer = new IPlayerSync(uuid);
                    IMute iMute = LostProxy.getInstance().getMuteManager().getMute(uuid);
                    if (iMute == null) {
                        if (commandSender.hasPermission("lostproxy.command.mute.group." + targetIPlayer.getIPermissionGroup().getName().toLowerCase())) {
                            if (LostProxy.getInstance().getReasonManager().getRegistedMuteReasons().size() > 0) {
                                List<IMuteReason> iMuteReasons = LostProxy.getInstance().getReasonManager().getRegistedMuteReasons().stream().filter(one -> commandSender.hasPermission(one.getPermission())).collect(Collectors.toList());

                                if (iMuteReasons.size() > 0) {
                                    iMuteReasons.sort(Comparator.comparingInt(IReason::getId));

                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Verfügbare Mutegründe§8:").build());
                                    iMuteReasons.forEach(iMuteReason -> {
                                        TextComponent tc1 = new MessageBuilder("§8┃ §e" + iMuteReason.getId() + " §8» §c" + iMuteReason.getName() + " §8» ").build();
                                        TextComponent tc2 = new MessageBuilder("§7[§a§lKlick§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + targetIPlayer.getPlayerName() + " " + iMuteReason.getId()).build();
                                        tc1.addExtra(tc2);
                                        commandSender.sendMessage(tc1);
                                    });
                                    commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                } else {
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Zurzeit sind §ckeinerlei §7Mutegründe für dich verfügbar§8.").build());
                                }
                            } else {
                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Zurzeit sind §ckeinerlei §7Mutegründe registriert§8.").build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §7Rechte§8, §7um " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7zu §emuten§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7ist §cbereits §7gemutet§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du darfst dich §cnicht §7selber muten§8.").build());
            }
        } else if (strings.length == 2) {
            if (!commandSender.getName().equalsIgnoreCase(strings[0])) {
                UUID uuid = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
                if (uuid != null) {
                    IPlayerSync targetIPlayer = new IPlayerSync(uuid);
                    IMute iMute = LostProxy.getInstance().getMuteManager().getMute(uuid);
                    if (iMute == null) {
                        if (commandSender.hasPermission("lostproxy.command.mute.group." + targetIPlayer.getIPermissionGroup().getName().toLowerCase())) {
                            try {
                                int reasonId = Integer.parseInt(strings[1]);

                                IMuteReason iMuteReason = LostProxy.getInstance().getReasonManager().getMuteReasonByID(reasonId);

                                if (iMuteReason != null) {
                                    if (commandSender.hasPermission(iMuteReason.getPermission())) {
                                        long currentTimeMillis = System.currentTimeMillis();
                                        long muteDuration = (iMuteReason.getTime() == -1 ? -1 : iMuteReason.getETimeUnit().toMillis(iMuteReason.getTime()));
                                        String invokerId = (commandSender instanceof ProxiedPlayer ? ((ProxiedPlayer) commandSender).getUniqueId().toString() : "console");

                                        IMute mute = new IMute(uuid, targetIPlayer.getPlayerName(), invokerId, iMuteReason.getName(), currentTimeMillis, muteDuration, true);

                                        LostProxy.getInstance().getMuteManager().insertMute(mute);

                                        if (targetIPlayer.isOnline()) {
                                            if (muteDuration == -1) {
                                                ProxyServer.getInstance().getPlayer(uuid).sendMessage(new MessageBuilder(Prefix.BKMS + "Du wurdest §4permanent §7gemutet§8.").build());
                                                ProxyServer.getInstance().getPlayer(uuid).sendMessage(new MessageBuilder("§8┃ §7Grund §8» §c" + mute.getReason()).build());
                                                ProxyServer.getInstance().getPlayer(uuid).sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                            } else {
                                                ProxyServer.getInstance().getPlayer(uuid).sendMessage(new MessageBuilder(Prefix.BKMS + "Du wurdest §ctemporär §7gemutet§8.").build());
                                                ProxyServer.getInstance().getPlayer(uuid).sendMessage(new MessageBuilder("§8┃ §7Grund §8» §c" + mute.getReason()).build());
                                                ProxyServer.getInstance().getPlayer(uuid).sendMessage(new MessageBuilder("§8┃ §7Verleibende Zeit §8» §c" + LostProxy.getInstance().getMuteManager().calculateRemainingTime(mute.getEnd())).build());
                                                ProxyServer.getInstance().getPlayer(uuid).sendMessage(new MessageBuilder("§8┃ §7Läuft ab am §8» §c" + new SimpleDateFormat("dd.MM.yyyy").format(mute.getEnd()) + " §7um §c" + new SimpleDateFormat("HH:mm:ss").format(mute.getEnd()) + " §7Uhr").build());
                                                ProxyServer.getInstance().getPlayer(uuid).sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                            }
                                        }

                                        if (commandSender instanceof ProxiedPlayer) {
                                            IPlayerSync iPlayer = new IPlayerSync(((ProxiedPlayer) commandSender).getUniqueId());
                                            LostProxy.getInstance().getTeamManager().sendMuteNotify(iPlayer.getDisplay() + iPlayer.getPlayerName(), targetIPlayer.getDisplay() + targetIPlayer.getPlayerName(), iMuteReason);
                                        } else {
                                            LostProxy.getInstance().getTeamManager().sendMuteNotify("§4Konsole", targetIPlayer.getDisplay() + targetIPlayer.getPlayerName(), iMuteReason);
                                        }

                                        IMuteHistory iMuteHistory = LostProxy.getInstance().getHistoryManager().getMuteHistory(uuid);
                                        iMuteHistory.addEntry(new IMuteEntry(EMuteEntryType.MUTE_ENTRY, uuid, invokerId, currentTimeMillis, iMuteReason.getName(), iMuteReason.getTime(), iMuteReason.getETimeUnit(), (muteDuration == -1 ? -1 : currentTimeMillis + muteDuration)));

                                        LostProxy.getInstance().getHistoryManager().saveMuteHistory(iMuteHistory);
                                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7wegen §e" + iMuteReason.getName() + " §7für §c" + (iMuteReason.getTime() == -1 ? "§4permanent" : iMuteReason.getTime() + " " + ETimeUnit.getDisplayName(iMuteReason.getTime(), iMuteReason.getETimeUnit())) + " §7gemutet§8.").build());
                                    } else {
                                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast darfst den Mutegrund §e" + iMuteReason.getName() + " §cnicht §7benutzen§8.").build());
                                    }
                                } else {
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Mutegrund wurde §cnicht §7gefunden§8.").build());
                                }
                            } catch (NumberFormatException numberFormatException) {
                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §7Zahl angegeben§8.").build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §7Rechte§8, §7um " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7zu §emuten§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7ist §cbereits §7gemutet§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du darfst dich §cnicht §7selber muten§8.").build());
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
        }
    }
}