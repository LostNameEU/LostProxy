/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 15.01.2021 @ 00:03:35
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * LocaleManager.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.manager;

import com.google.gson.Gson;
import eu.lostname.lostproxy.database.LostProxyDatabase;

public class LocaleManager {

    private final Gson gson;
    private final LostProxyDatabase database;

    public LocaleManager(Gson gson, LostProxyDatabase database) {
        this.gson = gson;
        this.database = database;
    }


}
