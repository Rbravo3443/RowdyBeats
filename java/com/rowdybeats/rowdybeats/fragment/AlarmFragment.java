package com.rowdybeats.rowdybeats.fragment;

import android.app.Activity;
import android.content.Context;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.codetroopers.betterpickers.timepicker.TimePickerDialogFragment;
import com.rowdybeats.rowdybeats.R;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.rowdybeats.rowdybeats.Alarm.AlarmList;
import com.rowdybeats.rowdybeats.models.Alarm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmFragment extends Fragment implements View.OnClickListener, RadialTimePickerDialogFragment.OnTimeSetListener, CalendarDatePickerDialogFragment.OnDateSetListener {
    private OnFragmentInteractionListener mListener;
    private FloatingActionButton action;
    //private ImageView delete;
    private View view;
    private AlarmList adapter;
    private Calendar calendar;
    private Context context;

    public AlarmFragment() { }

    public static AlarmFragment newInstance(Context context) {
        AlarmFragment fragment = new AlarmFragment();
        fragment.context = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        //listy = (ListView) view.findViewById(R.id.view);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        //ImageView view2 =
        //ImageView view2 =
        action = (FloatingActionButton) view.findViewById(R.id.actionButton);
        action.setOnClickListener( this );

        adapter = new AlarmList(this.getContext());
        ListView listy = (ListView) view.findViewById(R.id.view);
        listy.setAdapter(adapter);
        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Toast.makeText(context, "Alarm", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        calendar = Calendar.getInstance();

        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(AlarmFragment.this)
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setDoneText("Ok")
                .setCancelText("Cancel")
                .setThemeDark();
        cdp.show(getFragmentManager(), "0");
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(AlarmFragment.this)
                .setStartTime(calendar.get(Calendar.HOUR_OF_DAY) + (calendar.get(Calendar.MINUTE) + 10) / 60, (calendar.get(Calendar.MINUTE) + 10) % 60)
                .setDoneText("Ok")
                .setCancelText("Cancel")
                .setThemeDark();
        rtpd.show(getFragmentManager(), "0");

        calendar.set(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
        adapter.add(new Alarm(calendar.getTimeInMillis()));

        //Log.v("debugerino", "WE DID IT REDDIT!");
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

   // public void populateList(){

    //}

}
