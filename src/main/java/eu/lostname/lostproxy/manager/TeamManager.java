package eu.lostname.lostproxy.manager;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.function.Consumer;

public class TeamManager {

    private final ArrayList<ProxiedPlayer> notificationOn;

    public TeamManager() {
        this.notificationOn = new ArrayList<>();
    }


    public void enableNotifications(ProxiedPlayer player, Consumer<Boolean> consumer) {
        consumer.accept(notificationOn.add(player));
    }

    public void disableNotifications(ProxiedPlayer player, Consumer<Boolean> consumer) {
        consumer.accept(notificationOn.remove(player));
    }

    public ArrayList<ProxiedPlayer> getNotificationOn() {
        return notificationOn;
    }
}
