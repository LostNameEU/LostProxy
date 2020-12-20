package eu.lostname.lostproxy.interfaces.historyandentries.ban;

import eu.lostname.lostproxy.enums.EBanEntryType;

import java.util.UUID;

public class IBanAppealEntry extends IBanSpecificEntry {

    public IBanAppealEntry(UUID _id, String invokerId, long timestamp, String reason) {
        super(_id, invokerId, timestamp, reason, EBanEntryType.BAN_APPEAL_ENTRY);
    }
}
