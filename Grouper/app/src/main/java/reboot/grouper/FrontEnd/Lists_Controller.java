package reboot.grouper.FrontEnd;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import reboot.grouper.UI.Lists;

/**
 * Created by visha on 2017-11-07.
 */

public class Lists_Controller {
    private Lists lists; //The lists activity;

    private List<Map<String, String>> lst_Display;
    private List<String> lst_ID;

    private String univ,cour,envi,grou;
    private STATE state;

    private Response.Listener<JSONArray>    LstResponse;
    private Response.Listener<JSONObject>   ObjResponse;
    private Response.ErrorListener          ErrResponse;


    private enum STATE{ UNIV, COURSES, ENVI, FORMATION, GROUPS, JOINED, WAITING }

    public Lists_Controller(Lists lst){
        lists = lst;
        state = STATE.UNIV;
        init_Response_Listeners();
    }

    public void updateView(){
        lists.set_Loading(true);
        get_Menu_Selection();
        set_Title();
        switch (state){
            case UNIV       :lists.show_Admin(1); break;
            case COURSES    :lists.show_Admin(2); break;
            case ENVI       :lists.show_Admin(3); break;
            case FORMATION  :lists.show_Admin(4); break;
            case GROUPS     :lists.show_Admin(5); break;
            case JOINED     :lists.show_Admin(0); break;
            case WAITING    :lists.show_Admin(6); break;
        }
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, create_GET_URL(),null, LstResponse, ErrResponse);
        Volley.I(lists).addtoReqQueue(req);
    }


    private void set_Title(){
        switch (state){
            case UNIV       :lists.set_Title("Universities"     ); break;
            case COURSES    :lists.set_Title("Courses     "     ); break;
            case ENVI       :lists.set_Title("Environments"     ); break;
            case FORMATION  :lists.set_Title( envi              ); break;
            case GROUPS     :lists.set_Title( grou              ); break;
            case JOINED     :lists.set_Title("Joined Groups"    ); break;
            case WAITING    :lists.set_Title("Waiting List"     ); break;
        }
    }

    private void get_Menu_Selection(){
        int select = lists.get_Drawer_Select();
        switch(select){
            case 0 : state = STATE.UNIV     ; break;
            case 1 : state = STATE.JOINED   ; break;
            case 2 : state = STATE.WAITING  ; break;
        }
    }

    private String create_GET_URL(){
        String out = "";
        switch (state){
            case FORMATION  : out+= "";
            case GROUPS     : out = "/" + envi + "/" +out;
            case ENVI       : out = "/" + cour + "/environments" + out;
            case COURSES    : out = "/" + univ + "/courses" + out;
            case UNIV       : out = "universities" + out;
        }
        if(state==STATE.FORMATION)  out+="users";
        if(state==STATE.GROUPS)     out+="groups";
        out = Volley.I(lists).getAddress() + out;
        return out;
    }

    private Map<String, String> new_List_Item(String... args){
        Map<String, String> data = new HashMap<>(1);
        for(int i = 0; i < args.length; i++){
            data.put("H"+i,args[i]);
        }
        return data;
    }

    private void init_Response_Listeners(){
        LstResponse = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                lst_Display = new ArrayList<>();
                lst_ID      = new ArrayList<>();
                for(int i=0; i<response.length();i++){
                    try {
                        String elem = response.getJSONArray(i).getString(1);
                        lst_ID.add(elem);
                        lst_Display.add(new_List_Item(elem,""));
                    } catch (Exception e){  e.printStackTrace(); }
                }
                lists.set_List(lst_Display,0);
                lists.set_Loading(false);
            }
        };

        ErrResponse = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(lists.getApplicationContext(), create_GET_URL() + " : " + error.toString(),
                        Toast.LENGTH_SHORT).show();
                lists.set_Loading(false);
            }
        };
    }
}
