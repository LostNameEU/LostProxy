package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.utils.MongoCollection;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.UUID;

public class BanHistoryClearCommand extends Command implements TabExecutor {

    public BanHistoryClearCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0 || strings.length >= 3) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/bhclear§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/bhclear <Spieler> §8» §7Leert die Ban-History des angegebenen Spielers").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/bhclear ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (targetUUID != null) {
                IPlayerSync targetiPlayer = new IPlayerSync(targetUUID);
                IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(targetUUID);
                if (iBanHistory.getHistory().size() > 0) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Möchtest du wirklich die §eBan-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §clöschen§8?").build());
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "§7[§a§lKlick§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/bhclear " + strings[0] + " confirmed").build());
                    if (!LostProxy.getInstance().getHistoryManager().getBanHistoryClearCommandProcess().contains(commandSender.getName())) {
                        LostProxy.getInstance().getHistoryManager().getBanHistoryClearCommandProcess().add(commandSender.getName());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die §eBan-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §7ist §cleer§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler konnte §cnicht §7gefunden werden§8.").build());
            }
        } else {
            if (strings[1].equalsIgnoreCase("confirmed")) {
                if (LostProxy.getInstance().getHistoryManager().getBanHistoryClearCommandProcess().contains(commandSender.getName())) {
                    UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
                    if (targetUUID != null) {
                        IPlayerSync targetiPlayer = new IPlayerSync(targetUUID);
                        IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(targetUUID);
                        LostProxy.getInstance().getHistoryManager().getBanHistoryClearCommandProcess().remove(commandSender.getName());
                        if (iBanHistory.getHistory().size() > 0) {
                            iBanHistory.getHistory().clear();
                            LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory);
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §aerfolgreich §7die §eBan-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §cgelöscht§8.").build());
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die §eBan-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §7ist §cleer§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler konnte §cnicht §7gefunden werden§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §eVerifizierung §7für diesen §eProzess §7beantragt§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 0) {
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.BAN_HISTORIES).find().forEach(one -> {
                IBanHistory iBanHistory = LostProxy.getInstance().getGson().fromJson(one.toJson(), IBanHistory.class);
                if (iBanHistory.getHistory().size() > 1) {
                    list.add(new IPlayerSync(iBanHistory.getUniqueId()).getPlayerName());
                }
            });
        }
        return list;
    }
}
