package eu.lostname.lostproxy.interfaces.historyandentries;

public class IEntry {

    private final String _id;
    private final String invokerId;
    private final long timestamp;

    public IEntry(String _id, String invokerId, long timestamp) {
        this._id = _id;
        this.invokerId = invokerId;
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

    public long getTimestamp() {
        return timestamp;
    }
}
