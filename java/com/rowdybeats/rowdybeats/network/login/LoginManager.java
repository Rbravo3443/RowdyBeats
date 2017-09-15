package com.rowdybeats.rowdybeats.network.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.rowdybeats.rowdybeats.R;


/**
 * Created by bryceboesen on 8/11/17.
 */

public class LoginManager {
    private static LoginManager instance;
    private static String authToken;

    public static void init(Context context) {
        SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        authToken = settings.getString("authToken", null);
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(Context context, String token) {
        authToken = token;

        SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("authToken", token);
        editor.commit();
    }

    public static boolean isAuthenticated() {
        return getAuthToken() != null;
    }

    public static void logout() {
        authToken = null;
    }
}
