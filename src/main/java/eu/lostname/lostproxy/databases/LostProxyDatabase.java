package eu.lostname.lostproxy.databases;

import eu.lostname.lostproxy.interfaces.IDatabaseConnection;

public class LostProxyDatabase extends IDatabaseConnection {

    public LostProxyDatabase(String username, String password, String database) {
        super(username, password, database);
    }
}
