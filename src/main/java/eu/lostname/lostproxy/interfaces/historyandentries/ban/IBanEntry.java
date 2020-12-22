package eu.lostname.lostproxy.interfaces.historyandentries.ban;

import eu.lostname.lostproxy.enums.EBanEntryType;
import eu.lostname.lostproxy.enums.ETimeUnit;
import eu.lostname.lostproxy.interfaces.historyandentries.IEntry;

import java.util.UUID;

public class IBanEntry extends IEntry {

    private final EBanEntryType eBanEntryType;
    private final String reason;
    private final long time;
    private final ETimeUnit eTimeUnit;
    private final long end;

    public IBanEntry(EBanEntryType eBanEntryType, UUID _id, String invokerId, long timestamp, String reason, long time, ETimeUnit eTimeUnit, long end) {
        super(_id, invokerId, timestamp);
        this.eBanEntryType = eBanEntryType;
        this.time = time;
        this.eTimeUnit = eTimeUnit;
        this.end = end;
        this.reason = reason;
    }

    public EBanEntryType getEBanEntryType() {
        return eBanEntryType;
    }

    public String getReason() {
        return reason;
    }

    public long getTime() {
        return time;
    }

    public ETimeUnit getETimeUnit() {
        return eTimeUnit;
    }

    public long getEnd() {
        return end;
    }
}
