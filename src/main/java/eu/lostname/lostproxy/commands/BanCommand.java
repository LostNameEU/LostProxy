package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IReason;
import eu.lostname.lostproxy.interfaces.bkms.IBanReason;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;

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
        }
    }
}
