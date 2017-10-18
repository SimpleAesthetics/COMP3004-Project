package com.aesthetics.simple.grouper;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

        //Initialize title;
        txt_Title = (TextView) findViewById(R.id.txt_Title);
        txt_Title.setText("Unknown");

        //Set list content;
        lst_Main = (ListView) findViewById(R.id.lst_Main);
        lst_Main.setAdapter(adapter);

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
        txt_Title.setText("  Universities");
        btn_Back.setVisibility(View.GONE);
        lst_Universities= new ArrayList<Map<String, String>>();
        //Hard Coded universities list;
        lst_Universities.add(newListItem("Carleton University","Ottawa ON"));
        lst_Universities.add(newListItem("University of Ottawa","Ottawa ON"));
        lst_Universities.add(newListItem("University of Toronto","Toronto ON"));

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

    }

    public void view_Environments(){

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
