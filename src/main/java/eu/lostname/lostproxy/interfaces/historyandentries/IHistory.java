package eu.lostname.lostproxy.interfaces.historyandentries;

import java.util.UUID;

public class IHistory {

    private final UUID _id;

    public IHistory(UUID _id) {
        this._id = _id;
    }

    public UUID getUniqueId() {
        return _id;
    }
}
