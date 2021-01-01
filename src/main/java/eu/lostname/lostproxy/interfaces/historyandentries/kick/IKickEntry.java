package eu.lostname.lostproxy.interfaces.historyandentries.kick;

import eu.lostname.lostproxy.interfaces.historyandentries.IEntry;

public class IKickEntry extends IEntry {

    public IKickEntry(String uniqueId, String invokerId, String reason, long timestamp) {
        super(uniqueId, invokerId, reason, timestamp);
    }
}
