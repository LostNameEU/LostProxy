package eu.lostname.lostproxy;

import com.google.gson.Gson;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.syncproxy.AbstractSyncProxyManagement;
import eu.lostname.lostproxy.commands.*;
import eu.lostname.lostproxy.databases.LostProxyDatabase;
import eu.lostname.lostproxy.listener.PlayerDisconnectListener;
import eu.lostname.lostproxy.listener.PostLoginListener;
import eu.lostname.lostproxy.listener.PreLoginListener;
import eu.lostname.lostproxy.manager.*;
import eu.lostname.lostproxy.utils.CloudServices;
import eu.lostname.lostproxy.utils.Property;
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
    private BanManager banManager;
    private ReasonManager reasonManager;

    private Property property;

    public static LostProxy getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        this.gson = new Gson();
        this.database = new LostProxyDatabase(property.get("cfg", "db.username"), property.get("cfg", "db.password"), property.get("cfg", "db.database"));
        this.linkageManager = new LinkageManager(gson);
        this.playerManager = new PlayerManager();
        this.historyManager = new HistoryManager();
        this.teamSpeakManager = new TeamSpeakManager();
        this.teamManager = new TeamManager();
        this.banManager = new BanManager();
        this.reasonManager = new ReasonManager(gson, database);

        getProxy().getPluginManager().registerCommand(this, new TSCommand("ts", "lostproxy.command.ts"));
        getProxy().getPluginManager().registerCommand(this, new PingCommand("ping", "lostproxy.command.ping"));
        getProxy().getPluginManager().registerCommand(this, new KickCommand("kick", "lostproxy.command.kick"));
        getProxy().getPluginManager().registerCommand(this, new NotifyCommand("notify", "lostproxy.command.notify", "benachrichtigung"));
        getProxy().getPluginManager().registerCommand(this, new KickHistoryClearCommand("kickhistoryclear", "lostproxy.command.kickhistoryclear", "khc", "khclear"));
        getProxy().getPluginManager().registerCommand(this, new KickInfoCommand("kickinfo", "lostproxy.command.kickinfo", "ki"));
        getProxy().getPluginManager().registerCommand(this, new TCCommand("tc", "lostproxy.command.tc", "teamchat"));
        getProxy().getPluginManager().registerCommand(this, new TeamCommand("team", "lostproxy.command.team"));
        getProxy().getPluginManager().registerCommand(this, new BanReasonsCommand("banreasons", "lostproxy.command.banreasons", "br"));
        getProxy().getPluginManager().registerCommand(this, new UnbanCommand("unban", "lostproxy.command.unban", "ub"));
        getProxy().getPluginManager().registerCommand(this, new BanInfoCommand("baninfo", "lostproxy.command.baninfo", "bi"));
        getProxy().getPluginManager().registerCommand(this, new BanHistoryClearCommand("banhistoryclear", "lostproxy.command.banhistoryclear", "bhc", "bhclear"));
        getProxy().getPluginManager().registerCommand(this, new BanCommand("ban", "lostproxy.command.ban", "b"));

        getProxy().getPluginManager().registerListener(this, new PostLoginListener());
        getProxy().getPluginManager().registerListener(this, new PlayerDisconnectListener());
        getProxy().getPluginManager().registerListener(this, new PreLoginListener());

        CloudServices.SYNCPROXY_MANAGEMENT = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(AbstractSyncProxyManagement.class);
    }

    @Override
    public void onLoad() {
        instance = this;

        property = new Property();
        property.setDefaultProps();

        CloudServices.PERMISSION_MANAGEMENT = CloudNetDriver.getInstance().getPermissionManagement();
        CloudServices.PLAYER_MANAGER = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        database.getMongoClient().close();
        getTeamSpeakManager().getTs3Query().exit();
    }

    public String formatArrayToString(int startIndex, String[] strings) {
        StringBuilder msg = new StringBuilder();
        for (int i = startIndex; i < strings.length; i++) {
            if (i == (strings.length - 1)) {
                msg.append(strings[i]);
            } else
                msg.append(strings[i]).append(" ");
        }
        return msg.toString();
    }

    public BanManager getBanManager() {
        return banManager;
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

    public Property getProperty() {
        return property;
    }

    public ReasonManager getReasonManager() {
        return reasonManager;
    }
}
