package eu.lostname.lostproxy.interfaces.historyandentries.mute;

import eu.lostname.lostproxy.interfaces.historyandentries.IHistory;

import java.util.ArrayList;
import java.util.UUID;

public class IMuteHistory extends IHistory {

    private final ArrayList<IMuteEntry> history;

    public IMuteHistory(UUID uniqueId, ArrayList<IMuteEntry> history) {
        super(uniqueId);
        this.history = history;
    }

    public ArrayList<IMuteEntry> getHistory() {
        return history;
    }

    public void addEntry(IMuteEntry iMuteEntry) {
        history.add(iMuteEntry);
    }
}
