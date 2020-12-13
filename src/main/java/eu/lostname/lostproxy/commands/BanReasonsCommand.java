package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;

public class BanReasonsCommand extends Command {
    public BanReasonsCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung von §c/banreasons§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/banreasons list §8» §7Liste dir alle Bangruende auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons list").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/banreasons add <Name> <Zeit> <Zeiteinheit> <Permission> §8» §7Liste dir alle Bangruende auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons add NAME ZEIT ZEITEINHEIT PERMISSION").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/banreasons <ID> §8» §7Zeige Informationen über einen Banngrund an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons ID").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/banreasons <ID> set <id,name,time,timeunit,permission> <Wert> §8» §7Bearbeite einen Banngrund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons ID set ").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/banreasons <ID> delete §8» §7Lösche einen Banngrund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons ID set ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        }
    }
}
