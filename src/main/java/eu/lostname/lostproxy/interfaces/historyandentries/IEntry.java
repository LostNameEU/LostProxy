package eu.lostname.lostproxy.interfaces.historyandentries;

public class IEntry {

    private final String _id;
    private final String invokerId;
    private final String reason;
    private final long timestamp;

    public IEntry(String _id, String invokerId, String reason, long timestamp) {
        this._id = _id;
        this.invokerId = invokerId;
        this.reason = reason;
        this.timestamp = timestamp;
    }

    public String getUniqueId() {
        return _id;
    }

    public String getInvokerId() {
        return invokerId;
    }

    public boolean isInvokerConsole() {
        return invokerId.equalsIgnoreCase("console");
    }

    public String getReason() {
        return reason;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
