package com.aesthetics.simple.grouper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.aesthetics.simple.grouper.com.simpleaesthetics.application.Model.Course;
import com.aesthetics.simple.grouper.com.simpleaesthetics.application.Model.Environment;
import com.aesthetics.simple.grouper.com.simpleaesthetics.application.Model.Group;
import com.aesthetics.simple.grouper.com.simpleaesthetics.application.Model.University;
import com.aesthetics.simple.grouper.com.simpleaesthetics.application.Model.User;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainLists extends AppCompatActivity {
    private FloatingActionButton btn_Maps,btn_Chat;
    private ImageButton btn_Back;
    private TextView txt_Title;
    private SimpleAdapter adapter;
    private ListView lst_Main;
    private PopupMenu popup;
    private int state = 0;
    private int univ,cour,envi;

    //Lists;
    private List<Map<String, String>> lst_Universities;
    private List<Map<String, String>> lst_Courses;
    private List<Map<String, String>> lst_Environments;
    private List<Map<String, String>> lst_People;

    private List<Integer> lst_ID_Universities;
    private List<Integer> lst_ID_Courses;
    private List<Integer> lst_ID_Environments;
    private List<Integer> lst_ID_People;
    private VirtualServer VS;
    private Group g;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        VS = new VirtualServer();
        ImageButton btn_Menu,btn_search;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lists);

        //Initialize buttons;
        btn_Maps = (FloatingActionButton)(findViewById(R.id.btn_Map)) ;
        btn_Chat = (FloatingActionButton)(findViewById(R.id.btn_Chat)) ;
        btn_Back = (ImageButton) findViewById(R.id.btn_Back);
        btn_Back.setImageResource(R.drawable.ic_back);
        btn_Menu = (ImageButton) findViewById(R.id.btn_menu);
        btn_Menu.setImageResource(R.drawable.ic_menu);
        btn_search = (ImageButton) findViewById(R.id.btn_search);
        btn_search.setImageResource(R.drawable.ic_search);

        popup = new PopupMenu(this,btn_Menu);
        popup.getMenu().add("Exit");
        //Initialize title;
        txt_Title = (TextView) findViewById(R.id.txt_Title);
        txt_Title.setText("Unknown");

        //Set list content;
        lst_Main = (ListView) findViewById(R.id.lst_Main);
        lst_Main.setAdapter(adapter);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return false;
            }
        });

        btn_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.show();
            }
        });

        view_Universities(true);

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAStep();
            }
        });

        view_Universities(true);

    }

    private void set_List(List<Map<String, String>> lst_cur, boolean lst_type){
        int layout = lst_type?android.R.layout.simple_list_item_2:R.layout.simple_list_item;
        adapter = new SimpleAdapter(this, lst_cur,
                layout,
                new String[]{"H1","Sub"},
                new int[] {android.R.id.text1, android.R.id.text2});
        lst_Main.setAdapter(adapter);


    }

    /*
    private String newUniversityListItem(String Name, int num){
        String out = Name + "\n" + num + " available course";
        if (num!=1) out=out+"s";
        return out;
    }

    private String newCourseListItem(String Name, int CompNum, int pendNum){
        String out = Name + "\n" + CompNum + " Completed Environment";
        if (CompNum!=1) out+="s\n";
        out+=pendNum+" Incomplete Environment";
        if (pendNum!=1) out+="s";
        return out;
    }



    private String newPeopleListItem(String Name, float match, boolean linked){
        String out = Name+"\n" + match + "% Match"+(linked?"\nLINKED":"");
        return out;
    }

    */

    private String newEnvironmentListItem(String Name, int groupNum, int membNum, boolean formed, Date dueDate){
        String out = "";
        if (formed){
            out+=groupNum+" Group";
            if (groupNum!=1) out+="s";
            out+=" Formed\n"+membNum+" Member";
            if (membNum!=1) out+="s";
        }
        else{
            out+=membNum+" Member";
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (membNum!=1) out+="s\nDeadline: "+dateFormat.format(dueDate);
        }
        return out;
    }

    private void backAStep(){
        if(state==0)
            finish();
        else if(state==1)
            view_Universities(false);
        else if(state==2)
            view_Courses(false);
        if (state==3)
            view_Environments(false);
    }

    private Map<String, String> newListItem(String H1, String Sub){
        Map<String, String> data = new HashMap<>(2);
        data.put("H1",H1);
        data.put("Sub",Sub);
        return data;
    }

    @SuppressLint("SetTextI18n")
    public void view_Universities(boolean reload){
        state=0;
        txt_Title.setText("    Universities");
        btn_Maps.setVisibility(View.GONE);
        btn_Chat.setVisibility(View.GONE);
        btn_Back.setVisibility(View.GONE);
        lst_Universities    = new ArrayList<>();
        lst_ID_Universities = new ArrayList<>();
        for(University Un : VS.Universities){
            lst_Universities.add(newListItem(Un.getName(),""));
        }
        set_List(lst_Universities,false);
        lst_Main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get Courses from here; using on click id.
                univ = position;
                view_Courses(true);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void view_Courses(boolean reload){
        state=1;
        txt_Title.setText("Courses");
        btn_Maps.setVisibility(View.GONE);
        btn_Chat.setVisibility(View.GONE);
        btn_Back.setVisibility(View.VISIBLE);
        lst_Courses    = new ArrayList<>();
        lst_ID_Courses = new ArrayList<>();
        for(Course Un : VS.Universities.get(univ).coursesList){
            boolean Formed;

            lst_Courses.add(newListItem(Un.getName(),""));
        }
        set_List(lst_Courses,false);
        lst_Main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get Courses from here; using on click id.
                cour = position;
                view_Environments(true);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void view_Environments(boolean reload){
        state=2;
        txt_Title.setText("Environments");
        btn_Back.setVisibility(View.VISIBLE);
        btn_Maps.setVisibility(View.GONE);
        btn_Chat.setVisibility(View.GONE);

        lst_Environments    = new ArrayList<>();
        lst_ID_Environments = new ArrayList<>();
        for(Environment Un: VS.Universities.get(univ).coursesList.get(cour).envList) {
            Date timeStamp = Calendar.getInstance().getTime();
            lst_Environments.add(newListItem(Un.getName(), newEnvironmentListItem("",Un.groups.size(),Un.users.size(),timeStamp.after(Un.getDeadline()),Un.getDeadline())));
        }
        set_List(lst_Environments,true);
        lst_Main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get Courses from here; using on click id.
                envi = position;
                Date timeStamp = Calendar.getInstance().getTime();
                Date duedate = VS.Universities.get(univ).coursesList.get(cour).envList.get(envi).getDeadline();
                if(timeStamp.after(duedate)){
                    viewGroup();
                }
                else view_Environments(true);
            }
        });
    }

    public void viewGroup(){
        state=3;
        btn_Maps.setVisibility(View.VISIBLE);
        btn_Chat.setVisibility(View.VISIBLE);
        g = null;
        Set<Group> gSet = VS.Universities.get(univ).coursesList.get(cour).envList.get(envi).getGroups();
        for(Group G : gSet){
            if(G.getName().equals(CurrentUser.currentGroup))
                g=G;
        }
        //Get Group;
        txt_Title.setText(g.getName());
        lst_People = new ArrayList<>();
        for(User M : g.getGroupMembers()){
            lst_People.add(newListItem(M.getNickname(),""));
        }
        set_List(lst_People,false);
        btn_Maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] addr = g.getMeetingLocation().split(":");
                Uri gmmIntentUri = Uri.parse("geo:"+addr[0]+","+addr[1]+addr[2]);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        btn_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo add chat view;
            }
        });
    }

    /*
    public void view_People(){
        state=3;
        txt_Title.setText("People");
        btn_Back.setVisibility(View.VISIBLE);
        lst_People    = new ArrayList<>();
        lst_ID_People = new ArrayList<>();
        for(User Un: VS.Universities.get(univ).coursesList.get(cour).envList.get(envi).getUsers()) {
            Date timeStamp = Calendar.getInstance().getTime();
            lst_Environments.add(newListItem(Un.getName(), newEnvironmentListItem("",Un.groups.size(),Un.users.size(),timeStamp.after(Un.getDeadline()),Un.getDeadline())));
        }
        set_List(lst_Environments,true);
        lst_Main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get Courses from here; using on click id.
                envi = position;

                view_Environments(true);
            }
        });
    }
    */

    @Override
    public void onBackPressed() {
        backAStep();
    }
}
