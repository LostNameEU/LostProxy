/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:35:32
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * LostProxyDatabase.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.databases;

import eu.lostname.lostproxy.interfaces.IDatabaseConnection;

public class LostProxyDatabase extends IDatabaseConnection {

    public LostProxyDatabase(String host, String port, String username, String password, String database) {
        super(host, port, username, password, database);
    }
}
