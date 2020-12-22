package eu.lostname.lostproxy.interfaces.bkms;

import eu.lostname.lostproxy.enums.EReasonType;
import eu.lostname.lostproxy.enums.ETimeUnit;
import eu.lostname.lostproxy.interfaces.IReason;

public class IBanReason extends IReason {

    private long time;
    private ETimeUnit eTimeUnit;
    private String permission;

    public IBanReason(int _id, String name, long time, ETimeUnit eTimeUnit, String permission) {
        super(_id, name, EReasonType.BAN);
        this.time = time;
        this.eTimeUnit = eTimeUnit;
        this.permission = permission;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ETimeUnit getETimeUnit() {
        return eTimeUnit;
    }

    public void setETimeUnit(ETimeUnit eTimeUnit) {
        this.eTimeUnit = eTimeUnit;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
