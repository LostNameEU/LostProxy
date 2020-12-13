package eu.lostname.lostproxy.interfaces.bkms.reasons;

import java.util.concurrent.TimeUnit;

public class IReason {

    private int _id;
    private String name;
    private long time;
    private TimeUnit timeUnit;
    private String permission;

    public IReason(int _id, String name, long time, TimeUnit timeUnit, String permission) {
        this._id = _id;
        this.name = name;
        this.time = time;
        this.timeUnit = timeUnit;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
}
