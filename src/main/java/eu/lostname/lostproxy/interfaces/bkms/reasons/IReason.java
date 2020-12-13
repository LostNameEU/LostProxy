package eu.lostname.lostproxy.interfaces.bkms.reasons;

public class IReason {

    private int _id;
    private String name;
    private long duration;
    private String permission;

    public IReason(int _id, String name, long duration, String permission) {
        this._id = _id;
        this.name = name;
        this.duration = duration;
        this.permission = permission;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
