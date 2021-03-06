package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.utils.CloudServices;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class TSCommand extends Command {

    public TSCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            if (strings.length == 0) {
                player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Benutzung von §b/ts§8:").build());
                player.sendMessage(new MessageBuilder("§8┃ §b/ts set <Identität> §8» §7Verknüfe manuell deine TeamSpeak Identität").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts set ").build());
                player.sendMessage(new MessageBuilder("§8┃ §b/ts unlink §8» §7Hebe die Teamspeak-Verknüpfung auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts unlink").build());
                player.sendMessage(new MessageBuilder("§8┃ §b/ts info §8» §7Zeige dir Informationen zu deiner TeamSpeak Verknüfung an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts info").build());
                if (player.hasPermission("lostproxy.command.ts.iinfo")) {
                    player.sendMessage(new MessageBuilder("§8┃ §b/ts iinfo <Identität> §8» §7Lasse dir Informationen zu einer TeamSpeak Identität anzeigen").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts iinfo ").build());
                }
                if (player.hasPermission("lostproxy.command.ts.ninfo")) {
                    player.sendMessage(new MessageBuilder("§8┃ §b/ts ninfo <Spielernamen> §8» §7Lasse dir Informationen zu einem Spielernamen anzeigen").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts ninfo ").build());
                }
                if (player.hasPermission("lostproxy.command.ts.delete")) {
                    player.sendMessage(new MessageBuilder("§8┃ §b/ts delete <Name> §8» §7Lösche die TeamSpeak Verknüpfung eines anderen Spielers").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts delete Name").build());
                }
                if (player.hasPermission("lostproxy.command.ts.set")) {
                    player.sendMessage(new MessageBuilder("§8┃ §b/ts set <Rang> <ID> §8» §7Setzte einer Permission-Gruppe die dazugehörige TS-Servergruppen-ID").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts set ").build());
                }
                player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
            } else if (strings.length == 1) {
                switch (strings[0]) {
                    case "unlink":
                        LostProxy.getInstance().getLinkageManager().getTeamSpeakLinkage(player.getUniqueId(), iTeamSpeakLinkage -> {
                            if (iTeamSpeakLinkage != null) {
                                LostProxy.getInstance().getLinkageManager().deleteTeamSpeakLinkage(iTeamSpeakLinkage, (deleteResult, throwable) -> {
                                    if (deleteResult.wasAcknowledged()) {
                                        player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Die Verknüpfung wurde §aerfolgreich §cgelöscht§8.").build());
                                    } else {
                                        player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Es ist ein §4Fehler §8aufgetreten§8. §7Bitte versuche es später erneut!").build());
                                    }
                                });
                            } else {
                                player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Du hast §ckeine §7TeamSpeak §eIdentität §7mit deinem Minecraft-Account verknüpft§8.").build());
                            }
                        });
                        break;
                    case "info":
                        LostProxy.getInstance().getLinkageManager().getTeamSpeakLinkage(player.getUniqueId(), iTeamSpeakLinkage -> {
                            if (iTeamSpeakLinkage != null) {
                                player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Verknüpfungsinformationen§8:").build());
                                player.sendMessage(new MessageBuilder("§8┃ §7TeamSpeak-Identität §8» §b" + iTeamSpeakLinkage.getIdentity()).build());
                                player.sendMessage(new MessageBuilder("§8┃ §7Zeitstempel §8» §7Am §b" + new SimpleDateFormat("dd.MM.yyyy").format(iTeamSpeakLinkage.getCreationTimestamp()) + " §7um §b" + new SimpleDateFormat("HH:mm:ss").format(iTeamSpeakLinkage.getCreationTimestamp()) + " §7Uhr").build());
                                player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                            } else {
                                player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Du hast deinen Minecraft-Account §cnicht §7mit einer TeamSpeak-Identität §everknüpft§8.").build());
                            }
                        });
                        break;
                    default:
                        player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                        break;
                }
            } else if (strings.length == 2) {
                String argument = strings[1];

                switch (strings[0]) {
                    case "set":
                        LostProxy.getInstance().getLinkageManager().getTeamSpeakLinkage(player.getUniqueId(), identityCheck -> {
                            if (identityCheck == null) {
                                LostProxy.getInstance().getLinkageManager().isTeamSpeakIdentityInUse(argument, aBoolean -> {
                                    if (!aBoolean) {
                                        LostProxy.getInstance().getLinkageManager().createTeamSpeakLinkage(player.getUniqueId(), player.getName(), argument, linkage -> {
                                            player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Du hast §aerfolgreich §7deinen Minecraft-Account mit der folgenden TeamSpeak-Identität verknüpft§8: §b" + linkage.getIdentity()).build());
                                        });
                                    } else {
                                        player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Diese Identität ist §cbereits §7in Verwendung§8.").build());
                                    }
                                });
                            } else {
                                player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Du hast §cbereits §7deinen Minecraft-Account mit einer TeamSpeak-Identität verknüpft§8.").build());
                            }
                        });
                        break;
                    case "iinfo":
                        if (player.hasPermission("lostproxy.command.ts.iinfo")) {
                            LostProxy.getInstance().getLinkageManager().getTeamSpeakLinkage(argument, iTeamSpeakLinkage -> {
                                if (iTeamSpeakLinkage != null) {
                                    LostProxy.getInstance().getPlayerManager().getIPlayerAsync(iTeamSpeakLinkage.getUuid(), iPlayer -> {
                                        player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Verknüpfungsinformationen§8:").build());
                                        player.sendMessage(new MessageBuilder("§8┃ §7TeamSpeak-Identität §8» §b" + iTeamSpeakLinkage.getIdentity()).addClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, iTeamSpeakLinkage.getIdentity()).build());
                                        player.sendMessage(new MessageBuilder("§8┃ §7Minecraft-Account §8» " + iPlayer.getDisplay() + iPlayer.getPlayerName()).build());
                                        player.sendMessage(new MessageBuilder("§8┃ §7Zeitstempel §8» §7Am §b" + new SimpleDateFormat("dd.MM.yyyy").format(iTeamSpeakLinkage.getCreationTimestamp()) + " §7um §b" + new SimpleDateFormat("HH:mm:ss").format(iTeamSpeakLinkage.getCreationTimestamp()) + " §7Uhr").build());
                                        player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                    });
                                } else {
                                    player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Die angegebene Identität ist mit §ckeinem §7Minecraft-Account verknüpft§8.").build());
                                }
                            });
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§8.").build());
                        }
                        break;
                    case "ninfo":
                        if (player.hasPermission("lostproxy.command.ts.ninfo")) {
                            LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(argument, uuid -> {
                                if (uuid != null) {
                                    LostProxy.getInstance().getLinkageManager().getTeamSpeakLinkage(uuid, iTeamSpeakLinkage -> {
                                        if (iTeamSpeakLinkage != null) {
                                            LostProxy.getInstance().getPlayerManager().getIPlayerAsync(uuid, iPlayer -> {
                                                player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Verknüpfungsinformationen§8:").build());
                                                player.sendMessage(new MessageBuilder("§8┃ §7Minecraft-Account §8» " + iPlayer.getDisplay() + iPlayer.getPlayerName()).build());
                                                player.sendMessage(new MessageBuilder("§8┃ §7TeamSpeak-Identität §8» §b" + iTeamSpeakLinkage.getIdentity()).addClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, iTeamSpeakLinkage.getIdentity()).build());
                                                player.sendMessage(new MessageBuilder("§8┃ §7Zeitstempel §8» §7Am §b" + new SimpleDateFormat("dd.MM.yyyy").format(iTeamSpeakLinkage.getCreationTimestamp()) + " §7um §b" + new SimpleDateFormat("HH:mm:ss").format(iTeamSpeakLinkage.getCreationTimestamp()) + " §7Uhr").build());
                                                player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                            });
                                        } else {
                                            player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Dieser Minecraft-Account hat §ckeine §7TeamSpeak-Identität §7verknüpft§8.").build());
                                        }
                                    });
                                } else {
                                    player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Zu dem angegebenen Spielernamen konnte §ckeine §7UUID gefunden werden§8.").build());
                                }
                            });
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§8.").build());
                        }
                        break;
                    case "delete":
                        if (player.hasPermission("lostproxy.command.ts.delete")) {
                            LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(argument, uniqueId -> {
                                if (uniqueId != null) {
                                    LostProxy.getInstance().getLinkageManager().getTeamSpeakLinkage(uniqueId, iTeamSpeakLinkage -> {
                                        if (iTeamSpeakLinkage != null) {
                                            LostProxy.getInstance().getLinkageManager().deleteTeamSpeakLinkage(iTeamSpeakLinkage, (deleteResult, throwable) -> {
                                                if (deleteResult.wasAcknowledged()) {
                                                    LostProxy.getInstance().getPlayerManager().getIPlayerAsync(uniqueId, iPlayerAsync -> {
                                                        player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Du hast §aerfolgreich §7die §eVerknüpfung §7für den Minecraft-Account §b" + iPlayerAsync.getDisplay() + iPlayerAsync.getPlayerName() + " §7gelöscht§8.").build());
                                                    });
                                                } else {
                                                    player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Es ist ein §4Fehler §8aufgetreten§8. §7Bitte versuche es später erneut!").build());
                                                }
                                            });
                                        } else {
                                            player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Dieser Minecraft-Account hat §ckeine §7TeamSpeak-Identität §7verknüpft§8.").build());
                                        }
                                    });
                                } else {
                                    player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Zu dem angegebenen Spielernamen konnte §ckeine §7UUID gefunden werden§8.").build());
                                }
                            });
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§8.").build());
                        }
                        break;
                    default:
                        player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                        break;
                }
            } else if (strings.length == 3) {
                switch (strings[0]) {
                    case "set":
                        if (player.hasPermission("lostproxy.command.ts.set")) {
                            CloudServices.PERMISSION_MANAGEMENT.getGroupAsync(strings[1]).onComplete(iPermissionGroup -> {
                                if (iPermissionGroup != null) {
                                    AtomicInteger tsGroupId = new AtomicInteger(0);

                                    try {
                                        tsGroupId.set(Integer.parseInt(strings[2]));
                                    } catch (NumberFormatException exception) {
                                        player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Du hast §ckeine §7Zahl angegeben§8.").build());
                                        return;
                                    }

                                    LostProxy.getInstance().getTeamSpeakManager().getServerGroup(tsGroupId.get(), serverGroup -> {
                                        if (serverGroup != null) {
                                            iPermissionGroup.getProperties().append("tsGroupId", tsGroupId.get());
                                            CloudServices.PERMISSION_MANAGEMENT.updateGroupAsync(iPermissionGroup).onComplete(unused -> player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Du hast §aerfolgreich §7die hinterlegte §eTS-Servergruppen-ID §7auf §b" + tsGroupId.get() + " §7gesetzt§8.").build())).onFailure(e -> player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Beim §eSpeichern §7der §eRechtegruppe §7ist ein §4Fehler §7aufgetreten§8.").build()));
                                        } else {
                                            player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Die angegebene §eTeamSpeak-Servergruppe §7wurde §cnicht §7gefunden§8.").build());
                                        }
                                    });
                                } else {
                                    player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Die angegebene §eRechtegruppe §7wurde §cnicht §7gefunden§8.").build());
                                }
                            }).onFailure(e -> player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Es trat ein §4Fehler §7bei der Suche der angegebenen §eRechtegruppe §7auf§8.").build()));
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§8.").build());
                        }
                        break;
                    default:
                        player.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                        break;
                }
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.TEAMSPEAK + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§8.").build());
        }
    }
}
