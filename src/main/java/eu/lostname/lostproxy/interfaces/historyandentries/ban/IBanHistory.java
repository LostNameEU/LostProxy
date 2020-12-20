package eu.lostname.lostproxy.interfaces.historyandentries.ban;

import eu.lostname.lostproxy.interfaces.historyandentries.IHistory;

import java.util.ArrayList;
import java.util.UUID;

public class IBanHistory extends IHistory {

    private final ArrayList<IBanSpecificEntry> history;

    public IBanHistory(UUID uniqueId, ArrayList<IBanSpecificEntry> history) {
        super(uniqueId);
        this.history = history;
    }

    public ArrayList<IBanSpecificEntry> getHistory() {
        return history;
    }

    public void addEntry(IBanSpecificEntry iBanSpecificEntry) {
        history.add(iBanSpecificEntry);
    }
}
