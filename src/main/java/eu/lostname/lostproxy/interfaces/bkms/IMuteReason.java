package eu.lostname.lostproxy.interfaces.bkms;

import eu.lostname.lostproxy.enums.EReasonType;
import eu.lostname.lostproxy.interfaces.IReason;

import java.sql.Time;
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

    /**
     * @return a display from the ban duration
     */
    @SuppressWarnings("deprecation")
    public String displayMuteDuration() {
        Time time = new Time(timeUnit.toMillis(this.time));
        String estimatedTime = "";

        if (time.getDay() == 1) {
            estimatedTime = "ein §7Tag§8, ";
        } else if (time.getDay() > 1) {
            estimatedTime = time.getDay() + " §7Tage§8, ";
        }

        if (time.getHours() == 1) {
            estimatedTime = "§ceine §7Stunde§8, ";
        } else if (time.getHours() > 1) {
            estimatedTime = "§c" + time.getHours() + " §7Stunden§8, ";
        }

        if (time.getMinutes() == 1) {
            estimatedTime = "§ceine §7Minute und ";
        } else if (time.getMinutes() > 1) {
            estimatedTime = "§c" + time.getMinutes() + " §7Minuten und ";
        }

        if (time.getSeconds() == 1) {
            estimatedTime = "§ceine §7Sekunde";
        } else if (time.getSeconds() > 1) {
            estimatedTime = "§c" + time.getSeconds() + " §7Sekunden";
        }

        return estimatedTime;
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
