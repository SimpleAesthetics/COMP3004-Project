package com.aesthetics.simple.grouper;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by visha on 2017-10-26.
 */

//As a singleton;
public final class vClient {
    private static volatile vClient instance= null;
    private RequestQueue requestQueue;
    private static Context context;
    private String Address;

    private vClient(Context c){
        context = c;
        Address = "http://192.168.2.59:8080/";
    }

    public String getAddress(){ getRequestQueue(); return Address; }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public<T> void addtoReqQueue(Request<T> request){
        getRequestQueue().add(request);
    }

    //Get Instance;
    public static vClient I(Context context){
        if (instance == null) {
            synchronized(vClient.class) {
                if (instance == null) instance = new vClient(context);
            }
        }
        return instance;
    }
}
