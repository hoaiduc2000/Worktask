package model;

import android.util.Log;

import java.text.SimpleDateFormat;

import util.TimeUtils;

/**
 * Created by nguyen.hoai.duc on 8/10/2016.
 */
public class TimeFree {
    private long startTime;
    private long dueTime;
    private long dueTime2;

    public TimeFree() {

    }

    public long getStartTime() {
        return TimeUtils.timeToMilisecond(TimeUtils.milisecondToTime(this.startTime)[0]
                , TimeUtils.milisecondToTime(this.startTime)[1]);
    }

    public void setStartTime(long startTime) {
        this.startTime = TimeUtils.timeToMilisecond(TimeUtils.milisecondToTime(startTime)[0]
                , TimeUtils.milisecondToTime(startTime)[1]);

    }

    public long getDueTime() {
        return dueTime;
    }

    public void setDueTime(long dueTime) {
        this.dueTime = dueTime;

    }

    public long getDueTime2() {
        return dueTime2;
    }

    public void setDueTime2(long dueTime2) {
        this.dueTime2 = dueTime2;

    }

    public long getDefaultDueTime() {
        return (this.startTime + 30 * TimeUtils.MINUTE);
    }

    public TimeFree(long startTime, long dueTime, long dueTime2) {
        this.startTime = startTime;
        this.dueTime = dueTime;
        this.dueTime2 = dueTime2;
    }
}
