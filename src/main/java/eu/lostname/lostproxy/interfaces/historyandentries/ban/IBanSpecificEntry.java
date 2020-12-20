package eu.lostname.lostproxy.interfaces.historyandentries.ban;

import eu.lostname.lostproxy.enums.EBanEntryType;
import eu.lostname.lostproxy.interfaces.historyandentries.IEntry;

import java.util.UUID;

public class IBanSpecificEntry extends IEntry {

    private final EBanEntryType eBanEntryType;
    private final String reason;

    public IBanSpecificEntry(UUID _id, String invokerId, long timestamp, String reason, EBanEntryType eBanEntryType) {
        super(_id, invokerId, timestamp);
        this.reason = reason;
        this.eBanEntryType = eBanEntryType;
    }

    public String getReason() {
        return reason;
    }

    public EBanEntryType getIBanEntryType() {
        return eBanEntryType;
    }
}
