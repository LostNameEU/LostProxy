package eu.lostname.lostproxy.interfaces.historyandentries.ban;

public class IBanAppealEntry extends IBanSpecificEntry {

    public IBanAppealEntry(String _id, String invokerId, long timestamp, String reason, IBanEntryType iBanEntryType) {
        super(_id, invokerId, timestamp, reason, iBanEntryType);
    }
}
