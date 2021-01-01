package eu.lostname.lostproxy.manager;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.bridge.player.ICloudOfflinePlayer;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.utils.CloudServices;

import java.util.UUID;
import java.util.function.Consumer;

public class PlayerManager {

    @SuppressWarnings("UnstableApiUsage")
    public void getUUIDofPlayername(String playername, Consumer<UUID> consumer) {
        CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).getRegisteredPlayersAsync().onComplete(iCloudOfflinePlayers -> {
            ICloudOfflinePlayer offlinePlayer = null;
            for (ICloudOfflinePlayer registeredPlayers : iCloudOfflinePlayers) {
                if (registeredPlayers.getName().equalsIgnoreCase(playername))
                    offlinePlayer = registeredPlayers;
            }

            if (offlinePlayer != null) {
                consumer.accept(offlinePlayer.getUniqueId());
            } else {
                consumer.accept(null);
            }
        }).onFailure(throwable -> consumer.accept(null));
    }

    public void getCloudOfflinePlayer(UUID uniqueId, Consumer<ICloudOfflinePlayer> consumer) {
        CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).getOfflinePlayerAsync(uniqueId).onComplete(consumer).onFailure(throwable -> consumer.accept(null));
    }

    public void getCloudPlayer(UUID uniqueId, Consumer<ICloudPlayer> consumer) {
        CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).getOnlinePlayerAsync(uniqueId).onComplete(consumer::accept).onFailure(throwable -> consumer.accept(null));
    }

    public void getPermissionUser(UUID uniqueId, Consumer<IPermissionUser> consumer) {
        CloudServices.PERMISSION_MANAGEMENT.getUserAsync(uniqueId).onComplete(consumer).onFailure(throwable -> consumer.accept(null));
    }

    public void getPermissionGroup(UUID uniqueId, Consumer<IPermissionGroup> consumer) {
        getPermissionUser(uniqueId, iPermissionUser -> {
            consumer.accept(CloudServices.PERMISSION_MANAGEMENT.getHighestPermissionGroup(iPermissionUser));
        });
    }

    public void getIPlayerAsync(UUID uniqueId, Consumer<IPlayer> consumer) {
        getCloudOfflinePlayer(uniqueId, iCloudOfflinePlayer -> {
            getPermissionUser(uniqueId, iPermissionUser -> {
                getPermissionGroup(uniqueId, iPermissionGroup -> {
                    getCloudPlayer(uniqueId, iCloudPlayer -> {
                        IPlayer async = new IPlayer(uniqueId,
                                iCloudOfflinePlayer.getName(),
                                iPermissionGroup.getPrefix(),
                                iPermissionGroup.getSuffix(),
                                iPermissionGroup.getDisplay(),
                                iPermissionGroup.getColor(),
                                (iPermissionGroup.getProperties().contains("chatColor") ? iPermissionGroup.getProperties().getString("chatColor") : "§f"),
                                iPermissionGroup,
                                iPermissionUser,
                                iCloudOfflinePlayer,
                                iCloudPlayer,
                                iCloudPlayer != null,
                                iCloudOfflinePlayer != null);

                        consumer.accept(async);
                    });
                });
            });
        });
    }
}