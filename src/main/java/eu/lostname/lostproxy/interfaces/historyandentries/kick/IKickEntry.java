package eu.lostname.lostproxy.interfaces.historyandentries.kick;

import eu.lostname.lostproxy.interfaces.historyandentries.IEntry;

public class IKickEntry extends IEntry {

    private final String reason;

    public IKickEntry(String _id, String invokerId, String reason, long timestamp) {
        super(_id, invokerId, timestamp);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
