/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 15.01.2021 @ 00:24:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * LocaleManager.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.manager;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import eu.lostname.lostproxy.database.LostProxyDatabase;
import eu.lostname.lostproxy.enums.ELocale;
import eu.lostname.lostproxy.interfaces.ILocaleData;
import eu.lostname.lostproxy.utils.MongoCollection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;

import java.util.HashMap;
import java.util.UUID;

public class LocaleManager {

    private final Gson gson;
    private final LostProxyDatabase database;
    private final HashMap<UUID, ILocaleData> cache;

    public LocaleManager(Gson gson, LostProxyDatabase database) {
        this.gson = gson;
        this.database = database;
        this.cache = new HashMap<>();
    }


    public ILocaleData getLocaleData(ProxiedPlayer proxiedPlayer) {
        if (!cache.containsKey(proxiedPlayer.getUniqueId())) {
            Document d = database.getMongoDatabase().getCollection(MongoCollection.LOCALIZATION_DATA).find(Filters.eq("_id", proxiedPlayer.getUniqueId().toString())).first();

            if (d == null) {
                d = gson.fromJson(gson.toJson(new ILocaleData(proxiedPlayer.getUniqueId(), ELocale.AMERICAN_ENGLISH)), Document.class);
                database.getMongoDatabase().getCollection(MongoCollection.LOCALIZATION_DATA).insertOne(d);
            }
            cache.put(proxiedPlayer.getUniqueId(), gson.fromJson(d.toJson(), ILocaleData.class));
        }
        return cache.get(proxiedPlayer.getUniqueId());
    }

    public HashMap<UUID, ILocaleData> getCache() {
        return cache;
    }
}
