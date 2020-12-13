package eu.lostname.lostproxy.interfaces.historyandentries.ban;

import eu.lostname.lostproxy.enums.EBanEntryType;

public class IBanAppealEntry extends IBanSpecificEntry {

    public IBanAppealEntry(String _id, String invokerId, long timestamp, String reason) {
        super(_id, invokerId, timestamp, reason, EBanEntryType.BAN_APPEAL_ENTRY);
    }
}
