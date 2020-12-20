package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.EBanEntryType;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class EACommand extends Command {
    public EACommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length != 1) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/ea§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/ea <Spieler> §8» §7Verkürze den Ban des angegebenen Spielers").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ea ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else {
            UUID uuid = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (uuid != null) {
                IPlayerSync iPlayer = new IPlayerSync(uuid);
                IBan iBan = LostProxy.getInstance().getBanManager().getBan(uuid);
                if (iBan != null) {
                    if (iBan.getBanAppeal() == null) {
                        if (iBan.getDuration() != -1) {
                            if ((iBan.getEnd() - System.currentTimeMillis()) >= TimeUnit.DAYS.toMillis(3)) {
                                iBan.setEnd(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(3));

                                IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(uuid);
                                iBanHistory.addEntry(new IBanEntry(EBanEntryType.BAN_APPEAL_ENTRY, uuid, (commandSender instanceof ProxiedPlayer ? ((ProxiedPlayer) commandSender).getUniqueId().toString() : "console"), System.currentTimeMillis(), "EA-Command", 0, 0));

                                LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory);
                                LostProxy.getInstance().getBanManager().saveBan(iBan);
                                if (commandSender instanceof ProxiedPlayer) {
                                    LostProxy.getInstance().getPlayerManager().getIPlayerAsync(((ProxiedPlayer) commandSender).getUniqueId(), invoker -> LostProxy.getInstance().getTeamManager().sendEANotify(invoker.getPrefix() + invoker.getPlayerName(), iPlayer.getPrefix() + iPlayer.getPlayerName()));
                                } else {
                                    LostProxy.getInstance().getTeamManager().sendEANotify("§4Konsole", iPlayer.getPrefix() + iPlayer.getPlayerName());
                                }

                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Ban von " + iPlayer.getPrefix() + iPlayer.getPlayerName() + " §7läuft nun in §e3 Tagen §7ab§8.").build());
                            } else {
                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Da der Bann bereits in §e3 Tagen §cabläuft§8, §7kann der Bann §cnicht §7nochmal verkürzt werden§8.").build());
                            }
                                } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Ein §4permanenter §7Bann kann §cnicht §7verkürzt werden§8.").build());
                                }
                            } else {
                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Bann von " + iPlayer.getPrefix() + iPlayer.getPlayerName() + " §7wurde §cbereits §7verkürzt§8.").build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der Spieler " + iPlayer.getPrefix() + iPlayer.getPlayerName() + " §7ist §cnicht §7gebannt§8.").build());
                        }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                }
        }
    }
}
