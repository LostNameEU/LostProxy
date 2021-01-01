package eu.lostname.lostproxy.interfaces.historyandentries;

import java.util.UUID;

public class IHistory {

    private final String _id;

    public IHistory(UUID _id) {
        this._id = _id.toString();
    }

    public UUID getUniqueId() {
        return UUID.fromString(_id);
    }
}
