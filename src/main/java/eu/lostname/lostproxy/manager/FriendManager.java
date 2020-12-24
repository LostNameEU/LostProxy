package eu.lostname.lostproxy.manager;

import com.google.gson.Gson;
import eu.lostname.lostproxy.databases.LostProxyDatabase;

public class FriendManager {

    private final LostProxyDatabase database;
    private final Gson gson;

    public FriendManager(LostProxyDatabase database, Gson gson) {
        this.database = database;
        this.gson = gson;
    }


}
