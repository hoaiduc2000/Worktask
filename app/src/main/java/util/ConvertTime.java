package util;

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
public class ConvertTime {
    public static final int SECOND = 1000;
    public static final int MINUTE = 60 * SECOND;
    public static final int HOUR = 60 * MINUTE;
    public static final int DAY = 24 * HOUR;

    public static long timeToMilisecond(String time, String date) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date mDate = null;
        try {
            mDate = mSimpleDateFormat.parse(date + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDate.getTime();
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

    public static int[] getFreeTime(long mTime) {
        int hours;
        int minutes;
        long mFreeTime = mTime - System.currentTimeMillis();
        hours = (int) mFreeTime / HOUR;
        minutes = (int) (mFreeTime % HOUR) / MINUTE;
        int[] values = new int[]{hours, minutes};
        return values;
    }

    public static String getEstimateTime(long start, long due) {
        long mEstimate = due - start;
        int hours = (int) mEstimate / HOUR;
        int minutes = (int) mEstimate / MINUTE;
        if (hours <= Constrans.maxEstimateHour&& hours >= 1)
            return hours + "";
        if (hours < 1 && minutes >= 30)
            return 0.5 + "";
        if (hours > Constrans.maxEstimateHour)
            return Constrans.maxEstimateHour + "";
        else return 0 + "";
    }

    public static void sortDate(ArrayList<Task> mList){
        Collections.sort(mList, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                return lhs.getDate().compareTo(rhs.getDate());
            }
        });
    }

    public static String getCurrentTime(){
        return new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
    }

    public static String getCurrentDate(){
        return new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
    }
}
