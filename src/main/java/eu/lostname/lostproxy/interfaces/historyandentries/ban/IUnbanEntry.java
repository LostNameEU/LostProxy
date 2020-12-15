package eu.lostname.lostproxy.interfaces.historyandentries.ban;

import eu.lostname.lostproxy.enums.EBanEntryType;

import java.util.UUID;

public class IUnbanEntry extends IBanSpecificEntry {

    public IUnbanEntry(UUID _id, String invokerId, long timestamp, String reason) {
        super(_id, invokerId, timestamp, reason, EBanEntryType.UNBAN_ENTRY);
    }
}
