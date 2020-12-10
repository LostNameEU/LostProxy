package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;

public class KickHistoryClearCommand extends Command {

    public KickHistoryClearCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/khclear§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/khclear <Spieler> §8» §7Leert die Kick-History des angegebenen Spielers").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/khclear ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {

        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
        }
    }
}
