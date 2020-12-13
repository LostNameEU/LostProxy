package eu.lostname.lostproxy.interfaces.historyandentries.ban;

import eu.lostname.lostproxy.interfaces.historyandentries.IEntry;

public class IBanSpecificEntry extends IEntry {

    private final IBanEntryType iBanEntryType;
    private final String reason;

    public IBanSpecificEntry(String _id, String invokerId, long timestamp, String reason, IBanEntryType iBanEntryType) {
        super(_id, invokerId, timestamp);
        this.reason = reason;
        this.iBanEntryType = iBanEntryType;
    }

    public String getReason() {
        return reason;
    }

    public IBanEntryType getIBanEntryType() {
        return iBanEntryType;
    }
}
