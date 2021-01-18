/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 18.01.2021 @ 23:20:35
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * FriendCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.ELocale;
import eu.lostname.lostproxy.interfaces.IFriendData;
import eu.lostname.lostproxy.interfaces.IPlayerSync;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FriendCommand extends Command {

    public FriendCommand(String name, String s, String friends) {
        super(name, s, friends);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            IFriendData iFriendData = LostProxy.getInstance().getFriendManager().getFriendData(player.getUniqueId());
            IPlayerSync iPlayer = new IPlayerSync(player.getUniqueId());
            ELocale locale = LostProxy.getInstance().getLocaleManager().getLocaleData(player).getLocale();

            if (strings.length == 0) {
                player.sendMessage(new MessageBuilder(Prefix.FRIENDS + locale.getMessage("commands.usage").replaceAll("%cmd%", "§c/friend")).build());
                player.sendMessage(new MessageBuilder("§8» §c/friend 2 §8" + Prefix.DASH + " §7" + locale.getMessage("friendcommand.2.usage.description")).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend 2").build());
                player.sendMessage(new MessageBuilder("§8» §c/friend add <Name> §8" + Prefix.DASH + " §7" + locale.getMessage("friendcommand.add.usage.description")).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend add ").build());
                player.sendMessage(new MessageBuilder("§8» §c/friend remove <Name> §8" + Prefix.DASH + " §7" + locale.getMessage("friendcommand.remove.usage.description")).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend remove ").build());
                player.sendMessage(new MessageBuilder("§8» §c/friend accept <Name> §8" + Prefix.DASH + " §7" + locale.getMessage("friendcommand.accept.usage.description")).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend accept ").build());
                player.sendMessage(new MessageBuilder("§8» §c/friend deny <Name> §8" + Prefix.DASH + " §7" + locale.getMessage("friendcommand.deny.usage.description")).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend deny ").build());
                player.sendMessage(new MessageBuilder("§8» §c/friend list §8" + Prefix.DASH + " §7Listet alle Freunde auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend list").build());
                player.sendMessage(new MessageBuilder("§8» §c/friend requests §8" + Prefix.DASH + " §7Listet alle Freundschaftsanfragen auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend requests").build());
                player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
            } else if (strings.length == 1) {
                switch (strings[0]) {
                    case "2":
                        player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Hilfeseite 2§8:").build());
                        player.sendMessage(new MessageBuilder("§8» §c/friend acceptall §8" + Prefix.DASH + " §7Nimmt alle Freundschaftsanfragen an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend acceptall").build());
                        player.sendMessage(new MessageBuilder("§8» §c/friend denyall §8" + Prefix.DASH + " §7Lehnt alle Freundschaftsanfragen ab").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend denyall").build());
                        player.sendMessage(new MessageBuilder("§8» §c/friend broadcast [Nachricht] §8" + Prefix.DASH + " §7Schicke eine Nachricht an alle Freunde").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend broadcast ").build());
                        player.sendMessage(new MessageBuilder("§8» §c/friend clear §8" + Prefix.DASH + " §7Leert deine Freundesliste").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend clear").build());
                        player.sendMessage(new MessageBuilder("§8» §c/friend jump <Name> §8" + Prefix.DASH + " §7Springt zu einem Freund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend clear").build());
                        player.sendMessage(new MessageBuilder("§8» §c/friend toggle §8" + Prefix.DASH + " §7De- oder aktiviert Freundschaftsanfragen").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend toggle").build());
                        player.sendMessage(new MessageBuilder("§8» §c/friend togglenotify §8" + Prefix.DASH + " §7De- oder aktiviert Benachrichtigungen").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend togglenotify").build());
                        player.sendMessage(new MessageBuilder("§8» §c/friend togglemsg §8" + Prefix.DASH + " §7De- oder aktiviert Nachrichten von Freunden").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend togglemsg").build());
                        player.sendMessage(new MessageBuilder("§8» §c/friend togglejump §8" + Prefix.DASH + " §7De- oder aktiviert Nachspringen von Freunden").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend togglejump").build());
                        player.sendMessage(new MessageBuilder("§8» §c/friend toggleonline §8" + Prefix.DASH + " §7De- oder aktiviert den Onlinestatus vor anderen Freunden").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend toggleonline").build());
                        player.sendMessage(new MessageBuilder("§8» §c/friend info <Name> §8" + Prefix.DASH + " §7Zeigt Informationen über einen Freund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend info ").build());
                        player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                        break;
                    case "list":
                        if (iFriendData.getFriends().size() > 0) {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Deine Freundeliste §8(§e" + iFriendData.getFriends().size() + "§8):").build());
                            List<String> onlineFriends = iFriendData.getFriends().keySet().stream().filter(filter -> new IPlayerSync(UUID.fromString(filter)).isOnline()).filter(filter -> LostProxy.getInstance().getFriendManager().getFriendData(UUID.fromString(filter)).canFriendsSeeOnlineStatusAllowed()).collect(Collectors.toList());
                            List<String> offlineFriends = iFriendData.getFriends().keySet().stream().filter(filter -> !new IPlayerSync(UUID.fromString(filter)).isOnline() || !LostProxy.getInstance().getFriendManager().getFriendData(UUID.fromString(filter)).canFriendsSeeOnlineStatusAllowed()).collect(Collectors.toList());

                            onlineFriends.forEach(online -> {
                                IPlayerSync friendiPlayer = new IPlayerSync(UUID.fromString(online));

                                TextComponent playerNameComponent = new MessageBuilder("§8» " + friendiPlayer.getDisplay() + friendiPlayer.getPlayerName() + " §8" + Prefix.DASH + " ").build();
                                TextComponent serverComponent = new MessageBuilder("§e§n" + friendiPlayer.getICloudPlayer().getConnectedService().getServerName()).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend jump " + friendiPlayer.getPlayerName()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §eKlicke§8, §7um " + friendiPlayer.getDisplay() + friendiPlayer.getPlayerName() + " §7nachzuspringen§8.").build();
                                playerNameComponent.addExtra(serverComponent);

                                player.sendMessage(playerNameComponent);
                            });

                            offlineFriends.forEach(offline -> {
                                IPlayerSync friendiPlayer = new IPlayerSync(UUID.fromString(offline));
                                IFriendData friendData = LostProxy.getInstance().getFriendManager().getFriendData(friendiPlayer.getUniqueId());

                                TextComponent playerNameComponent = new MessageBuilder("§8» " + friendiPlayer.getDisplay() + friendiPlayer.getPlayerName() + " §8" + Prefix.DASH + " ").build();
                                TextComponent extraComponent;

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
                        if (iFriendData.getRequests().size() > 0) {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Freundschaftsanfragen §8(§e" + iFriendData.getRequests().size() + "§8):").build());
                            iFriendData.getRequests().keySet().forEach(all -> {
                                IPlayerSync friendiPlayer = new IPlayerSync(UUID.fromString(all));

                                TextComponent playerNameComponent = new MessageBuilder("§8» " + friendiPlayer.getDisplay() + friendiPlayer.getPlayerName() + " §8" + Prefix.DASH + " ").build();
                                TextComponent acceptComponent = new MessageBuilder("§a§l✔").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend accept " + friendiPlayer.getPlayerName()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §eKlicke§8, §7um diese Freundschaftsanfrage §aanzunehmen§8.").build();
                                TextComponent seperateComponent = new MessageBuilder(" §8| ").build();
                                TextComponent denyComponent = new MessageBuilder("§c§l✖").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend deny " + friendiPlayer.getPlayerName()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §eKlicke§8, §7um diese Freundschaftsanfrage §cabzulehnen§8.").build();

                                playerNameComponent.addExtra(acceptComponent);
                                playerNameComponent.addExtra(seperateComponent);
                                playerNameComponent.addExtra(denyComponent);

                                player.sendMessage(playerNameComponent);
                            });
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du hast §ckeine §7Freundschaftsanfragen §7erhalten§8.").build());
                        }
                        break;
                    case "acceptall":
                        if (iFriendData.getRequests().size() > 0) {
                            iFriendData.getRequests().keySet().forEach(request -> {
                                IPlayerSync requestIPlayer = new IPlayerSync(UUID.fromString(request));

                                iFriendData.addFriend(requestIPlayer.getUniqueId());
                                iFriendData.save();

                                IFriendData requestIFriendData = LostProxy.getInstance().getFriendManager().getFriendData(requestIPlayer.getUniqueId());
                                requestIFriendData.addFriend(player.getUniqueId());
                                requestIFriendData.save();

                                player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du bist nun mit " + requestIPlayer.getDisplay() + requestIPlayer.getPlayerName() + " §7befreundet§8.").build());

                                if (requestIPlayer.isOnline())
                                    ProxyServer.getInstance().getPlayer(requestIPlayer.getUniqueId()).sendMessage(new MessageBuilder(Prefix.FRIENDS + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §7hat deine Freundschaftsanfrage §aakzeptiert§8.").build());
                            });
                            iFriendData.getRequests().clear();
                            iFriendData.save();
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du hast §ckeine §7Freundschaftsanfragen §7erhalten§8.").build());
                        }
                        break;
                    case "denyall":
                        if (iFriendData.getRequests().size() > 0) {
                            iFriendData.getRequests().keySet().forEach(request -> {
                                IPlayerSync requestIPlayer = new IPlayerSync(UUID.fromString(request));

                                player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du hast die Freundschaftsanfrage von " + requestIPlayer.getDisplay() + requestIPlayer.getPlayerName() + " §cabgelehnt§8.").build());

                                if (requestIPlayer.isOnline())
                                    ProxyServer.getInstance().getPlayer(requestIPlayer.getUniqueId()).sendMessage(new MessageBuilder(Prefix.FRIENDS + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §7hat deine Freundschaftsanfrage §cabgelehnt§8.").build());
                            });
                            iFriendData.getRequests().clear();
                            iFriendData.save();
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du hast §ckeine §7Freundschaftsanfragen §7erhalten§8.").build());
                        }
                        break;
                    case "toggle":
                        iFriendData.setFriendRequestsAllowed(!iFriendData.canFriendsSeeOnlineStatusAllowed());
                        iFriendData.save();

                        if (iFriendData.canFriendsSeeOnlineStatusAllowed()) {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du erhältst nun §awieder §7Freundschaftsanfragen§8.").build());
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du erhältst nun §ckeine §7Freundschaftsanfragen mehr§8.").build());
                        }
                        break;
                    case "togglenotify":
                        iFriendData.setNotifyMessagesEnabled(!iFriendData.areNotifyMessagesEnabled());
                        iFriendData.save();

                        if (iFriendData.areNotifyMessagesEnabled()) {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du erhältst nun §awieder §7Benachrichtigungen§8.").build());
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du erhältst nun §ckeine §7Benachrichtigungen mehr§8.").build());
                        }
                        break;
                    case "togglemsg":
                        iFriendData.setSentMessagesAllowed(!iFriendData.canFriendsSentMessages());
                        iFriendData.save();

                        if (iFriendData.canFriendsSentMessages()) {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Freunde können dir nun wieder §eNachrichten §7schreiben§8.").build());
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Freunde können dir nun §ckeine §eNachrichten §7mehr schreiben§8.").build());
                        }
                        break;
                    case "togglejump":
                        iFriendData.setFriendJumpAllowed(!iFriendData.isFriendJumpAllowed());
                        iFriendData.save();

                        if (iFriendData.isFriendJumpAllowed()) {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Freunde können dir nun §awieder §7hinterher springen§8.").build());
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Freunde können dir nun §cnicht §7mehr hinterher springen§8.").build());
                        }
                        break;
                    case "toggleonline":
                        iFriendData.setFriendsSeeOnlineStatusAllowed(!iFriendData.canFriendsSeeOnlineStatusAllowed());
                        iFriendData.save();

                        if (iFriendData.canFriendsSeeOnlineStatusAllowed()) {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Freunde sehen deinen Onlinestatus nun §awieder§8.").build());
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Freunde sehen deinen Onlinestatus §cnicht §7mehr§8.").build());
                        }
                        break;
                    case "clear":
                        iFriendData.getFriends().keySet().forEach(friend -> {
                            IPlayerSync friendIPlayer = new IPlayerSync(UUID.fromString(friend));
                            IFriendData friendData = LostProxy.getInstance().getFriendManager().getFriendData(friendIPlayer.getUniqueId());

                            friendData.removeFriend(player.getUniqueId());
                            friendData.save();

                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Die Freundschaft mit " + friendIPlayer.getDisplay() + friendIPlayer.getPlayerName() + " §7wurde §caufgelöst§8.").build());

                            if (friendIPlayer.isOnline())
                                ProxyServer.getInstance().getPlayer(friendIPlayer.getUniqueId()).sendMessage(new MessageBuilder(Prefix.FRIENDS + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §7hat die Freundschaft §caufgelöst§8.").build());
                        });
                        iFriendData.getFriends().clear();
                        iFriendData.save();
                    case "party":
                        player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Diese Funktion folgt bald").build());
                    default:
                        player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                }
            } else if (strings.length == 2) {
                String argument = strings[1];
                UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(argument);
                switch (strings[0]) {
                    case "add":
                        if (!argument.equalsIgnoreCase(player.getName())) {
                            if (targetUUID != null) {
                                IPlayerSync targetIPlayer = new IPlayerSync(targetUUID);
                                IFriendData targetFriendData = LostProxy.getInstance().getFriendManager().getFriendData(targetUUID);
                                if (!iFriendData.isFriend(targetUUID)) {
                                    if (!targetFriendData.haveRequest(player.getUniqueId())) {
                                        if (targetFriendData.areFriendRequestsAllowed()) {
                                            targetFriendData.addRequest(player.getUniqueId());
                                            targetFriendData.save();

                                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du hast " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7eine §eFreundschaftsanfrage §7gesendet§8.").build());

                                            if (targetIPlayer.isOnline()) {
                                                TextComponent informationComponent = new MessageBuilder(Prefix.FRIENDS + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §7hat dir eine Freundschaftsanfrage gesendet§8. ").build();
                                                TextComponent acceptComponent = new MessageBuilder("§a§l✔").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend accept " + player.getName()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §eKlicke§8, §7um diese Freundschaftsanfrage §aanzunehmen§8.").build();
                                                TextComponent seperateComponent = new MessageBuilder(" §8| ").build();
                                                TextComponent denyComponent = new MessageBuilder("§c§l✖").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend deny " + player.getName()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §eKlicke§8, §7um diese Freundschaftsanfrage §cabzulehnen§8.").build();

                                                informationComponent.addExtra(acceptComponent);
                                                informationComponent.addExtra(seperateComponent);
                                                informationComponent.addExtra(denyComponent);

                                                ProxyServer.getInstance().getPlayer(targetUUID).sendMessage(informationComponent);
                                            }
                                        } else {
                                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7hat Freundschaftsanfragen §cdeaktiviert§8.").build());
                                        }
                                    } else {
                                        player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du hast " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §cbereits §7eine Freundschaftsanfrage gesendet§8.").build());
                                    }
                                } else {
                                    player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du bist §cbereits §7mit " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7befreundet§8.").build());
                                }
                            } else {
                                player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                            }
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du kannst §cnicht §7mit dir selbst befreundet sein§8.").build());
                        }
                        break;
                    case "remove":
                        if (!argument.equalsIgnoreCase(player.getName())) {
                            if (targetUUID != null) {
                                IPlayerSync targetIPlayer = new IPlayerSync(targetUUID);
                                IFriendData targetFriendData = LostProxy.getInstance().getFriendManager().getFriendData(targetUUID);
                                if (iFriendData.isFriend(targetUUID)) {
                                    targetFriendData.removeFriend(player.getUniqueId());
                                    targetFriendData.save();

                                    iFriendData.removeFriend(targetUUID);
                                    iFriendData.save();

                                    player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Die Freundschaft mit " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " wurde §caufgelöst§8.").build());

                                    if (targetIPlayer.isOnline()) {
                                        ProxyServer.getInstance().getPlayer(targetUUID).sendMessage(new MessageBuilder(Prefix.FRIENDS + "Die Freundschaft mit " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §7wurde §caufgelöst§8.").build());
                                    }
                                } else {
                                    player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du bist §cnicht §7mit " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7befreundet§8.").build());
                                }
                            } else {
                                player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                            }
                        }
                        break;
                    case "accept":
                        if (targetUUID != null) {
                            IPlayerSync targetIPlayer = new IPlayerSync(targetUUID);
                            if (iFriendData.getRequests().containsKey(targetUUID.toString())) {
                                iFriendData.removeRequest(targetUUID);
                                iFriendData.addFriend(targetUUID);
                                iFriendData.save();

                                player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du bist nun mit " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7befreundet§8.").build());

                                IFriendData targetIFriendData = LostProxy.getInstance().getFriendManager().getFriendData(targetUUID);
                                targetIFriendData.addFriend(player.getUniqueId());
                                targetIFriendData.save();

                                if (targetIPlayer.isOnline())
                                    ProxyServer.getInstance().getPlayer(targetUUID).sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du bist nun mit " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §7befreundet§8.").build());
                            } else {
                                player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du hast §ckeine §7Freundschaftsanfrage von " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7erhalten§8.").build());
                            }
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                        }
                        break;
                    case "deny":
                        if (targetUUID != null) {
                            IPlayerSync targetIPlayer = new IPlayerSync(targetUUID);

                            if (iFriendData.haveRequest(targetUUID)) {
                                iFriendData.removeRequest(targetUUID);
                                iFriendData.save();

                                player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du hast die Freundschaftsanfrage von " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §cabgelehnt§8.").build());

                                if (targetIPlayer.isOnline())
                                    ProxyServer.getInstance().getPlayer(targetUUID).sendMessage(new MessageBuilder(Prefix.FRIENDS + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §7hat deine Freundschaftsanfrage §cabgelehnt§8.").build());
                            } else {
                                player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du hast §ckeine §7Freundschaftsanfrage von " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7bekommen§8.").build());
                            }
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                        }
                        break;
                    case "jump":
                        if (targetUUID != null) {
                            IPlayerSync targetIPlayer = new IPlayerSync(targetUUID);

                            if (iFriendData.isFriend(targetUUID)) {
                                IFriendData targetIFriendData = LostProxy.getInstance().getFriendManager().getFriendData(targetUUID);

                                if (targetIFriendData.isFriendJumpAllowed()) {
                                    if (targetIPlayer.isOnline()) {
                                        ServerInfo targetServer = ProxyServer.getInstance().getPlayer(targetUUID).getServer().getInfo();

                                        if (!targetServer.getName().equalsIgnoreCase(player.getServer().getInfo().getName())) {
                                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du wirst nun mit dem Server von " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7verbunden§8.").build());
                                            player.connect(targetServer);

                                            if (targetIFriendData.areNotifyMessagesEnabled())
                                                ProxyServer.getInstance().getPlayer(targetUUID).sendMessage(new MessageBuilder(Prefix.FRIENDS + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §7ist zu dir §egesprungen§8.").build());
                                        } else {
                                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du befindest dich §cbereits §7auf dem Server von " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + "§8.").build());
                                        }
                                    } else {
                                        player.sendMessage(new MessageBuilder(Prefix.FRIENDS + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7ist §cnicht §7online§8.").build());
                                    }
                                } else {
                                    player.sendMessage(new MessageBuilder(Prefix.FRIENDS + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7hat das Nachspringen §cdeaktiviert§8.").build());
                                }
                            } else {
                                player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du bist §cnicht §7mit " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7befreundet§8.").build());
                            }
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                        }
                        break;
                    case "broadcast":
                        List<String> sortedFriends = iFriendData.getFriends().keySet().stream().filter(filter -> new IPlayerSync(UUID.fromString(filter)).isOnline() && LostProxy.getInstance().getFriendManager().getFriendData(UUID.fromString(filter)).canFriendsSentMessages()).collect(Collectors.toList());

                        if (sortedFriends.size() > 0) {
                            sortedFriends.forEach(sortedFriend -> {
                                UUID sortedFriendUUID = UUID.fromString(sortedFriend);
                                IPlayerSync sortedFriendIPlayer = new IPlayerSync(sortedFriendUUID);
                                ProxyServer.getInstance().getPlayer(sortedFriendUUID).sendMessage(new MessageBuilder(Prefix.FRIENDS + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §8➡ " + sortedFriendIPlayer.getDisplay() + sortedFriendIPlayer.getPlayerName() + " §8» §e" + argument).build());
                            });
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Deine Nachricht wurde an §e" + sortedFriends.size() + " Freunde §7versendet§8.").build());
                        } else {
                            player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Deine Nachricht konnte §cnicht §7zugestellt werden§8. §7Sind vielleicht keine Freunde online oder haben die Freunde, die online sind, Nachrichten ausgeschaltet§8?").build());
                        }
                        break;
                    default:
                        player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                        break;
                }
            } else {
                if (strings[0].equalsIgnoreCase("broadcast")) {
                    List<String> sortedFriends = iFriendData.getFriends().keySet().stream().filter(filter -> new IPlayerSync(UUID.fromString(filter)).isOnline() && LostProxy.getInstance().getFriendManager().getFriendData(UUID.fromString(filter)).canFriendsSentMessages()).collect(Collectors.toList());

                    if (sortedFriends.size() > 0) {
                        String message = LostProxy.getInstance().formatArrayToString(1, strings);

                        sortedFriends.forEach(sortedFriend -> {
                            UUID sortedFriendUUID = UUID.fromString(sortedFriend);
                            IPlayerSync sortedFriendIPlayer = new IPlayerSync(sortedFriendUUID);
                            ProxyServer.getInstance().getPlayer(sortedFriendUUID).sendMessage(new MessageBuilder(Prefix.FRIENDS + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §8➡ " + sortedFriendIPlayer.getDisplay() + sortedFriendIPlayer.getPlayerName() + " §8» §e" + message).build());
                        });
                        player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Deine Nachricht wurde an §e" + sortedFriends.size() + " Freunde §7versendet§8.").build());
                    } else {
                        player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Deine Nachricht konnte §cnicht §7zugestellt werden§8. §7Sind vielleicht keine Freunde online oder haben die Freunde, die online sind, Nachrichten ausgeschaltet§8?").build());
                    }
                } else {
                    player.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                }
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.FRIENDS + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§8.").build());
        }
    }
}