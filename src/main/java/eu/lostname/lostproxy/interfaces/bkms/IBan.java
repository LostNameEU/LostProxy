package eu.lostname.lostproxy.interfaces.bkms;

public class IBan {

    private final String _id;
    private String playerName;
    private final long timestamp;
    private final long duration;
    private String invoker;
    private String reason;
    private long end;
    private IBanAppeal banAppeal;

    public IBan(String _id, String playerName, String invoker, String reason, long timestamp, long duration, long end, IBanAppeal banAppeal) {
        this._id = _id;
        this.playerName = playerName;
        this.invoker = invoker;
        this.reason = reason;
        this.timestamp = timestamp;
        this.duration = duration;
        this.end = end;
        this.banAppeal = banAppeal;
    }

    public String getUniqueId() {
        return _id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getInvoker() {
        return invoker;
    }

    public void setInvoker(String invoker) {
        this.invoker = invoker;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getDuration() {
        return duration;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public boolean hasBanAppeal() {
        return banAppeal != null;
    }

    public IBanAppeal getBanAppeal() {
        return banAppeal;
    }

    public void setBanAppeal(IBanAppeal banAppeal) {
        this.banAppeal = banAppeal;
    }
}