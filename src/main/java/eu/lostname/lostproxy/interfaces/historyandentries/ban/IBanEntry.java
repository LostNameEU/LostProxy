package eu.lostname.lostproxy.interfaces.historyandentries.ban;

import eu.lostname.lostproxy.enums.EBanEntryType;

public class IBanEntry extends IBanSpecificEntry {

    private final long duration;
    private final long end;

    public IBanEntry(String _id, String invokerId, long timestamp, String reason, long duration, long end) {
        super(_id, invokerId, timestamp, reason, EBanEntryType.BAN_ENTRY);
        this.duration = duration;
        this.end = end;
    }

    public long getDuration() {
        return duration;
    }

    public long getEnd() {
        return end;
    }
}
