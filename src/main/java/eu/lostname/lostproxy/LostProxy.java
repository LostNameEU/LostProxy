package eu.lostname.lostproxy;

import com.google.gson.Gson;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.syncproxy.AbstractSyncProxyManagement;
import eu.lostname.lostproxy.commands.*;
import eu.lostname.lostproxy.databases.LostProxyDatabase;
import eu.lostname.lostproxy.listener.PlayerDisconnectListener;
import eu.lostname.lostproxy.listener.PostLoginListener;
import eu.lostname.lostproxy.manager.*;
import eu.lostname.lostproxy.utils.CloudServices;
import net.md_5.bungee.api.plugin.Plugin;

public class LostProxy extends Plugin {

    private static LostProxy instance;

    private LostProxyDatabase database;
    private Gson gson;
    private LinkageManager linkageManager;
    private PlayerManager playerManager;
    private TeamSpeakManager teamSpeakManager;
    private HistoryManager historyManager;
    private TeamManager teamManager;

    public static LostProxy getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        this.gson = new Gson();
        this.database = new LostProxyDatabase("LostProxy", "BIi7>$_GRP2;Y%+jB%1t_F/@>Y.KT):c%Q\"F3=}-O/,-D|!TeTMY<>86+n3D:1tH=fD2/?%qNylX!y=&:kaT\\g\"}xJl%6#YuGCJ", "LostProxy");
        this.linkageManager = new LinkageManager(gson);
        this.playerManager = new PlayerManager();
        this.historyManager = new HistoryManager();
        this.teamSpeakManager = new TeamSpeakManager("serveradmin", "windo10", "91.218.66.173", 10011);
        this.teamManager = new TeamManager();

        getProxy().getPluginManager().registerCommand(this, new TSCommand("ts", "lostproxy.command.ts"));
        getProxy().getPluginManager().registerCommand(this, new PingCommand("ping", "lostproxy.command.ping"));
        getProxy().getPluginManager().registerCommand(this, new KickCommand("kick", "lostproxy.command.kick"));
        getProxy().getPluginManager().registerCommand(this, new NotifyCommand("notify", "lostproxy.command.notify", "benachrichtigung"));
        getProxy().getPluginManager().registerCommand(this, new KickHistoryClearCommand("kickhistoryclear", "lostproxy.command.kickhistoryclear", "khc", "khclear"));
        getProxy().getPluginManager().registerCommand(this, new KickInfoCommand("kickinfo", "lostproxy.command.kickinfo", "ki"));
        getProxy().getPluginManager().registerCommand(this, new TCCommand("tc", "lostproxy.command.tc", "teamchat"));

        getProxy().getPluginManager().registerListener(this, new PostLoginListener());
        getProxy().getPluginManager().registerListener(this, new PlayerDisconnectListener());

        CloudServices.SYNCPROXY_MANAGEMENT = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(AbstractSyncProxyManagement.class);
    }

    @Override
    public void onLoad() {
        instance = this;

        CloudServices.PERMISSION_MANAGEMENT = CloudNetDriver.getInstance().getPermissionManagement();
        CloudServices.PLAYER_MANAGER = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        database.getMongoClient().close();
        getTeamSpeakManager().getTs3Query().exit();
    }

    public TeamSpeakManager getTeamSpeakManager() {
        return teamSpeakManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public LinkageManager getLinkageManager() {
        return linkageManager;
    }

    public Gson getGson() {
        return gson;
    }

    public LostProxyDatabase getDatabase() {
        return database;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }
}
