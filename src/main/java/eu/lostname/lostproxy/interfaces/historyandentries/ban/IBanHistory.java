package eu.lostname.lostproxy.interfaces.historyandentries.ban;

import eu.lostname.lostproxy.interfaces.historyandentries.IHistory;

import java.util.ArrayList;

public class IBanHistory extends IHistory {

    private final ArrayList<IBanSpecificEntry> history;

    public IBanHistory(String uniqueId, ArrayList<IBanSpecificEntry> history) {
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
