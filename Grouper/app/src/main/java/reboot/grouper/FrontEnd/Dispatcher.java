package reboot.grouper.FrontEnd;

import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import reboot.grouper.Model.Course;
import reboot.grouper.Model.Environment;
import reboot.grouper.Model.University;
import reboot.grouper.Model.User;
import reboot.grouper.UI.Lists;

/**
 * Created by visha on 2017-11-07.
 */

@SuppressWarnings("serial")
public class Dispatcher implements Serializable {
    public static Dispatcher inst;

    private Lists lists; //The lists activity;
    private Retrofit_API retrofit;

    private List<Map<String, String>>   lst_Display;
    private List<String>                lst_ID;
    private List<Environment>           lst_Formed_List;

    private List<Map<String, String>>   lst_Search_Display;
    private List<String>                lst_Search_ID;
    private List<Environment>           lst_Search_Formed_List;
    private Boolean                     isSearch;

    private String univ,cour,envi,grou;
    private STATE state;

    private Response.Listener<JSONArray>    LstResponse_Single;
    private Response.Listener<JSONArray>    LstResponse_Environment;
    private Response.Listener<String>       StrResponse;
    private Response.ErrorListener          ErrResponse;

    private Response<List<University>>  UnivResponse;
    private Response<List<Course>>      CourResponse;
    private Response<List<Environment>> EnviResponse;

    private Map<String, List<String>>   questionnaire;
    private Environment Envir;
    private int selectedenv;



    public Environment getEnvir(){ return Envir; }

    public Map<String, List<String>> getQuestionnaire(){ return questionnaire; };

    private REST rest;

    private DialogInterface.OnClickListener Create_University;

    private enum STATE{ UNIV, COURSES, ENVI, FORMATION, GROUPS, JOINED, WAITING }

    public Dispatcher(Lists lst){
        lists = lst;
        state = STATE.UNIV;
        init_Function_Listeners();
        inst = this;
        retrofit = Retrofit_Client.getAPI();
        isSearch = false;
    }

    public static Dispatcher getList(){ return inst; }

