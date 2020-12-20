package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IReason;
import eu.lostname.lostproxy.interfaces.bkms.IMuteReason;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

public class MuteReasonsCommand extends Command {
    public MuteReasonsCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/mutereasons§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/mutereasons list §8» §7Liste dir alle Mutegruende auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mutereasons list").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/mutereasons add <ID> <Name> <Zeit> <Zeiteinheit> <Permission> §8» §7Liste dir alle Mutegruende auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mutereasons add NAME ZEIT ZEITEINHEIT PERMISSION").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/mutereasons <ID> §8» §7Zeige Informationen über einen Mutegrund an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mutereasons ID").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/mutereasons <ID> set <id,name,time,timeunit,permission> <Wert> §8» §7Bearbeite einen Mutegrund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mutereasons ID set ").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/mutereasons <ID> delete §8» §7Lösche einen Mutegrund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mutereasons ID set ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            if ("list".equals(strings[0])) {
                if (commandSender.hasPermission("lostproxy.command.mutereasons.list")) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Folgende Mutegründe sind registriert§8:").build());
                    LostProxy.getInstance().getReasonManager().getRegistedMuteReasons().stream().sorted(Comparator.comparingInt(IReason::getId)).forEach(iMuteReasons -> commandSender.sendMessage(new MessageBuilder("§8┃ §e" + iMuteReasons.getId() + " §8» §e" + iMuteReasons.getName()).addClickEvent(ClickEvent.Action.RUN_COMMAND, "/mutereasons " + iMuteReasons.getId()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §7Klicke diese Nachricht§8, §7um genaue Informationen zu diesem Mutegrund zu erhalten§8.").build()));
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§8.").build());
                }
            } else {
                try {
                    IMuteReason iMuteReason = LostProxy.getInstance().getReasonManager().getMuteReasonByID(Integer.parseInt(strings[0]));

                    if (iMuteReason != null) {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Informationen zum angegebenen Mutegrund§8:").build());
                        commandSender.sendMessage(new MessageBuilder("§8┃ §7Name §8» §c" + iMuteReason.getName()).build());
                        commandSender.sendMessage(new MessageBuilder("§8┃ §7ID §8» §c" + iMuteReason.getId()).build());
                        commandSender.sendMessage(new MessageBuilder("§8┃ §7Mutezeit §8» §c" + iMuteReason.getTime() + iMuteReason.getTimeUnit().toString()).build());
                        commandSender.sendMessage(new MessageBuilder("§8┃ §7Berechtigung §8» §c" + iMuteReason.getPermission()).build());
                        commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());

                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Mutegrund wurde §cnicht §7gefunden§8.").build());
                    }
                } catch (NumberFormatException numberFormatException) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                }
            }
        } else if (strings.length == 2) {
            try {
                IMuteReason iMuteReason = LostProxy.getInstance().getReasonManager().getMuteReasonByID(Integer.parseInt(strings[0]));

                if (iMuteReason != null) {
                    if (strings[1].equalsIgnoreCase("delete")) {
                        if (commandSender.hasPermission("lostproxy.command.mutereasons.delete")) {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Soll der Mutegrund §e" + iMuteReason.getName() + " §8(§e" + iMuteReason.getId() + "§8) §7tatsächlich gelöscht werden§8.").build());
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "§7[§a§lKlick§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/banreasons " + iMuteReason.getId() + " delete confirmed").build());

                            if (!LostProxy.getInstance().getReasonManager().getMuteReasonCommandProcess().contains(commandSender.getName())) {
                                LostProxy.getInstance().getReasonManager().getMuteReasonCommandProcess().add(commandSender.getName());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Mutegrund wurde §cnicht §7gefunden§8.").build());
                }
            } catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
            }
        } else if (strings.length == 3) {
            try {
                IMuteReason iMuteReason = LostProxy.getInstance().getReasonManager().getMuteReasonByID(Integer.parseInt(strings[0]));

                if (iMuteReason != null) {
                    if (strings[1].equalsIgnoreCase("delete")) {
                        if (commandSender.hasPermission("lostproxy.command.mutereasons.delete")) {
                            if (LostProxy.getInstance().getReasonManager().getMuteReasonCommandProcess().contains(commandSender.getName())) {
                                if (strings[2].equalsIgnoreCase("confirmed")) {
                                    LostProxy.getInstance().getReasonManager().getMuteReasonCommandProcess().remove(commandSender.getName());
                                    LostProxy.getInstance().getReasonManager().deleteMuteReason(iMuteReason);
                                    LostProxy.getInstance().getReasonManager().reloadMuteReasons();
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Mutegrund §e" + iMuteReason.getName() + " §8(§e" + iMuteReason.getId() + "§8) §7wurde erfolgreich §cgelöscht§8.").build());
                                } else {
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                                }
                            } else {
                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §eVerifizierung §7für diesen §eProzess §7beantragt§8.").build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Mutegrund wurde §cnicht §7gefunden§8.").build());
                }
            } catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
            }
        } else if (strings.length == 4) {
            try {
                IMuteReason iMuteReason = LostProxy.getInstance().getReasonManager().getMuteReasonByID(Integer.parseInt(strings[0]));

                if (iMuteReason != null) {
                    if (strings[1].equalsIgnoreCase("set")) {
                        if (commandSender.hasPermission("lostproxy.command.mutereasons.set")) {
                            switch (strings[2]) {
                                case "id":
                                    int newId = Integer.parseInt(strings[3]);
                                    iMuteReason.setId(newId);

                                    LostProxy.getInstance().getReasonManager().saveMuteReason(iMuteReason);
                                    LostProxy.getInstance().getReasonManager().reloadMuteReasons();
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die neue ID des Mutegrunds §e" + iMuteReason.getName() + " §7lautet nun §a" + newId + "§8.").build());
                                    break;
                                case "name":
                                    iMuteReason.setName(strings[3].replaceAll("_", " "));

                                    LostProxy.getInstance().getReasonManager().saveMuteReason(iMuteReason);
                                    LostProxy.getInstance().getReasonManager().reloadMuteReasons();
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der neue Name des Mutegrunds §e" + iMuteReason.getName() + " §8(§e" + iMuteReason.getId() + "§8) §7lautet nun §a" + iMuteReason.getName() + "§8.").build());
                                    break;
                                case "time":
                                    iMuteReason.setTime(Long.parseLong(strings[3]));

                                    LostProxy.getInstance().getReasonManager().saveMuteReason(iMuteReason);
                                    LostProxy.getInstance().getReasonManager().reloadMuteReasons();
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der neue Zeitwert des Mutegrunds §e" + iMuteReason.getName() + " §8(§e" + iMuteReason.getId() + "§8) §7ist nun §a" + iMuteReason.getTime() + "§8.").build());
                                    break;
                                case "timeunit":
                                    TimeUnit timeUnit = Arrays.stream(TimeUnit.values()).filter(one -> one.toString().equalsIgnoreCase(strings[3])).findFirst().orElse(null);
                                    if (timeUnit == null) {
                                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die angegebene §eZeiteinheit §7wurde §cnicht §7gefunden§8.").build());
                                        return;
                                    }

                                    LostProxy.getInstance().getReasonManager().saveMuteReason(iMuteReason);
                                    LostProxy.getInstance().getReasonManager().reloadMuteReasons();
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die neue Zeiteinheit des Mutegrunds §e" + iMuteReason.getName() + " §8(§e" + iMuteReason.getId() + "§8) §7ist nun §a" + iMuteReason.getTimeUnit().toString() + "§8.").build());
                                    break;
                                case "permission":
                                    iMuteReason.setPermission(strings[3]);

                                    LostProxy.getInstance().getReasonManager().saveMuteReason(iMuteReason);
                                    LostProxy.getInstance().getReasonManager().reloadMuteReasons();
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die neue Permission des Mutegrunds §e" + iMuteReason.getName() + " §8(§e" + iMuteReason.getId() + "§8) §7ist nun §a" + iMuteReason.getPermission() + "§8.").build());
                                    break;
                                default:
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                                    break;
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Banngrund wurde §cnicht §7gefunden§8.").build());
                }
            } catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
            }
        } else if (strings.length == 6) {
            if (strings[0].equalsIgnoreCase("add")) {
                try {
                    int id = Integer.parseInt(strings[1]);

                    if (LostProxy.getInstance().getReasonManager().getMuteReasonByID(id) == null) {
                        String name = strings[2];
                        int time = Integer.parseInt(strings[3]);
                        TimeUnit timeUnit = Arrays.stream(TimeUnit.values()).filter(one -> one.toString().equalsIgnoreCase(strings[3])).findFirst().orElse(null);

                        if (timeUnit != null) {
                            String permission = strings[4];
                            IMuteReason iMuteReason = new IMuteReason(id, name, time, timeUnit, permission);

                            LostProxy.getInstance().getReasonManager().saveMuteReason(iMuteReason);
                            LostProxy.getInstance().getReasonManager().reloadMuteReasons();
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §aerfolgreich §7den Mutegrund §e" + name + " §7mit der ID §e" + id + " §7erstellt§8.").build());
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die angegebene §eZeiteinheit §7wurde §cnicht §7gefunden§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Es existiert §cbereits §7ein Mutegrund mit der ID §e" + id + "§8.").build());
                    }
                } catch (NumberFormatException numberFormatException) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast als Argument §ckeine §7Zahl angegeben§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
        }
    }
}