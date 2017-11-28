package reboot.grouper.FrontEnd;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by visha on 2017-10-26.
 */

//As a singleton;
public final class Volley {
    private static volatile Volley instance= null;
    private RequestQueue requestQueue;
    private static Context context;
    private String Address;

    private Volley(Context c){
        context = c;
        Address = "http://grouper-server-deployment.cfapps.io/";
        //Address = "http://192.168.2.69:8080/";
        //Address = "http://172.17.135.12:8080/";
    }

    public String getAddress(){ getRequestQueue(); return Address; }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public<T> void addtoReqQueue(Request<T> request){
        getRequestQueue().add(request);
    }

    //Get Instance;
    public static Volley I(Context context){
        if (instance == null) {
            synchronized(Volley.class) {
                if (instance == null) instance = new Volley(context);
            }
        }
        return instance;
    }

}
