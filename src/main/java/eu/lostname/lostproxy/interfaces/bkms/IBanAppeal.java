package eu.lostname.lostproxy.interfaces.bkms;

import java.util.UUID;

public class IBanAppeal {

    private final UUID _id;
    private final String invoker;
    private final long timestamp;

    public IBanAppeal(UUID _id, String invoker, long timestamp) {
        this._id = _id;
        this.invoker = invoker;
        this.timestamp = timestamp;
    }

    public UUID getUniqueId() {
        return _id;
    }

    public String getInvoker() {
        return invoker;
    }

    public long getTimestamp() {
        return timestamp;
    }
}