package eu.lostname.lostproxy.interfaces.historyandentries.kick;

import eu.lostname.lostproxy.interfaces.historyandentries.IHistory;

import java.util.ArrayList;

public class IKickHistory extends IHistory {

    private final ArrayList<IKickEntry> history;

    public IKickHistory(String uniqueId, ArrayList<IKickEntry> history) {
        super(uniqueId);
        this.history = history;
    }

    public ArrayList<IKickEntry> getHistory() {
        return history;
    }

    public void addEntry(IKickEntry iKickEntry) {
        history.add(iKickEntry);
    }
}
