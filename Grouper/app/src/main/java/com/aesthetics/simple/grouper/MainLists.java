package com.aesthetics.simple.grouper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainLists extends AppCompatActivity {
    private ImageButton btn_Back,btn_Menu,btn_search;
    private TextView txt_Title;
    private SimpleAdapter adapter;
    private String title;
    private ListView lst_Main;
    private PopupMenu popup;
    private int state = 0;
    private int univ,cour,envi,grou;

    //Lists;
    private List<Map<String, String>> lst_Universities;
    private List<Map<String, String>> lst_Courses;
    private List<Map<String, String>> lst_Environments;
    private List<Map<String, String>> lst_Inner;

    private List<Integer> lst_ID_Universities;
    private List<Integer> lst_ID_Courses;
    private List<Integer> lst_ID_Environments;
    private List<Integer> lst_ID_Inner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lists);

        //Initialize buttons;
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

    }

    private void set_List(List<Map<String, String>> lst_cur){

        adapter = new SimpleAdapter(this, lst_cur,
                android.R.layout.simple_list_item_2,
                new String[]{"H1","Sub"},
                new int[] {android.R.id.text1, android.R.id.text2});
        ;
        lst_Main.setAdapter(adapter);


    }

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

    private String newEnvironmentListItem(String Name, int groupNum, int membNum, boolean formed, Date dueDate){
        String out = Name+"\n";
        if (formed){
            out+=groupNum+" Group";
            if (groupNum!=1) out+="s";
            out+=" Formed\n"+membNum+" Member";
            if (membNum!=1) out+="s";
        }
        else{
            out+=membNum+" Member";
            if (membNum!=1) out+="s\nDeadline: "+dueDate.toString();
        }
        return out;
    }

    private String newPeopleListItem(String Name, float match, boolean linked){
        String out = Name+"\n" + match + "% Match"+(linked?"\nLINKED":"");
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
        Map<String, String> data = new HashMap<String, String>(2);
        data.put("H1",H1);
        data.put("Sub",Sub);
        return data;
    }

    public void view_Universities(boolean reload){
        state=0;
        txt_Title.setText("    Universities");
        btn_Back.setVisibility(View.GONE);
        if(reload) {
            lst_Universities    = new ArrayList<Map<String, String>>();
            lst_ID_Universities = new ArrayList<Integer>();
            //Get universities;
        }

        set_List(lst_Universities);
        lst_Main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get Courses from here; using on click id.
                univ = lst_ID_Universities.get((int)position);
                view_Courses(true);
            }
        });
    }

    public void view_Courses(boolean reload){
        state=1;
        txt_Title.setText("Courses");
        btn_Back.setVisibility(View.VISIBLE);
        if(reload) {
            lst_Courses     = new ArrayList<Map<String, String>>();
            lst_ID_Courses  = new ArrayList<Integer>();
        }
        set_List(lst_Courses);

        lst_Main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get Courses from here; using on click id.
                cour=lst_ID_Courses.get((int)position);
                view_Environments(true);
            }
        });
    }

    public void view_Environments(boolean reload){
        state=2;
        txt_Title.setText("Environments");
        btn_Back.setVisibility(View.VISIBLE);
        if(reload) {
            lst_ID_Environments = new ArrayList<Integer>();
            lst_Environments    = new ArrayList<Map<String, String>>();
        }
        set_List(lst_Environments);
        lst_Main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get Courses from here; using on click id.
                envi=lst_ID_Environments.get((int)position);
                view_People();
            }
        });
    }

    public void view_People(){
        state=3;
        txt_Title.setText("People");
        btn_Back.setVisibility(View.VISIBLE);
        lst_ID_Environments = new ArrayList<Integer>();
        lst_Environments    = new ArrayList<Map<String, String>>();
        set_List(lst_Environments);
        lst_Main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get Courses from here; using on click id.
                envi=lst_ID_Inner.get((int)position);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Include the code here
        backAStep();
        return;
    }
}
