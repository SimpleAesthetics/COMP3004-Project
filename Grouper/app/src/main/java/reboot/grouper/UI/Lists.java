package reboot.grouper.UI;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import reboot.grouper.FrontEnd.Dispatcher;
import reboot.grouper.FrontEnd.UserSession;
import reboot.grouper.Model.Course;
import reboot.grouper.R;

public class Lists extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Dispatcher controller;

    public  ListView                lst_Main;
    private SimpleAdapter           adapter;
    private Toolbar                 toolbar;
    private NavigationView          navigationView;
    private int                     list_Menu;
    private RelativeLayout          loadingBar;
    private FloatingActionButton    btn_Maps;
    private FloatingActionButton    btn_Chat;
    private MenuItem                admin;
    private MenuItem                addUniv;
    private MenuItem                addCour;
    private MenuItem                addEnvi;
    private MenuItem                editGroup;
    private MenuItem                leaveEnvir;
    private TextView                fn;
    private TextView                un;
    private EditText                Text_Input;
    private SearchView              searchView;
    private MenuItem                searchMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        toolbar = findViewById(R.id.toolbar);
        loadingBar = findViewById(R.id.loadingPanel);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onStart(){
        super.onStart();
        navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);


        btn_Maps    = findViewById(R.id.btn_Map);
        btn_Chat    = findViewById(R.id.btn_Chat);
        View headerLayout = navigationView.getHeaderView(0);
        fn = headerLayout.findViewById(R.id.lbl_FULLNAME);
        un = headerLayout.findViewById(R.id.lbl_USERNAME);

        admin       = navigationView.getMenu().getItem(3);
        addUniv     = admin.getSubMenu().getItem(0);
        addCour     = admin.getSubMenu().getItem(1);
        addEnvi     = admin.getSubMenu().getItem(2);
        editGroup   = admin.getSubMenu().getItem(3);
        leaveEnvir  = admin.getSubMenu().getItem(4);

        lst_Main = findViewById(R.id.lst_Main);
        lst_Main.setAdapter(adapter);

        list_Menu = 0;
        show_Admin(0);
        set_Floating_Buttons(false);

        controller = new Dispatcher(this);
        controller.updateView();
        show_Username_In_Dash();
        lst_Main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                controller.progress_A_Step(position);

                searchMenu.collapseActionView();
                searchView.setQuery("", false);
                controller.clearSearch();
            }
        });
    }

    public void show_Username_In_Dash(){
        un.setText(UserSession.I().getUser().getNickname());
        fn.setText(UserSession.I().getUser().getFirstName() + " " + UserSession.I().getUser().getLastName());
    }

    public void text_Input_Dialog(DialogInterface.OnClickListener listener, String title){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        TextView txt_view = promptView.findViewById(R.id.lbl_Title);
        txt_view.setText(title);
        Text_Input = promptView.findViewById(R.id.txt_Input);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Submit", listener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }});

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void course_Create_Dialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.course_input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        final TextView CourseName = promptView.findViewById(R.id.txt_Name);
        final TextView CourseCode = promptView.findViewById(R.id.txt_Code);
        final TextView Instructor = promptView.findViewById(R.id.txt_Instructor);


        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Course c = new Course();
                        c = new Course();
                        c.setName(CourseName.getText().toString());
                        c.setCourseCode(CourseCode.getText().toString());
                        controller.Create_Course(c);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }});

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public int get_Drawer_Select(){
        return list_Menu;
    }

    private void create_University(){
        controller.create_University();
    }

    public void set_List(List<Map<String, String>> lst_cur, int lst_type){
        if(lst_type>0) {
            adapter = new SimpleAdapter(this, lst_cur,
                    android.R.layout.simple_list_item_2,
                    new String[]{"H1", "H2"},
                    new int[]{android.R.id.text1, android.R.id.text2});
            lst_Main.setAdapter(adapter);
        }
        else{
            adapter = new SimpleAdapter(this, lst_cur,
                    R.layout.simple_list_item,
                    new String[]{"H1"},
                    new int[]{android.R.id.text1});
            lst_Main.setAdapter(adapter);
        }
    }

    public String get_Text_Input(){ return Text_Input.getText().toString(); }

    public void set_Floating_Buttons(Boolean visible){
        if(visible) {
            btn_Maps.setVisibility(View.VISIBLE);
            btn_Chat.setVisibility(View.VISIBLE);
        }
        else{
            btn_Maps.setVisibility(View.GONE);
            btn_Chat.setVisibility(View.GONE);
        }
    }

    public void set_Title(String title){
        toolbar.setTitle(title);
        setTitle(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            controller.back_A_Step();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        searchMenu = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()));
        searchView.setFocusable(false);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                controller.doSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                controller.doSearch(s);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.requestFocus();
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchMenu.collapseActionView();
                searchView.setQuery("", false);
                controller.clearSearch();
                return false;
            }
        });

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        searchView.setFocusable(true);
        searchView.requestFocus();
        searchView.setIconified(false);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void show_Admin(int i){
        switch (i){
            case 0 :
                admin.setVisible(false);
                break;
            case 1 :
                admin.setVisible(true);
                addUniv.setVisible      (true);
                addCour.setVisible      (false);
                addEnvi.setVisible      (false);
                editGroup.setVisible    (false);
                leaveEnvir.setVisible   (false);
                break;
            case 2 :
                admin.setVisible(true);
                addUniv.setVisible      (false);
                addCour.setVisible      (true);
                addEnvi.setVisible      (false);
                editGroup.setVisible    (false);
                leaveEnvir.setVisible   (false);
                break;
            case 3 :
                admin.setVisible(true);
                addUniv.setVisible      (false);
                addCour.setVisible      (false);
                addEnvi.setVisible      (true);
                editGroup.setVisible    (false);
                leaveEnvir.setVisible   (false);
                break;
            case 4 :
                admin.setVisible(true);
                addUniv.setVisible      (false);
                addCour.setVisible      (false);
                addEnvi.setVisible      (false);
                editGroup.setVisible    (true);
                leaveEnvir.setVisible   (false);
                break;
            case 5 :
                admin.setVisible(true);
                addUniv.setVisible      (false);
                addCour.setVisible      (false);
                addEnvi.setVisible      (false);
                editGroup.setVisible    (false);
                leaveEnvir.setVisible   (true);
                break;
            case 6 :
                admin.setVisible(true);
                addUniv.setVisible      (true);
                addCour.setVisible      (true);
                addEnvi.setVisible      (true);
                editGroup.setVisible    (true);
                leaveEnvir.setVisible   (true);
                break;
        }
    }

    public void set_Loading(boolean load){
        if(load)    loadingBar.setVisibility(View.VISIBLE);
        else        loadingBar.setVisibility(View.GONE);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if      (id == R.id.explore){ list_Menu = 0; controller.updateView();}
        else if (id == R.id.jgroups){ list_Menu = 1; controller.updateView();}
        else if (id == R.id.waiting){ list_Menu = 2; controller.updateView();}
        else{
            //eg
            if(id == R.id.exit){
                finish();
            }
            else if(id == R.id.logout){
                /*

                Clear user data;

                */
                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();

            }
            else if(id == R.id.addUniv) create_University();
            else if(id == R.id.addCour) course_Create_Dialog();
            else if(id == R.id.addEnvi) {
                Intent I = new Intent(this,NewEnvironment.class);
                startActivity(I);
            }

            navigationView.getMenu().getItem(list_Menu).setChecked(true);
        }

        /*
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
