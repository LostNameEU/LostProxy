package eu.lostname.lostproxy.manager;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.function.Consumer;

public class TeamManager {

    private final ArrayList<ProxiedPlayer> notificationOn;

    public TeamManager() {
        this.notificationOn = new ArrayList<>();
    }

    /**
     * @param player   the player who gets the notifications enabled
     * @param consumer a consumer which includes the feedback whether the adding to the list worked
     * @since 08.12.2020
     */
    public void enableNotifications(ProxiedPlayer player, Consumer<Boolean> consumer) {
        consumer.accept(notificationOn.add(player));
    }

    /**
     * @param player   the player who gets the notifications disabled
     * @param consumer a consumer which includes the feedback whether the removing from the list worked
     * @since 08.12.2020
     */
    public void disableNotifications(ProxiedPlayer player, Consumer<Boolean> consumer) {
        consumer.accept(notificationOn.remove(player));
    }

    /**
     * @param proxiedPlayer the player who has to be checked
     * @return whether the proxiedPlayer is in the notificationOn list
     */
    public boolean hasNotificationsEnabled(ProxiedPlayer proxiedPlayer) {
        return getNotificationOn().contains(proxiedPlayer);
    }

    public ArrayList<ProxiedPlayer> getNotificationOn() {
        return notificationOn;
    }
}
