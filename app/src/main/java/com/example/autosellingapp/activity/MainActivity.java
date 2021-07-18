package com.example.autosellingapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.autosellingapp.R;
import com.example.autosellingapp.fragments.FragmentHome;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    public NavigationView navigationView;
    private FragmentTransaction fragmentTransaction;
    private BottomNavigationView bottomNavigationView;

    public static final int FRAGMENT_HOME = 1;
    public static final int FRAGMENT_SEARCH = 2;
    public static final int FRAGMENT_SELLING = 5;
    public static final int FRAGMENT_PROFILE = 6;
    public static final int FRAGMENT_FAVOURITE = 7;
    public static final int FRAGMENT_LOGOUT = 8;
    public static final int FRAGMENT_CONTACT = 9;
    public static final int FRAGMENT_SETTING = 10;
    public static final int FRAGMENT_TERM = 11;
    public static final int FRAGMENT_RATE = 12;
    public static final int FRAGMENT_ANOTHER = 99;

    public int currentFragment = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Hook();
        NavigationDrawerMenu();
        openFragmentHome();
    }

    private void Hook(){
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.bottom_nav_home:
                        if(currentFragment != FRAGMENT_HOME){
                            Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();
                            currentFragment = FRAGMENT_HOME;
                        }
                        break;
                    case R.id.bottom_nav_search:
                        if(currentFragment != FRAGMENT_SEARCH){
                            Toast.makeText(MainActivity.this, "search", Toast.LENGTH_SHORT).show();
                            currentFragment = FRAGMENT_SEARCH;
                        }
                        break;
                    case R.id.bottom_nav_favourite:
                        if(currentFragment != FRAGMENT_FAVOURITE){
                            Toast.makeText(MainActivity.this, "favourite", Toast.LENGTH_SHORT).show();
                            currentFragment = FRAGMENT_FAVOURITE;
                        }
                        break;
                    case R.id.bottom_nav_selling:
                        if(currentFragment != FRAGMENT_SELLING){
                            Toast.makeText(MainActivity.this, "selling", Toast.LENGTH_SHORT).show();
                            currentFragment = FRAGMENT_SELLING;
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void NavigationDrawerMenu(){
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            if(getSupportFragmentManager().getBackStackEntryCount() != 0){
                super.onBackPressed();
            }else{
                openQuitDialog();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_home:
                if(FRAGMENT_HOME != currentFragment){
                    ReplaceFragment(new FragmentHome(), getString(R.string.Home));
                    currentFragment = FRAGMENT_HOME;
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_search:
                if(FRAGMENT_SEARCH != currentFragment){
                    //ReplaceFragment(new SearchFragment(), "SEARCH");
                    currentFragment = FRAGMENT_SEARCH;
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_selling:
                if(FRAGMENT_SELLING != currentFragment){
                    //ReplaceFragment(new InfoFragment(), "INFO");
                    currentFragment = FRAGMENT_SELLING;
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_profile:
                if(FRAGMENT_PROFILE != currentFragment){
                    //ReplaceFragment(new InfoFragment(), "INFO");
                    currentFragment = FRAGMENT_PROFILE;
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_favourite:
                if(FRAGMENT_FAVOURITE != currentFragment){
                    //ReplaceFragment(new InfoFragment(), "INFO");
                    currentFragment = FRAGMENT_FAVOURITE;
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_logout:
                break;
            case R.id.nav_contact:
                if(FRAGMENT_CONTACT != currentFragment){
                    //ReplaceFragment(new InfoFragment(), "INFO");
                    currentFragment = FRAGMENT_CONTACT;
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_setting:
                if(FRAGMENT_SETTING != currentFragment){
                    //ReplaceFragment(new InfoFragment(), "INFO");
                    currentFragment = FRAGMENT_SETTING;
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_terms:
                if(FRAGMENT_TERM != currentFragment){
                    //ReplaceFragment(new InfoFragment(), "INFO");
                    currentFragment = FRAGMENT_TERM;
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_rate:
                if(FRAGMENT_RATE != currentFragment){
                    //ReplaceFragment(new InfoFragment(), "INFO");
                    currentFragment = FRAGMENT_RATE;
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragmentHome(){
        ReplaceFragment(new FragmentHome(), getString(R.string.Home));
    }

    public void ReplaceFragment(Fragment fragment, String name){

        if(getSupportFragmentManager().getBackStackEntryCount() != 0){
            getSupportFragmentManager().popBackStack();
        }
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_content, fragment, name);
        fragmentTransaction.commit();
    }

    public void openQuitDialog() {
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this);
        alert.setTitle("Message");
        alert.setMessage("Log out?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }
}