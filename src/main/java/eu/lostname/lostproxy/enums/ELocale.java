/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 15.01.2021 @ 00:24:11
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * ELocale.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.enums;

public enum ELocale {

    GERMAN("Deutsch", "de_DE"),
    AMERICAN_ENGLISH("English", "en_US");

    private final String displayName;
    private final String fileName;

    ELocale(String displayName, String fileName) {
        this.displayName = displayName;
        this.fileName = fileName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFileName() {
        return fileName;
    }
}
