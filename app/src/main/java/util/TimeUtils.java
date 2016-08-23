package util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import model.Task;

/**
 * Created by nguyen.hoai.duc on 7/28/2016.
 */
public class TimeUtils {
    public static final int SECOND = 1000;
    public static final int MINUTE = 60 * SECOND;
    public static final int HOUR = 60 * MINUTE;
    public static final int DAY = 24 * HOUR;

    public static long timeToMilisecond(String time, String date) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date mDate = null;
        try {
            mDate = mSimpleDateFormat.parse(date + " " + time + ":00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDate.getTime();
    }

    public static String[] milisecondToTime(long milisecond) {
        String time = new SimpleDateFormat("HH:mm").format(milisecond);
        String date = new SimpleDateFormat("dd/MM/yyyy").format(milisecond);
        return new String[]{time, date};
    }

    public static Date timeToDate(String time, String date) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date mDate = null;
        try {
            mDate = mSimpleDateFormat.parse(date + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDate;
    }

    public static Calendar timeToCalendar(String time, String date) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar mCalendar = Calendar.getInstance();
        try {
            mCalendar.setTime(mSimpleDateFormat.parse(date + " " + time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mCalendar;
    }

    public static int[] getFreeTime(long startDate, long dueDate) {
        int hours;
        int minutes;
        long mFreeTime = dueDate - startDate;
        hours = (int) mFreeTime / HOUR;
        minutes = (int) (mFreeTime % HOUR) / MINUTE;
        int[] values = new int[]{hours, minutes};
        return values;
    }

    public static String getEstimateTime(long start, long due) {
        long mEstimate = due - start;
        long hourss = mEstimate / HOUR;
        int hours = (int) (mEstimate / HOUR);
        float minutes = (((int) mEstimate / MINUTE - hours * 60) * 100 / 60) / 10;
        if (minutes < 0)
            minutes = minutes * (-1);
        return hours + "." + Math.round(minutes);
    }

    public static void sortStartDate(ArrayList<Task> mList) {
        Collections.sort(mList, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                return lhs.getStartDate().compareTo(rhs.getStartDate());
            }
        });
    }

    public static void sortDueDate(ArrayList<Task> mList) {
        Collections.sort(mList, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                return lhs.getDueDate().compareTo(rhs.getDueDate());
            }
        });
    }

    public static String getDueTime(long start, double estimate) {
        long due = start + Math.round(estimate * HOUR);
        if (estimate == Constrans.INIT_ESTIMATE)
            due = start + Math.round(HOUR / 2 + HOUR % 2);
        String time = new SimpleDateFormat("HH:mm").format(due);
        return time;
    }

    public static String getDueDate(long start, double estimate) {
        long due = start + Math.round(estimate * HOUR);
        String date = new SimpleDateFormat("dd/MM/yyyy").format(due);
        return date;
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
    }

    public static boolean checkFreeTime(long mTask1, long mTask2) {
        int mFreetime = (int) (mTask2 - mTask1);
        if (mFreetime >= (30 * MINUTE))
            return true;
        return false;
    }

    public static String[] startTime() {
        String[] startTime;
        String time = new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());
        String date = new SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis());
        startTime = new String[]{time, date};

        return startTime;
    }

    public static String[] endTime(long starttime) {
        String[] dueTime;
        String time = new SimpleDateFormat("HH:mm").format(starttime + 30 * MINUTE);
        String date = new SimpleDateFormat("dd/MM/yyyy").format(starttime + 30 * MINUTE);
        dueTime = new String[]{time, date};
        return dueTime;
    }
}
