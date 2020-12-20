package eu.lostname.lostproxy.interfaces.historyandentries.ban;

import eu.lostname.lostproxy.interfaces.historyandentries.IHistory;

import java.util.ArrayList;
import java.util.UUID;

public class IBanHistory extends IHistory {

    private final ArrayList<IBanEntry> history;

    public IBanHistory(UUID uniqueId, ArrayList<IBanEntry> history) {
        super(uniqueId);
        this.history = history;
    }

    public ArrayList<IBanEntry> getHistory() {
        return history;
    }

    public void addEntry(IBanEntry iBanEntry) {
        history.add(iBanEntry);
    }
}
