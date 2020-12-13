package eu.lostname.lostproxy.interfaces.historyandentries.ban;

public class IUnbanEntry extends IBanSpecificEntry {

    public IUnbanEntry(String _id, String invokerId, long timestamp, String reason) {
        super(_id, invokerId, timestamp, reason, IBanEntryType.UNBAN_ENTRY);
    }
}