    public void updateView(){
        lists.set_Loading(true);
        //get_Menu_Selection();
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
        isSearch = false;
        selectedenv = 0;
        questionnaire = null;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, create_GET_URL(),null, LstResponse_Single, ErrResponse) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                String[] auth = UserSession.credentials();
                headers.put(auth[0], (auth[1]));
                return headers;
            }
        };

        if(state == STATE.ENVI){
            req = new JsonArrayRequest(Request.Method.GET, create_GET_URL(),null, LstResponse_Environment, ErrResponse) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap();
                    String[] auth = UserSession.credentials();
                    headers.put(auth[0], (auth[1]));
                    return headers;
                }
            };
            System.out.println("Environments!");
        }
        if(state == STATE.FORMATION){
            PreviewUsers();
        }
        else {

        }
        if(state == STATE.UNIV || state == STATE.COURSES ||state == STATE.ENVI) {
            Volley.I(lists).addtoReqQueue(req);
        }
    }

    public void setupQuiz(Map<String, List<String>> quiz){
        questionnaire = quiz;
    }

    public void doSearch(String Query){
        lst_Search_Display      = new ArrayList<>();
        lst_Search_ID           = new ArrayList<>();
        lst_Search_Formed_List  = new ArrayList<>();
        for(int i = 0; i < lst_Display.size(); i++){
            if(lst_ID.get(i).toUpperCase().contains(Query.toUpperCase())){
                lst_Search_ID.add(lst_ID.get(i));
                lst_Search_Display.add(lst_Display.get(i));
                if(lst_Formed_List.size()==lst_ID.size())
                    lst_Search_Formed_List.add(lst_Formed_List.get(i));
            }
        }
        lists.set_List(lst_Search_Display,-1);
        isSearch = true;
    }

    public void clearSearch(){
        lst_Search_Display      = new ArrayList<>();
        lst_Search_ID           = new ArrayList<>();
        lst_Search_Formed_List  = new ArrayList<>();
        isSearch = false;
    }

    public void progress_A_Step(int id){
        /* Transition to next list */
        switch (state){
            case UNIV       :
                univ = isSearch?lst_Search_ID.get(id):lst_ID.get(id);
                state = STATE.COURSES;
                updateView();
                break;
            case COURSES    :
                cour = isSearch?lst_Search_ID.get(id):lst_ID.get(id);
                state = STATE.ENVI;
                updateView();
                break;
            case ENVI       :
                envi = isSearch?lst_Search_ID.get(id):lst_ID.get(id);
                lists.show_env_settings(lst_Formed_List.get(id));
                selectedenv = id;
                break;
            case FORMATION  :lists.show_Admin(4); break;
            case GROUPS     :lists.show_Admin(5); break;
            case JOINED     :lists.show_Admin(0); break;
            case WAITING    :lists.show_Admin(6); break;

        }
        clearSearch();

    }

    public void enter_environment(Environment E){
        Envir = E;
        if(E.getGroups().size()>0){
            /* Show Groups */
            state = STATE.GROUPS;
        }
        else{
            state = STATE.FORMATION;
        }
        clearSearch();
    }

    public void PreviewUsers(){
        lst_Display     = new ArrayList<>();
        lst_ID          = new ArrayList<>();
        if(Envir!=null) {
            Set<User> users = Envir.getUsers();
            for (User u : users) {
                lst_Display.add(new_List_Item(u.getNickname(), ""));
                lst_ID.add(u.getNickname());
            }
            lists.set_List(lst_Display, -1);
        }
        else updateView();
        lists.set_Loading(false);
    }

    public void back_A_Step(){
        switch (state){
            case UNIV       : lists.finish(); return;
            case COURSES    :
                cour = "";
                state = STATE.UNIV;
                break;
            case ENVI       :
                envi = "";
                state = STATE.COURSES;
                /* Check for environment formation status */
                break;
            case FORMATION  :lists.show_Admin(4); state = STATE.ENVI; break;
            case GROUPS     :lists.show_Admin(5); state = STATE.ENVI; break;
            case JOINED     :lists.show_Admin(0); break;
            case WAITING    :lists.show_Admin(6); break;

        }
        updateView();
    }

    private void set_Title(){
        switch (state){
            case UNIV       :lists.set_Title("Universities"     ); break;
            case COURSES    :lists.set_Title("Courses"          ); break;
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
    /*
    private Map<String, String> new_List_Item(String... args){
        Map<String, String> data = new HashMap<>(1);
        for(int i = 0; i < args.length; i++){
            data.put("H"+i,args[i]);
        }
        return data;
    }
    */
    private Map<String, String> new_List_Item(String arg1, String arg2){
        Map<String, String> data = new HashMap<>(1);
        data.put("H1",arg1);
        data.put("H2",arg2);
        return data;
    }

    public void onError(){
        lst_Display     = new ArrayList<>();
        lst_ID          = new ArrayList<>();
        lst_Formed_List = new ArrayList<>();
        lists.set_List(lst_Display,-1);
        lists.set_Loading(false);
    }

    public void onResponseList(JSONArray response) {
        lst_Display     = new ArrayList<>();
        lst_ID          = new ArrayList<>();
        lst_Formed_List = new ArrayList<>();

        for(int i=0; i<response.length();i++){
            try {
                String elem = response.getJSONArray(i).getString(1);
                lst_ID.add(elem);
                lst_Display.add(new_List_Item(elem,""));
            } catch (Exception e){  e.printStackTrace(); }
        }
        lists.lst_Main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                progress_A_Step(i);
            }
        });
        lists.set_List(lst_Display,-1);
        lists.set_Loading(false);
    }

    public void create_University(){
        lists.text_Input_Dialog(Create_University,"Add a University");
    }

    public void Create_Course(Course c){
        final Course course = c;
        String URL = Volley.I(lists).getAddress() + "universities/"+univ+"/courses";
        StringRequest req = new StringRequest(Request.Method.POST, URL, StrResponse, ErrResponse){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", "application/json; charset=utf-8");
                String[] auth = UserSession.credentials();
                headers.put(auth[0],(auth[1]));
                return headers;
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();
                return gson.toJson(course).getBytes();
            }
        };
        Volley.I(lists).addtoReqQueue(req);
    }
    public void Create_Environment(Environment e){
        final Environment environment = e;
        String URL = Volley.I(lists).getAddress() + "universities/"+univ+"/courses/"+cour+"/environments";
        StringRequest req = new StringRequest(Request.Method.POST, URL, StrResponse, ErrResponse){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", "application/json; charset=utf-8");
                String[] auth = UserSession.credentials();
                headers.put(auth[0],(auth[1]));
                return headers;
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();
                System.out.print(gson.toJson(environment));
                return gson.toJson(environment).getBytes();
            }
        };
        Volley.I(lists).addtoReqQueue(req);
    }

    public void setQuestionnaireResponse(User usr, String e){
        final User user = usr;
        String URL = Volley.I(lists).getAddress() + "universities/"+univ+"/courses/"+cour+"/environments/"+e+"/users";
        StringRequest req = new StringRequest(Request.Method.POST, URL, StrResponse, ErrResponse){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", "application/json; charset=utf-8");
                String[] auth = UserSession.credentials();
                headers.put(auth[0],(auth[1]));
                return headers;
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();
                System.out.print(gson.toJson(user));
                return gson.toJson(user).getBytes();
            }
        };
        Volley.I(lists).addtoReqQueue(req);
    }

    private void init_Function_Listeners(){
        LstResponse_Single = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                lst_Display     = new ArrayList<>();
                lst_ID          = new ArrayList<>();
                lst_Formed_List = new ArrayList<>();

                for(int i=0; i<response.length();i++){
                    try {
                        String elem = response.getJSONObject(i).getString("name");
                        lst_ID.add(elem);
                        lst_Display.add(new_List_Item(elem,""));
                        System.out.println(elem);
                    } catch (Exception e){  e.printStackTrace(); }
                }
                lists.lst_Main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        progress_A_Step(i);
                    }
                });
                lists.set_List(lst_Display,-1);
                lists.set_Loading(false);
            }
        };

        LstResponse_Environment = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                lst_Display     = new ArrayList<>();
                lst_ID          = new ArrayList<>();
                lst_Formed_List = new ArrayList<>();

                for(int i=0; i<response.length();i++){
                    try {
                        String elem = response.getJSONObject(i).getString("name");
                        lst_ID.add(elem);
                        lst_Display.add(new_List_Item(elem,""));
                        JSONObject env = response.getJSONObject(i);
                        lst_Formed_List.add(new Gson().fromJson(env.toString(),Environment.class));
                        System.out.println(elem);
                    } catch (Exception e){  e.printStackTrace(); }
                }
                lists.lst_Main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        progress_A_Step(i);
                    }
                });
                lists.set_List(lst_Display,-1);
                lists.set_Loading(false);
            }
        };




        ErrResponse = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(create_GET_URL() + " : " + error.toString());
                lst_Display     = new ArrayList<>();
                lst_ID          = new ArrayList<>();
                lst_Formed_List = new ArrayList<>();
                lists.set_List(lst_Display,-1);
                lists.set_Loading(false);
            }
        };

        StrResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(lists.getApplicationContext(), "Success!",
                        Toast.LENGTH_SHORT).show();
                updateView();
            }
        };

        Create_University = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String URL = Volley.I(lists).getAddress() + "universities";
                final University Uni = new University(lists.get_Text_Input());
                StringRequest req = new StringRequest(Request.Method.POST, URL, StrResponse, ErrResponse){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        String[] auth = UserSession.credentials();
                        headers.put(auth[0],(auth[1]));
                        return headers;
                    }
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        Gson gson = new Gson();
                        return gson.toJson(Uni).getBytes();
                    }
                };
                Volley.I(lists).addtoReqQueue(req);
            }
        };
    /*
        Create_Course = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String URL = Volley.I(lists).getAddress() + "universities/"+univ+"/courses";
                final Course Elem = new Course(lists.get_Text_Input());
                StringRequest req = new StringRequest(Request.Method.POST, URL, StrResponse, ErrResponse){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return Elem.toString().getBytes();
                    }
                };
                Volley.I(lists).addtoReqQueue(req);
            }
        };
        */
    }
}
