package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IReason;
import eu.lostname.lostproxy.interfaces.bkms.IBanReason;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

public class BanReasonsCommand extends Command {
    public BanReasonsCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0 || strings.length == 5 || strings.length >= 7) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/banreasons§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/banreasons list §8» §7Liste dir alle Bangruende auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons list").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/banreasons add <ID> <Name> <Zeit> <Zeiteinheit> <Permission> §8» §7Liste dir alle Bangruende auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons add NAME ZEIT ZEITEINHEIT PERMISSION").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/banreasons <ID> §8» §7Zeige Informationen über einen Banngrund an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons ID").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/banreasons <ID> set <id,name,time,timeunit,permission> <Wert> §8» §7Bearbeite einen Banngrund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons ID set ").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/banreasons <ID> delete §8» §7Lösche einen Banngrund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons ID set ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            if ("list".equals(strings[0])) {
                if (commandSender.hasPermission("lostproxy.command.banreasons.list")) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Folgende Banngründe sind registriert§8:").build());
                    LostProxy.getInstance().getReasonManager().getRegistedBanReasons().stream().sorted(Comparator.comparingInt(IReason::getId)).forEach(iBanReason -> commandSender.sendMessage(new MessageBuilder("§8┃ §e" + iBanReason.getId() + " §8» §e" + iBanReason.getName()).addClickEvent(ClickEvent.Action.RUN_COMMAND, "/banreasons " + iBanReason.getId()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §7Klicke diese Nachricht§8, §7um genaue Informationen zu diesem Banngrund zu erhalten§8.").build()));
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§8.").build());
                }
            } else {
                try {
                    IBanReason iBanReason = LostProxy.getInstance().getReasonManager().getBanReasonByID(Integer.parseInt(strings[0]));

                    if (iBanReason != null) {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Informationen zum angegebenen Banngrund§8:").build());
                        commandSender.sendMessage(new MessageBuilder("§8┃ §7Name §8» §c" + iBanReason.getName()).build());
                        commandSender.sendMessage(new MessageBuilder("§8┃ §7ID §8» §c" + iBanReason.getId()).build());
                        commandSender.sendMessage(new MessageBuilder("§8┃ §7Bannzeit §8» §c" + iBanReason.getTime() + " " + iBanReason.getTimeUnit().toString()).build());
                        commandSender.sendMessage(new MessageBuilder("§8┃ §7Berechtigung §8» §c" + iBanReason.getPermission()).build());
                        commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());

                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Banngrund wurde §cnicht §7gefunden§8.").build());
                    }
                } catch (NumberFormatException numberFormatException) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                }
            }
        } else if (strings.length == 2) {
            try {
                IBanReason iBanReason = LostProxy.getInstance().getReasonManager().getBanReasonByID(Integer.parseInt(strings[0]));

                if (iBanReason != null) {
                    if (strings[1].equalsIgnoreCase("delete")) {
                        if (commandSender.hasPermission("lostproxy.command.banreasons.delete")) {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Soll der Banngrund §e" + iBanReason.getName() + " §8(§e" + iBanReason.getId() + "§8) §7tatsächlich gelöscht werden§8.").build());
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "§7[§a§lKlick§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/banreasons " + iBanReason.getId() + " delete confirmed").build());

                            if (!LostProxy.getInstance().getReasonManager().getBanReasonCommandProcess().contains(commandSender.getName())) {
                                LostProxy.getInstance().getReasonManager().getBanReasonCommandProcess().add(commandSender.getName());
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
        } else if (strings.length == 3) {
            try {
                IBanReason iBanReason = LostProxy.getInstance().getReasonManager().getBanReasonByID(Integer.parseInt(strings[0]));

                if (iBanReason != null) {
                    if (strings[1].equalsIgnoreCase("delete")) {
                        if (commandSender.hasPermission("lostproxy.command.banreasons.delete")) {
                            if (LostProxy.getInstance().getReasonManager().getBanReasonCommandProcess().contains(commandSender.getName())) {
                                if (strings[2].equalsIgnoreCase("confirmed")) {
                                    LostProxy.getInstance().getReasonManager().getBanReasonCommandProcess().remove(commandSender.getName());
                                    LostProxy.getInstance().getReasonManager().deleteBanReason(iBanReason, (deleteResult, throwable) -> {
                                        if (deleteResult.wasAcknowledged()) {
                                            LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Banngrund §e" + iBanReason.getName() + " §8(§e" + iBanReason.getId() + "§8) §7wurde erfolgreich §cgelöscht§8.").build());
                                        } else {
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Beim Löschen des §eBanngrunds §7ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                                        }
                                    });
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
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Banngrund wurde §cnicht §7gefunden§8.").build());
                }
            } catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
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

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason, (updateResult, throwable) -> {
                                        if (updateResult.wasAcknowledged()) {
                                            LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die neue ID des Banngrunds §e" + iBanReason.getName() + " §7lautet nun §a" + newId + "§8.").build());
                                        } else {
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Beim Aktualisieren des §eBanngrunds §7ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                                        }
                                    });
                                    break;
                                case "name":
                                    iBanReason.setName(strings[3].replaceAll("_", " "));

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason, (updateResult, throwable) -> {
                                        if (updateResult.wasAcknowledged()) {
                                            LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der neue Name des Banngrunds §e" + iBanReason.getName() + " §8(§e" + iBanReason.getId() + "§8) §7lautet nun §a" + iBanReason.getName() + "§8.").build());
                                        } else {
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Beim Aktualisieren des §eBanngrunds §7ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                                        }
                                    });
                                    break;
                                case "time":
                                    iBanReason.setTime(Long.parseLong(strings[3]));

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason, (updateResult, throwable) -> {
                                        if (updateResult.wasAcknowledged()) {
                                            LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der neue Zeitwert des Banngrunds §e" + iBanReason.getName() + " §8(§e" + iBanReason.getId() + "§8) §7ist nun §a" + iBanReason.getTime() + "§8.").build());
                                        } else {
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Beim Aktualisieren des §eBanngrunds §7ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                                        }
                                    });
                                    break;
                                case "timeunit":
                                    TimeUnit timeUnit = Arrays.stream(TimeUnit.values()).filter(one -> one.toString().equalsIgnoreCase(strings[3])).findFirst().orElse(null);
                                    if (timeUnit == null) {
                                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die angegebene §eZeiteinheit §7wurde §cnicht §7gefunden§8.").build());
                                        return;
                                    }

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason, (updateResult, throwable) -> {
                                        if (updateResult.wasAcknowledged()) {
                                            LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die neue Zeiteinheit des Banngrunds §e" + iBanReason.getName() + " §8(§e" + iBanReason.getId() + "§8) §7ist nun §a" + iBanReason.getTimeUnit().toString() + "§8.").build());
                                        } else {
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Beim Aktualisieren des §eBanngrunds §7ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                                        }
                                    });
                                    break;
                                case "permission":
                                    iBanReason.setPermission(strings[3]);

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason, (updateResult, throwable) -> {
                                        if (updateResult.wasAcknowledged()) {
                                            LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die neue Permission des Banngrunds §e" + iBanReason.getName() + " §8(§e" + iBanReason.getId() + "§8) §7ist nun §a" + iBanReason.getPermission() + "§8.").build());
                                        } else {
                                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Beim Aktualisieren des §eBanngrunds §7ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                                        }
                                    });
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
        } else {
            if (strings[0].equalsIgnoreCase("add")) {
                try {
                    int id = Integer.parseInt(strings[1]);

                    if (LostProxy.getInstance().getReasonManager().getBanReasonByID(id) == null) {
                        String name = strings[2];
                        int time = Integer.parseInt(strings[3]);
                        TimeUnit timeUnit = Arrays.stream(TimeUnit.values()).filter(one -> one.name().equalsIgnoreCase(strings[4])).findFirst().orElse(null);

                        if (timeUnit != null) {
                            String permission = strings[5];
                            IBanReason iBanReason = new IBanReason(id, name, time, timeUnit, permission);

                            LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason, (updateResult, throwable) -> {
                                if (updateResult.wasAcknowledged()) {
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();

                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §aerfolgreich §7den Banngrund §e" + name + " §7mit der ID §e" + id + " §7erstellt§8.").build());
                                } else {
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Beim Hinzufügen des §eBanngrunds §7ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                                }
                            });
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die angegebene §eZeiteinheit §7wurde §cnicht §7gefunden§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Es existiert §cbereits §7ein Banngrund mit der ID §e" + id + "§8.").build());
                    }
                } catch (NumberFormatException numberFormatException) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast als Argument §ckeine §7Zahl angegeben§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
            }
        }
    }
}