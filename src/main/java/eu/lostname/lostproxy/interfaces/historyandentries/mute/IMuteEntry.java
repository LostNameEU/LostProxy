package eu.lostname.lostproxy.interfaces.historyandentries.mute;

import eu.lostname.lostproxy.enums.EMuteEntryType;
import eu.lostname.lostproxy.interfaces.historyandentries.IEntry;

import java.util.UUID;

public class IMuteEntry extends IEntry {

    private final EMuteEntryType eMuteEntryType;
    private final String reason;
    private final long duration;
    private final long end;

    public IMuteEntry(EMuteEntryType eMuteEntryType, UUID _id, String invokerId, long timestamp, String reason, long duration, long end) {
        super(_id, invokerId, timestamp);
        this.eMuteEntryType = eMuteEntryType;
        this.duration = duration;
        this.end = end;
        this.reason = reason;
    }

    public EMuteEntryType getEMuteEntryType() {
        return eMuteEntryType;
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
