package eu.lostname.lostproxy.interfaces.historyandentries.ban;

public class IUnbanEntry extends IBanSpecificEntry {

    private final String reason;

    public IUnbanEntry(String _id, String invokerId, long timestamp, String reason) {
        super(_id, invokerId, timestamp, IBanEntryType.UNBAN_ENTRY);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
