package eu.lostname.lostproxy.interfaces;

import java.util.UUID;

public class ILinkage {

    private String playerName;
    private UUID _id;
    private long creationTimestamp;

    public ILinkage(String playerName, UUID _id, long creationTimestamp) {
        this.playerName = playerName;
        this._id = _id;
        this.creationTimestamp = creationTimestamp;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public UUID getUuid() {
        return _id;
    }

    public void setUuid(UUID uuid) {
        this._id = uuid;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }
}
