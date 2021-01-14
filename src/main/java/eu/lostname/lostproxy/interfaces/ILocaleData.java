/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 15.01.2021 @ 00:03:35
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * ILocaleData.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces;

import java.util.Locale;
import java.util.UUID;

public class ILocaleData {

    private final String _id;
    private final Locale locale;

    public ILocaleData(UUID uuid, Locale locale) {
        this._id = uuid.toString();
        this.locale = locale;
    }

    public UUID getUniqueId() {
        return UUID.fromString(_id);
    }

    public Locale getLocale() {
        return locale;
    }
}
