package eu.lostname.lostproxy.interfaces.bkms;

import eu.lostname.lostproxy.enums.EReasonType;
import eu.lostname.lostproxy.interfaces.IReason;

import java.util.concurrent.TimeUnit;

public class IMuteReason extends IReason {

    private long time;
    private TimeUnit timeUnit;
    private String permission;

    public IMuteReason(int _id, String name, long time, TimeUnit timeUnit, String permission) {
        super(_id, name, EReasonType.MUTE);
        this.time = time;
        this.timeUnit = timeUnit;
        this.permission = permission;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
