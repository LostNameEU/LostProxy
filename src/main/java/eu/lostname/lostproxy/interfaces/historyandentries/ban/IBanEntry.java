package eu.lostname.lostproxy.interfaces.historyandentries.ban;

import eu.lostname.lostproxy.interfaces.historyandentries.IEntry;

public class IBanEntry extends IEntry {

    private final long duration;
    private final long end;

    public IBanEntry(String _id, String invokerId, String reason, long timestamp, long duration, long end) {
        super(_id, invokerId, reason, timestamp);
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
