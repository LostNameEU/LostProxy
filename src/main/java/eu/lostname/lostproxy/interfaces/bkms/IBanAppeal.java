package eu.lostname.lostproxy.interfaces.bkms;

public class IBanAppeal {

    private final String _id;
    private final String invoker;
    private final long timestamp;

    public IBanAppeal(String _id, String invoker, long timestamp) {
        this._id = _id;
        this.invoker = invoker;
        this.timestamp = timestamp;
    }

    public String get_id() {
        return _id;
    }

    public String getInvoker() {
        return invoker;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
