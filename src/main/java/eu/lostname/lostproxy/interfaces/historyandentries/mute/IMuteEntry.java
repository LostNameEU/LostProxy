package eu.lostname.lostproxy.interfaces.historyandentries.mute;

import eu.lostname.lostproxy.enums.EMuteEntryType;

import java.util.UUID;

public class IMuteEntry extends IMuteSpecificEntry {

    private final long duration;
    private final long end;

    public IMuteEntry(UUID _id, String invokerId, long timestamp, String reason, long duration, long end) {
        super(_id, invokerId, timestamp, reason, EMuteEntryType.MUTE_ENTRY);
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
