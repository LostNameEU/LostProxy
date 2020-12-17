package eu.lostname.lostproxy.interfaces.historyandentries.mute;

import eu.lostname.lostproxy.enums.EMuteEntryType;
import eu.lostname.lostproxy.interfaces.historyandentries.IEntry;

import java.util.UUID;

public class IMuteSpecificEntry extends IEntry {

    private final EMuteEntryType eMuteEntryType;
    private final String reason;

    public IMuteSpecificEntry(UUID _id, String invokerId, long timestamp, String reason, EMuteEntryType eMuteEntryType) {
        super(_id, invokerId, timestamp);
        this.reason = reason;
        this.eMuteEntryType = eMuteEntryType;
    }

    public String getReason() {
        return reason;
    }

    public EMuteEntryType getIMuteEntryType() {
        return eMuteEntryType;
    }
}
