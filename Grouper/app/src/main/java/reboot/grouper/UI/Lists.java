package reboot.grouper.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import reboot.grouper.FrontEnd.Lists_Controller;
import reboot.grouper.FrontEnd.UserSession;
import reboot.grouper.R;

public class Lists extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Lists_Controller        controller;

    private ListView                lst_Main;
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

        controller = new Lists_Controller(this);
        controller.updateView();
        show_Username_In_Dash();
    }

    public void show_Username_In_Dash(){
        un.setText(UserSession.I().getUser().getNickname());
        fn.setText(UserSession.I().getUser().getFirstName() + " " + UserSession.I().getUser().getLastName());
    }

    public int get_Drawer_Select(){
        return list_Menu;
    }

    private void create_University(){
        Intent intent = new Intent(this, CreateUniversity.class);
        startActivity(intent);

    }

    public void set_List(List<Map<String, String>> lst_cur, int lst_type){
        int layout = android.R.layout.simple_list_item_2;
        if(lst_type>0) {
            adapter = new SimpleAdapter(this, lst_cur,
                    layout,
                    new String[]{"H1", "H2"},
                    new int[]{android.R.id.text1, android.R.id.text2});
            lst_Main.setAdapter(adapter);
        }
        else{
            adapter = new SimpleAdapter(this, lst_cur,
                    layout,
                    new String[]{"H1"},
                    new int[]{android.R.id.text1});
            lst_Main.setAdapter(adapter);
        }
    }

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
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
