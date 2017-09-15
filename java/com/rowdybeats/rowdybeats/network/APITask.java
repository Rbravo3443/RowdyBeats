package com.rowdybeats.rowdybeats.network;

/**
 * Created by bryceboesen on 7/20/17.
 */

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class APITask {
    private static final String API_URL = "http://dev-env.ypuzdmwitd.us-east-1.elasticbeanstalk.com/"; //server
    //private static final String API_URL = "http://192.168.1.65:8080/"; //local

    private Context context;
    private String endpoint;
    private APITaskType method = APITaskType.GET;

    public APITask(Context context, String endpoint, APITaskType method) {
        super();

        this.context = context;
        this.endpoint = endpoint;
        this.method = method;
    }

    public String getURL() {
        return API_URL + endpoint;
    }

    public void execute(JSONObject payload, final Listener callback) throws Exception {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                callback.onResponse(null, response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                callback.onResponse(error, null);
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        APIRequest request = new APIRequest(context, method.getInt(), getURL(), payload, listener, errorListener);
        queue.add(request);
    }

    public static String directURL(String endpoint) {
        return API_URL + endpoint;
    }

    public interface Listener {
        void onResponse(VolleyError error, JSONObject response);
    }
}