package eu.lostname.lostproxy.interfaces.historyandentries.mute;

import eu.lostname.lostproxy.enums.EMuteEntryType;

import java.util.UUID;

public class IUnmuteEntry extends IMuteSpecificEntry {

    public IUnmuteEntry(UUID _id, String invokerId, long timestamp, String reason) {
        super(_id, invokerId, timestamp, reason, EMuteEntryType.UNMUTE_ENTRY);
    }
}
