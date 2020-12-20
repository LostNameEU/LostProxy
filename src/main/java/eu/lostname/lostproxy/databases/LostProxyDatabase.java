package eu.lostname.lostproxy.databases;

import eu.lostname.lostproxy.interfaces.IDatabaseConnection;

public class LostProxyDatabase extends IDatabaseConnection {

    public LostProxyDatabase(String host, String port, String username, String password, String database) {
        super(host, port, username, password, database);
    }
}
