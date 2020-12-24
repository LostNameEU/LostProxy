package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IFriendData;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FriendCommand extends Command {

    public FriendCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            IFriendData iFriendData = LostProxy.getInstance().getFriendManager().getFriendData(player.getUniqueId());

            if (strings.length == 0) {
                player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Benutzung von §c/friend§8:").build());
                player.sendMessage(new MessageBuilder("§8┃ §c/friend 2 §8» §7Zeigt Hilfeseite 2").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend 2").build());
                player.sendMessage(new MessageBuilder("§8┃ §c/friend add <Name> §8» §7Fügt jemanden als Freund hinzu").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend add ").build());
                player.sendMessage(new MessageBuilder("§8┃ §c/friend remove <Name> §8» §7Löst die Freundschaft mit jemanden auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend remove ").build());
                player.sendMessage(new MessageBuilder("§8┃ §c/friend accept <Name> §8» §7Nimmt Freundschaftsanfragen an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend accept ").build());
                player.sendMessage(new MessageBuilder("§8┃ §c/friend deny <Name> §8» §7Lehnt Freundschaftsanfragen ab").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend deny ").build());
                player.sendMessage(new MessageBuilder("§8┃ §c/friend list §8» §7Listet alle Freunde auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend list").build());
                player.sendMessage(new MessageBuilder("§8┃ §c/friend requests §8» §7Listet alle Freundschaftsanfragen auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend requests").build());
                player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
            } else if (strings.length == 1) {
                switch (strings[0]) {
                    case "2":
                        player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Hilfeseite 2§8:").build());
                        player.sendMessage(new MessageBuilder("§8┃ §c/friend acceptall §8» §7Nimmt alle Freundschaftsanfragen an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend acceptall").build());
                        player.sendMessage(new MessageBuilder("§8┃ §c/friend denyall §8» §7Lehnt alle Freundschaftsanfragen ab").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend denyall").build());
                        player.sendMessage(new MessageBuilder("§8┃ §c/friend broadcast <Nachricht> §8» §7Schicke eine Nachricht an alle Freunde").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend broadcast ").build());
                        player.sendMessage(new MessageBuilder("§8┃ §c/friend clear §8» §7Leert deine Freundesliste").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend clear").build());
                        player.sendMessage(new MessageBuilder("§8┃ §c/friend jump <Name> §8» §7Springt zu einem Freund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend clear").build());
                        player.sendMessage(new MessageBuilder("§8┃ §c/friend toggle §8» §7De- oder aktiviert Freundschaftsanfragen").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend toggle").build());
                        player.sendMessage(new MessageBuilder("§8┃ §c/friend togglenotify §8» §7De- oder aktiviert Benachrichtigungen").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend togglenotify").build());
                        player.sendMessage(new MessageBuilder("§8┃ §c/friend togglemsg §8» §7De- oder aktiviert Nachrichten von Freunden").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend togglenotify").build());
                        player.sendMessage(new MessageBuilder("§8┃ §c/friend togglejump §8» §7De- oder aktiviert Nachspringen von Freunden").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend togglejump").build());
                        player.sendMessage(new MessageBuilder("§8┃ §c/friend toggleonline §8» §7De- oder aktiviert den Onlinestatus vor anderen Freunden").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend toggleonline").build());
                        player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                        break;
                    case "list":
                        if (iFriendData.getFriends().size() > 0) {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Deine Freundeliste §8(§e" + iFriendData.getFriends().size() + "§8):").build());
                            List<String> onlineFriends = iFriendData.getFriends().keySet().stream().filter(filter -> new IPlayerSync(UUID.fromString(filter)).isOnline()).filter(filter -> LostProxy.getInstance().getFriendManager().getFriendData(UUID.fromString(filter)).canFriendsSeeOnlineStatusAllowed()).collect(Collectors.toList());
                            List<String> offlineFriends = iFriendData.getFriends().keySet().stream().filter(filter -> !new IPlayerSync(UUID.fromString(filter)).isOnline() || !LostProxy.getInstance().getFriendManager().getFriendData(UUID.fromString(filter)).canFriendsSeeOnlineStatusAllowed()).collect(Collectors.toList());

                            onlineFriends.forEach(online -> {
                                IPlayerSync iPlayer = new IPlayerSync(UUID.fromString(online));

                                TextComponent playerNameComponent = new MessageBuilder("§8┃ " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §8» ").build();
                                TextComponent serverComponent = new MessageBuilder("§e§n" + iPlayer.getICloudPlayer().getConnectedService().getServerName()).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend jump " + iPlayer.getPlayerName()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §eKlicke§8, §7um " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §7nachzuspringen§8.").build();
                                playerNameComponent.addExtra(serverComponent);

                                player.sendMessage(playerNameComponent);
                            });

                            offlineFriends.forEach(offline -> {
                                IPlayerSync iPlayer = new IPlayerSync(UUID.fromString(offline));
                                IFriendData friendData = LostProxy.getInstance().getFriendManager().getFriendData(iPlayer.getUniqueId());

                                TextComponent playerNameComponent = new MessageBuilder("§8┃ " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §8» ").build();
                                TextComponent extraComponent = null;

                                if (friendData.canFriendsSeeOnlineStatusAllowed()) {
                                    extraComponent = new MessageBuilder("§7zul. online am §e" + new SimpleDateFormat("dd.MM.yyyy").format(iFriendData.getLastLogoutTimestamp()) + " §7um §e" + new SimpleDateFormat("HH:mm:ss").format(iFriendData.getLastLogoutTimestamp()) + " §7Uhr").build();
                                } else {
                                    extraComponent = new MessageBuilder("§7Onlinestatus verborgen").build();
                                }
                                playerNameComponent.addExtra(extraComponent);

                                player.sendMessage(playerNameComponent);
                            });

                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Deine Freundesliste ist §cleer§8.").build());
                        }
                        break;
                    case "requests":
                        break;
                    case "acceptall":
                        break;
                    case "denyall":
                        break;
                    case "toggle":
                        break;
                    case "togglenotify":
                        break;
                    case "togglejump":
                        break;
                    case "toggleonline":
                        break;
                    default:
                        player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                }
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§8.").build());
        }
    }
}