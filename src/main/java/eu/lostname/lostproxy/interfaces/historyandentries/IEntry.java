package eu.lostname.lostproxy.interfaces.historyandentries;

import java.util.UUID;

public class IEntry {

    private final String _id;
    private final String invokerId;
    private final long timestamp;

    public IEntry(UUID _id, String invokerId, long timestamp) {
        this._id = _id.toString();
        this.invokerId = invokerId;
        this.timestamp = timestamp;
    }

    public UUID getUniqueId() {
        return UUID.fromString(_id);
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
