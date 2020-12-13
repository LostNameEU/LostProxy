package eu.lostname.lostproxy.interfaces.historyandentries.ban;

import eu.lostname.lostproxy.interfaces.historyandentries.IEntry;

public class IBanSpecificEntry extends IEntry {

    private final IBanEntryType iBanEntryType;

    public IBanSpecificEntry(String _id, String invokerId, long timestamp, IBanEntryType iBanEntryType) {
        super(_id, invokerId, timestamp);
        this.iBanEntryType = iBanEntryType;
    }

    public IBanEntryType getIBanEntryType() {
        return iBanEntryType;
    }
}
