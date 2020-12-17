package eu.lostname.lostproxy.interfaces.historyandentries.mute;

import eu.lostname.lostproxy.interfaces.historyandentries.IHistory;

import java.util.ArrayList;
import java.util.UUID;

public class IMuteHistory extends IHistory {

    private final ArrayList<IMuteSpecificEntry> history;

    public IMuteHistory(UUID uniqueId, ArrayList<IMuteSpecificEntry> history) {
        super(uniqueId);
        this.history = history;
    }

    public ArrayList<IMuteSpecificEntry> getHistory() {
        return history;
    }

    public void addEntry(IMuteSpecificEntry iMuteSpecificEntry) {
        history.add(iMuteSpecificEntry);
    }
}
