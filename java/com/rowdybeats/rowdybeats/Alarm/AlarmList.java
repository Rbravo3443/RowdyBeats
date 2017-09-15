package com.rowdybeats.rowdybeats.Alarm;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rowdybeats.rowdybeats.R;
import com.rowdybeats.rowdybeats.models.Alarm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by richard on 8/2/17.
 * AlarmList class, work in progress
 */
public class AlarmList extends ArrayAdapter<Alarm> implements View.OnClickListener {
    private Context context;

    //Default constructor
    public AlarmList(Context context) {
        super(context, R.layout.item);

        this.context = context;
        this.addAll(getStoredAlarms());
    }

    private List<Alarm> getStoredAlarms() {
        List<Alarm> alarms = new ArrayList<Alarm>();

        SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        Set<String> stringifiedAlarms = settings.getStringSet("alarms", null);
        Log.d("debugerino", "alarm stringifiedAlarms(out): " + stringifiedAlarms);
        if(stringifiedAlarms != null) {
            for (String alarm : stringifiedAlarms) {
                try {
                    alarms.add(Alarm.fromString(alarm));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return alarms;
    }

    private void updateStoredAlarms() {
        Set<String> stringifiedAlarms = new HashSet<String>();
        for(int i = 0; i < getCount(); i++) {
            Alarm alarm = getItem(i);
            stringifiedAlarms.add(alarm.toString());
        }

        Log.d("debugerino", "alarm stringifiedAlarms(in): " + stringifiedAlarms);
        SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet("alarms", stringifiedAlarms);
        editor.commit();
    }

    @Override
    public void add(Alarm alarm) {
        super.add(alarm);

        updateStoredAlarms();
    }

    @Override
    public void remove(Alarm alarm) {
        super.remove(alarm);

        updateStoredAlarms();
    }

    public View getView(int p, View convertView, ViewGroup parent){
        LayoutInflater inflator = LayoutInflater.from(getContext());
        View customView = inflator.inflate(R.layout.item,parent,false);

        Alarm currAlarm = getItem(p);
        String AlarmTime = currAlarm.getTime();
        String Alarmday = currAlarm.getDay();

        TextView alarmText = (TextView) customView.findViewById(R.id.AlarmNum);
        TextView timeText = (TextView) customView.findViewById(R.id.time);
        TextView dateText = (TextView) customView.findViewById(R.id.date);

        //p++;
        String newtext = Integer.toString(p);
        alarmText.setText("Alarm " + newtext);
        timeText.setText(currAlarm.getTime());
        dateText.setText(currAlarm.getDay());

        ImageButton del = (ImageButton) customView.findViewById(R.id.deleteButton);
        del.setTag(p);
        del.setOnClickListener(this);

        return customView;
    }

    @Override
    public void onClick(View view) {
        int p = (int)view.getTag();
        Log.d("debugerino", "attempt to delete element " + p);

        Alarm toRemove = getItem(p);
        this.remove(toRemove);
    }
}
