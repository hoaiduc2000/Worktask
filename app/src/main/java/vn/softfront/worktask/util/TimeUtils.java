package vn.softfront.worktask.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import vn.softfront.worktask.model.Task;

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

    public static long dateToMilisecond(String date) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date mDate = null;
        try {
            mDate = mSimpleDateFormat.parse(date);
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
        int hours = (int) (mEstimate / HOUR);
        int minutes = (int) (mEstimate / MINUTE - hours * 60);
        if (minutes < 0)
            minutes = minutes * (-1);
        if (hours == 0)
            return minutes + " minutes";
        else
            return hours + " hours " + minutes + " minutes";
    }

    public static void sortStartDate(ArrayList<Task> mList) {
        Collections.sort(mList, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                return lhs.getStartDate().compareTo(rhs.getStartDate());
            }
        });
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

    public static int[] position(String startTime, String dueTime) throws ParseException {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date mStart = mSimpleDateFormat.parse(startTime + ":" + "00");
        Date mDue = mSimpleDateFormat.parse(dueTime + ":" + "00");
        int start = mStart.getHours() * 60 + mStart.getMinutes();
        int due = mDue.getHours() * 60 + mDue.getMinutes();
        return new int[]{start / 10, due / 10};
    }

    public static String getDayOfWeek(int n) {
        String dayOfWeek = " ";
        switch (n) {
            case 1:
                dayOfWeek = "Sun";
                break;
            case 2:
                dayOfWeek = "Mon";
                break;
            case 3:
                dayOfWeek = "Tue";
                break;
            case 4:
                dayOfWeek = "Wed";
                break;
            case 5:
                dayOfWeek = "Thu";
                break;
            case 6:
                dayOfWeek = "Fri";
                break;
            case 7:
                dayOfWeek = "Sat";
                break;
        }

        return dayOfWeek;
    }

    public static String[] getCalendar(int n) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DAY_OF_MONTH, n);
        String mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy").format(mCalendar.getTime());
        String day = getDayOfWeek(mCalendar.get(Calendar.DAY_OF_WEEK));
        return new String[]{day + " " + mSimpleDateFormat, mSimpleDateFormat};
    }

}
