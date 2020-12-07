package eu.lostname.lostproxy.manager;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

public class TeamManager {

    private final ArrayList<ProxiedPlayer> notificationOn;

    public TeamManager() {
        this.notificationOn = new ArrayList<>();
    }

    public ArrayList<ProxiedPlayer> getNotificationOn() {
        return notificationOn;
    }
}
