package reboot.grouper.FrontEnd;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import reboot.grouper.Model.University;
import reboot.grouper.UI.Lists;


public class REST {

    private Response.Listener<String>       StrResponse;
    private Response.ErrorListener          ErrResponse;
    private Lists list;
    private  Dispatcher control;

    public REST(Lists l,Dispatcher lc){
        list=l;
        control=lc;
    }


    public void post(final Object obj, String url) throws JSONException {
        listeners(url);
        StringRequest req = new StringRequest(Request.Method.POST, url,StrResponse,ErrResponse ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();
                return gson.toJson(obj).getBytes();
            }
        };
        Volley.I(list).addtoReqQueue(req);
    }

    private void listeners(final String url){
        ErrResponse = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(list.getApplicationContext(), url + " : " + error.toString(),
                        Toast.LENGTH_SHORT).show();
                control.onError();
            }
        };
        StrResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(list.getApplicationContext(), "Success!",
                        Toast.LENGTH_SHORT).show();
                control.updateView();
            }
        };
    }

    public void getList(String url) throws JSONException {
        listeners(url);
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                control.onResponseList(response);
            }
        },ErrResponse);
        Volley.I(list).addtoReqQueue(req);
    }

    //
    public void getUsers(String url) throws JSONException {
        listeners(url);
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                control.onResponseList(response);
            }
        },ErrResponse);
        Volley.I(list).addtoReqQueue(req);
    }

}

