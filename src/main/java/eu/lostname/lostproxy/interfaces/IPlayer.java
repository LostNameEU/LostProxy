/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:39:29
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * IPlayer.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces;

import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.bridge.player.ICloudOfflinePlayer;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;

import java.util.UUID;

public class IPlayer {

    private final UUID uniqueId;
    private String playerName, prefix, suffix, display, color, chatColor;
    private IPermissionGroup iPermissionGroup;
    private IPermissionUser iPermissionUser;
    private ICloudOfflinePlayer iCloudOfflinePlayer;
    private ICloudPlayer iCloudPlayer;
    private boolean online, exists;

    /**
     * @param uniqueId            the universally unique identiy of the requested player
     * @param playerName          the player's name
     * @param prefix              the prefix of the player's highest permission group
     * @param suffix              the suffix of the player's highest permission group
     * @param display             the display of the player's highest permission group
     * @param color               the color of the player's highest permission group
     * @param chatColor           the color which is used to write in chat of the player's highest permission group
     * @param iPermissionGroup    the player's highest permission group
     * @param iPermissionUser     the IPermissionUser which belongs to the requested player
     * @param iCloudOfflinePlayer the player's associated ICloudOfflinePlayer
     * @param iCloudPlayer        when player is online, the ICloudPlayer which belongs to the player
     * @param online              Value whether the player is online or not
     * @param exists              Value whether the player is recognized by the cloud
     */
    public IPlayer(UUID uniqueId, String playerName, String prefix, String suffix, String display, String color, String chatColor, IPermissionGroup iPermissionGroup, IPermissionUser iPermissionUser, ICloudOfflinePlayer iCloudOfflinePlayer, ICloudPlayer iCloudPlayer, boolean online, boolean exists) {
        this.uniqueId = uniqueId;
        this.playerName = playerName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.display = display;
        this.color = color;
        this.chatColor = chatColor;
        this.iPermissionGroup = iPermissionGroup;
        this.iPermissionUser = iPermissionUser;
        this.iCloudOfflinePlayer = iCloudOfflinePlayer;
        this.iCloudPlayer = iCloudPlayer;
        this.online = online;
        this.exists = exists;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getChatColor() {
        return chatColor;
    }

    public void setChatColor(String chatColor) {
        this.chatColor = chatColor;
    }

    public IPermissionGroup getiPermissionGroup() {
        return iPermissionGroup;
    }

    public void setiPermissionGroup(IPermissionGroup iPermissionGroup) {
        this.iPermissionGroup = iPermissionGroup;
    }

    public IPermissionUser getiPermissionUser() {
        return iPermissionUser;
    }

    public void setiPermissionUser(IPermissionUser iPermissionUser) {
        this.iPermissionUser = iPermissionUser;
    }

    public ICloudOfflinePlayer getiCloudOfflinePlayer() {
        return iCloudOfflinePlayer;
    }

    public void setiCloudOfflinePlayer(ICloudOfflinePlayer iCloudOfflinePlayer) {
        this.iCloudOfflinePlayer = iCloudOfflinePlayer;
    }

    public ICloudPlayer getiCloudPlayer() {
        return iCloudPlayer;
    }

    public void setiCloudPlayer(ICloudPlayer iCloudPlayer) {
        this.iCloudPlayer = iCloudPlayer;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}