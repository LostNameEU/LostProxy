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
                LostProxy.getInstance().getTeamManager().disableNotifications(player, aBoolean -> {
                    if (aBoolean) {
                        player.sendMessage(new MessageBuilder(Prefix.NOTIFICATIONS + "§c✖").build());
                    } else {
                        player.sendMessage(new MessageBuilder(Prefix.NOTIFICATIONS + "§7Es ist ein §4Fehler §7aufgetreten§8. §7Bitte wende dich an den zuständigen §eSachbearbeiter§8.").build());
                    }
                });
            } else {
                LostProxy.getInstance().getTeamManager().enableNotifications(player, aBoolean -> {
                    if (aBoolean) {
                        player.sendMessage(new MessageBuilder(Prefix.NOTIFICATIONS + "§a✔").build());
                    } else {
                        player.sendMessage(new MessageBuilder(Prefix.NOTIFICATIONS + "§7Es ist ein §4Fehler §7aufgetreten§8. §7Bitte wende dich an den zuständigen §eSachbearbeiter§8.").build());
                    }
                });
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.NOTIFICATIONS + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§8.").build());
        }
    }
}