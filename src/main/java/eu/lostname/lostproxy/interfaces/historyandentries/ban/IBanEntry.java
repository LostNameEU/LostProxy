package eu.lostname.lostproxy.interfaces.historyandentries.ban;

public class IBanEntry extends IBanSpecificEntry {

    private final String reason;
    private final long duration;
    private final long end;

    public IBanEntry(String _id, String invokerId, long timestamp, IBanEntryType iBanEntryType, String reason, long duration, long end) {
        super(_id, invokerId, timestamp, iBanEntryType);
        this.reason = reason;
        this.duration = duration;
        this.end = end;
    }

    public String getReason() {
        return reason;
    }

    public long getDuration() {
        return duration;
    }

    public long getEnd() {
        return end;
    }
}
