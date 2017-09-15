package com.rowdybeats.rowdybeats.fragment;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rowdybeats.rowdybeats.R;
import com.rowdybeats.rowdybeats.radio.RadioManager;
import com.rowdybeats.rowdybeats.radio.SongListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StreamFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StreamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StreamFragment extends Fragment implements SongListener, ImageButton.OnClickListener {
    private AudioManager audioManager;
    private OnFragmentInteractionListener mListener;
    private Context context;


    public StreamFragment() { }

    public static StreamFragment newInstance(Context context) {
        StreamFragment fragment = new StreamFragment();
        fragment.context = context;
        RadioManager.register(fragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stream, container, false);

        TextView songTitle = (TextView)view.findViewById(R.id.song_title);
        songTitle.setText(RadioManager.getSongTitle());

        ImageButton button = (ImageButton)view.findViewById(R.id.play_button);
        button.setOnClickListener(this);


        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.d("debugerino", "volume: " + curVolume + ", " + maxVolume);
        SeekBar volControl = (SeekBar)view.findViewById(R.id.volume_bar);
        volControl.setMax(maxVolume);
        volControl.setProgress(curVolume);
        volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("debugerino", "stopped tracking: " + seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("debugerino", "started tracking: " + seekBar.getProgress());
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, 0);
            }
        });

        return view;
    }



    public void onSongChange() {
        Log.d("debugerino", "song changed - changed playing now title");
        View view = getView();
        if(view != null) {
            TextView songTitle = (TextView) view.findViewById(R.id.song_title);
            songTitle.setText(RadioManager.getSongTitle());
        }
    }

    @Override
    public void onClick(View v) {
        ImageButton button = (ImageButton) v;
        if(RadioManager.isPlaying()){
            RadioManager.pause();
            button.setImageResource(R.drawable.pause_button);
        }else{
            RadioManager.play();
            button.setImageResource(R.drawable.play_button);
        }

    }


    // TODO: Rename method, update argument and hook method into UI event
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
            //Make toast message
            Toast.makeText(context, "Stream", Toast.LENGTH_SHORT).show();
            onSongChange();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
