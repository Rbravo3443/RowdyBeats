package com.rowdybeats.rowdybeats.models;

import android.app.AlarmManager;
import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by richard on 8/7/17.
 */

public class Alarm {

    AlarmManager alarm;
    String time;
    String day;
    long timestamp;

    public Alarm() {
        this.day = "Monday";
        this.time = "12:00";
    }

    public Alarm(String day, String time) {
        this.day = day;
        this.time = time;
    }

    public Alarm(long timestamp) {
        this.timestamp = timestamp;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
        String date = sdf1.format(calendar.getTime());

        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm a");

        this.time = sdf2.format(calendar.getTime());
        this.day = date + " - " + calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
    }

    //extrapolate date/time components from this rather than storing as a string
    public Date getDate() { return new Date(timestamp); }

    public String getTime(){
        return this.time;
    }

    public String getDay(){
        return this.day;
    }

    public String toString() {
        try {
            return new JSONObject().put("time", "" + timestamp).toString();
        } catch(Exception e) {
            return "" + time;
        }
    }

    public static Alarm fromString(String string) throws Exception {
        Log.d("debugerino", "alarm fromString: " + string);
        JSONObject obj = new JSONObject(string);
        Long timestamp = Long.parseLong(obj.getString("time"));
        return new Alarm(timestamp);
    }
}
