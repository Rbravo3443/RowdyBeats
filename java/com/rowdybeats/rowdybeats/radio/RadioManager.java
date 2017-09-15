package com.rowdybeats.rowdybeats.radio;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.volley.VolleyError;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.rowdybeats.rowdybeats.R;
import com.rowdybeats.rowdybeats.network.APITask;
import com.rowdybeats.rowdybeats.network.APITaskType;
import com.rowdybeats.rowdybeats.network.Endpoints;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.view.View;
import wseemann.media.FFmpegMediaPlayer;

/**
 * Created by bryceboesen on 7/20/17.
 */

public class RadioManager implements FFmpegMediaPlayer.OnPreparedListener, FFmpegMediaPlayer.OnErrorListener, FFmpegMediaPlayer.OnCompletionListener {
    private static String streamUrl = null;
    private static String metadataUrl = null;
    private ImageButton action2;
    private static RadioManager instance;
    private static Set<SongListener> observers = new HashSet<SongListener>();
    private Context context;

    private Socket mSocket;
    private FFmpegMediaPlayer mp;

    private JSONObject currentMetadata = null;

    private RadioManager(Context context) {
        this.context = context;
    }

    public static RadioManager getInstance(Context context) {
        if(instance == null) {
            instance = new RadioManager(context);
        } else
            instance.context = context;

        return instance;
    }

    public void fetchStream() throws Exception {
        APITask task = new APITask(this.context, Endpoints.getStreamURL(), APITaskType.GET);
        task.execute(null, new APITask.Listener() {
            public void onResponse(VolleyError error, JSONObject response) {
                setupMetaFetcher(response);
                setupMediaPlayer(response);
            }
        });
    }

    private void setupMediaPlayer(JSONObject response) {
        if(response == null) {
            Log.d("debugerino", "server returned no stream url response");
            return;
        }

        try {
            streamUrl = response.getString("streamUrl");
            Log.d("debugerino", "streamUrl: " + streamUrl);

            mp = new FFmpegMediaPlayer();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
            mp.setDataSource(streamUrl);

            mp.setOnPreparedListener(this);
            mp.setOnErrorListener(this);
            mp.setOnCompletionListener(this);
            mp.prepareAsync();
        } catch(Exception e) {
            e.printStackTrace();
            Log.d("debugerino", "setupMediaPlayer error: " + e.getMessage());
        }
    }

    private void setupMetaFetcher(JSONObject response) {
        if(response == null) {
            Log.d("debugerino", "server returned no metadata response");
            return;
        }

        try {
            metadataUrl = response.getString("metadataUrl");
            Log.d("debugerino", "metadataUrl: " + metadataUrl);

            mSocket = IO.socket(metadataUrl);
            mSocket.connect();

            mSocket.on("song change", onSongChange);
        } catch(Exception e) {
            e.printStackTrace();
            Log.d("debugerino", "setupMetaFetcher error: " + e.getMessage());
        }
    }


    @Override
    public void onPrepared(FFmpegMediaPlayer mp) {
        Log.i("debugerino", "prepare finished");
        mp.start();
    }

    @Override
    public void onCompletion(FFmpegMediaPlayer mp) {
        Log.i("debugerino", "mp completion -- ui should indicate the stream is paused, pressing pause button restarts stream");
    }

    @Override
    public boolean onError(FFmpegMediaPlayer mp, int what, int extra) {
        Log.i("debugerino", "mp error" + what + ", " + extra);
        return false;
    }

    private Emitter.Listener onSongChange = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            for(Object obj : args) {
                JSONObject data = (JSONObject)obj;
                Log.d("debugerino", data.toString());
                try {
                    currentMetadata = (JSONObject)data.get("metadata");
                    notifyObservers();
                } catch(Exception e) {
                    currentMetadata = null;
                    e.printStackTrace();
                }

            }
        }
    };

    public static void pause() {
        if(instance.mp != null && instance.mp.isPlaying())
            instance.mp.stop();
    }

    public static void play() {
        if(instance.mp == null) {
            try {
                instance.fetchStream();
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else if(!instance.mp.isPlaying()) {
            instance.mp.start();
        }
    }

    public static boolean isPlaying(){
        return instance.mp.isPlaying();
    }

    public static String getSongTitle() {
        JSONObject metadata = instance.currentMetadata;
        if (metadata != null) {
            try {
                return metadata.getString("StreamTitle");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return instance.mp != null && instance.mp.isPlaying() ? "Unknown song" : "Stream offline";
    }

    public static void destroy() {
        if(instance.mSocket != null) {
            instance.mSocket.disconnect();
            instance.mSocket.off("song change", instance.onSongChange);
        }

        if(instance.mp != null)
            instance.mp.release();
    }

    public static void register(SongListener listener) {
        observers.add(listener);
        //Log.d("debugerino", "registered observer " + observers.size());
    }

    private void notifyObservers() {
        //Log.d("debugerino", "notifyObservers1 - " + observers.size());
        if(observers.size() == 0)
            return;

        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Log.d("debugerino", "notifyObservers2 - " + observers.size());
                for(SongListener listener : observers) {
                    listener.onSongChange();
                }
            }
        });
    }


    public static void unregister(SongListener listener) {
        observers.remove(listener);
    }
}
