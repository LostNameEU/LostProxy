package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Random;

public class TCCommand extends Command {

    public TCCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            if (strings.length == 0) {
                player.sendMessage(new MessageBuilder(Prefix.TMS + "Benutzung von §a/tc§8:").build());
                player.sendMessage(new MessageBuilder("§8┃ §a/tc [Nachricht] §8» §7Schreibe in den TeamChat").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tc ").build());
                player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
            } else {
                if (LostProxy.getInstance().getTeamManager().isLoggedIn(player)) {
                    String msg = LostProxy.getInstance().formatArrayToString(0, strings);

                    LostProxy.getInstance().getPlayerManager().getIPlayerAsync(player.getUniqueId(), iPlayer -> LostProxy.getInstance().getTeamManager().getLoggedIn().forEach(all -> all.sendMessage(new MessageBuilder(Prefix.TMS + iPlayer.getPrefix() + player.getName() + " §8» §" + new Random().nextInt(9) + msg).build())));
                } else {
                    player.sendMessage(new MessageBuilder(Prefix.TMS + "Du bist §cnicht §7eingeloggt§8.").build());
                }
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.TMS + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§8.").build());
        }
    }
}
