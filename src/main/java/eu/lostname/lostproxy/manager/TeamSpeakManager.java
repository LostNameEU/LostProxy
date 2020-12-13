package eu.lostname.lostproxy.manager;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.listener.teamspeak.TeamSpeakListeners;
import eu.lostname.lostproxy.utils.TSServerGroups;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class TeamSpeakManager {

    private final TS3Config ts3Config;
    private final TS3Query ts3Query;
    private TS3ApiAsync api;

    public TeamSpeakManager() {
        this.ts3Config = new TS3Config();
        ts3Config.setFloodRate(TS3Query.FloodRate.UNLIMITED);
        ts3Config.setHost(LostProxy.getInstance().getProperty().get("cfg", "ts.hostname"));
        ts3Config.setQueryPort(Integer.parseInt(LostProxy.getInstance().getProperty().get("cfg", "ts.queryPort")));
        ts3Config.setEnableCommunicationsLogging(true);

        this.ts3Query = new TS3Query(ts3Config);
        ts3Query.connect();

        if (ts3Query.isConnected()) {
            api = ts3Query.getAsyncApi();

            api.login(LostProxy.getInstance().getProperty().get("cfg", "ts.username"), LostProxy.getInstance().getProperty().get("cfg", "ts.password"));
            api.selectVirtualServerByPort(Integer.parseInt(LostProxy.getInstance().getProperty().get("cfg", "ts.virtualServerPort")));
            api.setNickname(LostProxy.getInstance().getProperty().get("cfg", "ts.nickname"));
            api.registerAllEvents();
            api.addTS3Listeners(new TeamSpeakListeners());
        }
    }

    public void setServerGroupsUsingInGamePermissions(ClientInfo clientInfo, IPlayer iPlayer) {
        api.addClientToServerGroup(TSServerGroups.VERIFIED, clientInfo.getDatabaseId());
        api.addClientToServerGroup(iPlayer.getiPermissionGroup().getProperties().getInt("tsGroupId"), clientInfo.getDatabaseId());
    }

    public void setHead(ClientInfo clientInfo, String playerName) {
        URL url = null;
        try {
            url = new URL("https://minotar.net/helm/" + playerName + "/16.png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(Objects.requireNonNull(url).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int i = 0;

        while (true) {
            try {
                if (inputStream != null && -1 == (i = inputStream.read(buf))) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            byteArrayOutputStream.write(buf, 0, i);
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] response = byteArrayOutputStream.toByteArray();
        api.uploadIconDirect(response).onSuccess(iconId -> api.addClientPermission(clientInfo.getDatabaseId(), "i_icon_id", iconId.intValue(), false));
    }

    public void removeHead(ClientInfo clientInfo) {
        api.deleteClientPermission(clientInfo.getDatabaseId(), "i_icon_id");
    }

    public void resetAllServerGroups(ClientInfo clientInfo) {
        getSecretPermissionIdentitiesList(strings -> {
            boolean isSecretPermisisonIdentitiy = strings.contains(clientInfo.getUniqueIdentifier());

            if (isSecretPermisisonIdentitiy) {
                if (!clientInfo.isInServerGroup(TSServerGroups.ALL_PERMISSIONS)) {
                    api.addClientToServerGroup(TSServerGroups.ALL_PERMISSIONS, clientInfo.getDatabaseId()).onSuccess(unused -> api.sendPrivateMessage(clientInfo.getId(), "Da du in der geheimen Tabelle stehst und [b]nicht[/b] die Servergruppe hattest, die dir alle Rechte gibt, hast du sie nun erhalten!"));
                }
            }
            if (clientInfo.getServerGroups().length > 1) {
                for (int serverGroup : clientInfo.getServerGroups()) {
                    if (serverGroup == TSServerGroups.ALL_PERMISSIONS) {
                        if (!isSecretPermisisonIdentitiy) {
                            api.removeClientFromServerGroup(serverGroup, clientInfo.getDatabaseId());
                        }
                    } else {
                        api.removeClientFromServerGroup(serverGroup, clientInfo.getDatabaseId());
                    }
                }
            } else if (clientInfo.getServerGroups()[0] != TSServerGroups.GUEST && clientInfo.getServerGroups()[0] != TSServerGroups.ALL_PERMISSIONS) {
                api.removeClientFromServerGroup(clientInfo.getServerGroups()[0], clientInfo.getDatabaseId());
            }
        });
    }

    public void getSecretPermissionIdentitiesList(Consumer<ArrayList<String>> consumer) {
        ArrayList<String> identities = new ArrayList<>();
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection("teamspeakSecretIdentities").find().forEach(document -> identities.add(document.getString("_id")), (unused, throwable) -> {
            consumer.accept(identities);
        });
    }

    public ArrayList<Integer> getServerGroupsAsList(ClientInfo clientInfo) {
        ArrayList<Integer> serverGroups = new ArrayList<>();
        for (int serverGroup : clientInfo.getServerGroups()) {
            serverGroups.add(serverGroup);
        }

        return serverGroups;
    }

    public void getTeamRank(ClientInfo clientInfo, Consumer<ServerGroup> consumer) {
        ArrayList<Integer> serverGroups = getServerGroupsAsList(clientInfo);

        if (serverGroups.contains(TSServerGroups.MANAGER)) {
            getServerGroup(TSServerGroups.MANAGER, consumer);
        } else if (serverGroups.contains(TSServerGroups.BUILDER)) {
            getServerGroup(TSServerGroups.BUILDER, consumer);
        } else if (serverGroups.contains(TSServerGroups.CONTENT)) {
            getServerGroup(TSServerGroups.BUILDER, consumer);
        } else if (serverGroups.contains(TSServerGroups.DEVELOPER)) {
            getServerGroup(TSServerGroups.BUILDER, consumer);
        } else if (serverGroups.contains(TSServerGroups.GFX)) {
            getServerGroup(TSServerGroups.BUILDER, consumer);
        } else if (serverGroups.contains(TSServerGroups.HEAD_BUILDER)) {
            getServerGroup(TSServerGroups.BUILDER, consumer);
        } else if (serverGroups.contains(TSServerGroups.HEAD_GFX)) {
            getServerGroup(TSServerGroups.BUILDER, consumer);
        } else if (serverGroups.contains(TSServerGroups.HELPER)) {
            getServerGroup(TSServerGroups.BUILDER, consumer);
        } else if (serverGroups.contains(TSServerGroups.SENIOR_CONTENT)) {
            getServerGroup(TSServerGroups.BUILDER, consumer);
        } else if (serverGroups.contains(TSServerGroups.SENIOR_STAFF)) {
            getServerGroup(TSServerGroups.BUILDER, consumer);
        } else if (serverGroups.contains(TSServerGroups.STAFF)) {
            getServerGroup(TSServerGroups.BUILDER, consumer);
        } else {
            consumer.accept(null);
        }
    }

    public void getClient(int clientId, Consumer<ClientInfo> consumer) {
        api.getClientInfo(clientId).onSuccess(consumer::accept).onFailure(e -> consumer.accept(null));
    }

    public void getClient(String identifier, Consumer<ClientInfo> consumer) {
        api.getClientByUId(identifier).onSuccess(consumer::accept).onFailure(e -> consumer.accept(null));
    }

    public void getServerGroup(int serverGroupId, Consumer<ServerGroup> consumer) {
        api.getServerGroups().onSuccess(serverGroups -> {
            for (ServerGroup serverGroup : serverGroups) {
                if (serverGroup.getId() == serverGroupId) {
                    consumer.accept(serverGroup);
                    return;
                }
            }
            consumer.accept(null);
        }).onFailure(e -> consumer.accept(null));
    }

    public TS3Config getTs3Config() {
        return ts3Config;
    }

    public TS3Query getTs3Query() {
        return ts3Query;
    }

    public TS3ApiAsync getApi() {
        return api;
    }
}
