package com.rowdybeats.rowdybeats.network;

/**
 * Created by bryceboesen on 8/1/17.
 */

import com.android.volley.Request;
import com.android.volley.Request.Method;

public enum APITaskType {
    GET("GET", Request.Method.GET),
    POST("POST", Request.Method.POST),
    PUT("PUT", Request.Method.PUT),
    DELETE("DELETE", Request.Method.DELETE);

    private String name;
    private int method;

    private APITaskType(String name, int method) {
        this.name = name;
        this.method = method;
    }

    public String toString() {
        return this.name;
    }

    public int getInt() { return this.method; }
}