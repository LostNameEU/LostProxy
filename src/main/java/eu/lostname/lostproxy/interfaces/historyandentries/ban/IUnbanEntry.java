package eu.lostname.lostproxy.interfaces.historyandentries.ban;

import eu.lostname.lostproxy.enums.EBanEntryType;

public class IUnbanEntry extends IBanSpecificEntry {

    public IUnbanEntry(String _id, String invokerId, long timestamp, String reason) {
        super(_id, invokerId, timestamp, reason, EBanEntryType.UNBAN_ENTRY);
    }
}
