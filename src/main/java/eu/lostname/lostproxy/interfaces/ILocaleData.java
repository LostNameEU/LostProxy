/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 15.01.2021 @ 00:24:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * ILocaleData.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces;

import eu.lostname.lostproxy.enums.ELocale;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

public class ILocaleData {

    private final String _id;
    private final ELocale locale;
    private final long timestamp;

    public ILocaleData(UUID uuid, ELocale locale) {
        this._id = uuid.toString();
        this.locale = locale;
        this.timestamp = System.currentTimeMillis();
    }

    public String getMessage(String key) {
        String[] s = locale.getFileName().split("_");
        ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale(s[0], s[1]));
        return bundle.getString(key);
    }

    public UUID getUniqueId() {
        return UUID.fromString(_id);
    }

    public ELocale getLocale() {
        return locale;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
