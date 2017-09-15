package com.rowdybeats.rowdybeats.network;

import android.content.Context;
import android.provider.Settings.Secure;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rowdybeats.rowdybeats.network.login.LoginManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bryceboesen on 8/11/17.
 */

public class APIRequest extends JsonObjectRequest {
    private Map<String, String> headers;
    private Context context;

    /**
     * Creates a new request.
     * @param method the HTTP method to use
     * @param url URL to fetch the JSON from
     * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
     *   indicates no parameters will be posted along with request.
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public APIRequest(Context context, int method, String url, JSONObject jsonRequest,
                             Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);

        this.headers = new HashMap<String, String>();
        this.context = context;
    }

    @Override
    public Map<String, String> getHeaders() {
        headers.put("authToken", LoginManager.getAuthToken());
        headers.put("deviceId", Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID));
        headers.put("requestAt", "" + System.currentTimeMillis());

        return headers;
    }
}
