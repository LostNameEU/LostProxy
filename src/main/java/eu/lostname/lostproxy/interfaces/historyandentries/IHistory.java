package eu.lostname.lostproxy.interfaces.historyandentries;

public class IHistory {

    private final String _id;

    public IHistory(String _id) {
        this._id = _id;
    }

    public String getUniqueId() {
        return _id;
    }
}
