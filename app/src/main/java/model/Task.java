package model;

import java.sql.Time;
import java.util.Date;
import util.TimeUtils;

/**
 * Created by nguyen.hoai.duc on 7/26/2016.
 */
public class Task {
    private int id;
    private String title;
    private String priority;
    private String estimate;
    private String starttime;
    private String startdate;
    private String duetime;
    private String duedate;
    private String description;
    private String status;

    public Task() {

    }

    public Date getStartDate() {
        return TimeUtils.timeToDate(getStarttime(), getStartdate());

    }

    public Date getDueDate() {
        return TimeUtils.timeToDate(getStarttime(), getStartdate());

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getEstimate() {
        return estimate;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getDuedate() {
        return duedate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getDuetime() {
        return duetime;
    }

    public void setDuetime(String duetime) {
        this.duetime = duetime;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStart(){
        long n = TimeUtils.timeToMilisecond(this.starttime,this.startdate);
        int minute = (int)(n/TimeUtils.MINUTE);
        return minute;
    }

    public int getDue(){
        long n = TimeUtils.timeToMilisecond(this.duetime,this.duedate);
        int minute = (int)(n/TimeUtils.MINUTE);
        return minute;
    }
}
