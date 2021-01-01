package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class NotifyCommand extends Command {

    public NotifyCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            if (LostProxy.getInstance().getTeamManager().hasNotificationsEnabled(player)) {
                if (LostProxy.getInstance().getTeamManager().disableNotifications(player)) {
                    player.sendMessage(new MessageBuilder(Prefix.NOTIFICATIONS + "§c✖").build());
                } else {
                    player.sendMessage(new MessageBuilder(Prefix.NOTIFICATIONS + "Beim §eVerarbeiten §7dieser Anfrage §7ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                }
            } else {
                if (LostProxy.getInstance().getTeamManager().enableNotifications(player)) {
                    player.sendMessage(new MessageBuilder(Prefix.NOTIFICATIONS + "§a✔").build());
                } else {
                    player.sendMessage(new MessageBuilder(Prefix.NOTIFICATIONS + "Beim §eVerarbeiten §7dieser Anfrage §7ist ein §4Fehler §7aufgetreten§8. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                }
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.NOTIFICATIONS + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§8.").build());
        }
    }
}