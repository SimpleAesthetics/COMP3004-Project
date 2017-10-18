package com.aesthetics.simple.grouper;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
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

    //Lists;
    private List<Map<String, String>> lst_Universities;
    private List<Map<String, String>> lst_Courses;
    private List<Map<String, String>> lst_Environments;
    private List<Map<String, String>> lst_Inner;

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

        view_Universities();
    }

    private void set_List(List<Map<String, String>> lst_cur){

        adapter = new SimpleAdapter(this, lst_cur,
                android.R.layout.simple_list_item_2,
                new String[]{"H1","Sub"},
                new int[] {android.R.id.text1, android.R.id.text2});
        ;
        lst_Main.setAdapter(adapter);


    }

    private Map<String, String> newListItem(String H1, String Sub){
        Map<String, String> data = new HashMap<String, String>(2);
        data.put("H1",H1);
        data.put("Sub",Sub);
        return data;
    }

    public void view_Universities(){
        txt_Title.setText("    Universities");
        btn_Back.setVisibility(View.GONE);
        lst_Universities= new ArrayList<Map<String, String>>();
        //Hard Coded universities list;
        lst_Universities.add(newListItem("Carleton University","Ottawa ON\n3 Courses"));
        lst_Universities.add(newListItem("University of Ottawa","Ottawa ON\n4 Courses"));
        lst_Universities.add(newListItem("University of Toronto","Toronto ON\n1 Course"));

        set_List(lst_Universities);
        lst_Main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get Courses from here; using on click id.
                view_Courses();
            }
        });
    }

    public void view_Courses(){
        txt_Title.setText("Courses");
        btn_Back.setVisibility(View.VISIBLE);
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_Universities();
            }
        });
        lst_Courses= new ArrayList<Map<String, String>>();
        set_List(lst_Courses);
        lst_Courses.add(newListItem("COMP3001","4 Completed Environments\n3 Pending Environments"));
        lst_Courses.add(newListItem("COMP3004","3 Completed Environments\n0 Pending Environments"));

        lst_Main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get Courses from here; using on click id.
                view_Environments();
            }
        });
    }

    public void view_Environments(){
        txt_Title.setText("Environment");
        btn_Back.setVisibility(View.VISIBLE);
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_Courses();
            }
        });
        lst_Environments= new ArrayList<Map<String, String>>();
        set_List(lst_Environments);
        lst_Environments.add(newListItem("Fall Project","3 Waiting. 32 Groups.\nEnds 19 October 2017"));
        lst_Environments.add(newListItem("Study Groups","14 Waiting. 3 Groups.\nDue date reached."));
    }

    public void view_Groups(){

    }

    public void view_People(){

    }

    @Override
    public void onBackPressed() {
        //Include the code here
        return;
    }
}
