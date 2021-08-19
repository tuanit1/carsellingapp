package com.example.autosellingapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.autosellingapp.R;
import com.example.autosellingapp.fragments.FragmentAdsDetail;
import com.example.autosellingapp.fragments.FragmentFavourite;
import com.example.autosellingapp.fragments.FragmentHome;
import com.example.autosellingapp.fragments.FragmentMessage;
import com.example.autosellingapp.fragments.FragmentProfile;
import com.example.autosellingapp.fragments.FragmentSearch;
import com.example.autosellingapp.fragments.FragmentSelling;
import com.example.autosellingapp.fragments.FragmentSetting;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;
import com.example.autosellingapp.utils.SharedPref;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    public Toolbar toolbar;
    private DrawerLayout drawerLayout;
    public NavigationView navigationView;
    private FragmentTransaction fragmentTransaction;
    private BottomNavigationView bottomNavigationView;
    private Methods methods;
    private SharedPref sharedPref;
    private boolean mToolbarNavigationListenerIsRegistered = false;
    private ActionBarDrawerToggle toggle;

    public static final int FRAGMENT_HOME = 1;
    public static final int FRAGMENT_SEARCH = 2;
    public static final int FRAGMENT_SELLING = 5;
    public static final int FRAGMENT_PROFILE = 6;
    public static final int FRAGMENT_FAVOURITE = 7;
    public static final int FRAGMENT_MESSAGE = 8;
    public static final int FRAGMENT_CONTACT = 9;
    public static final int FRAGMENT_SETTING = 10;
    public static final int FRAGMENT_TERM = 11;
    public static final int FRAGMENT_RATE = 12;
    public static final int FRAGMENT_ANOTHER = 99;

    public int currentFragment = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Constant.verifyStoragePermissions(this);

        setContentView(R.layout.activity_main);

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        methods = new Methods(this);
        sharedPref = new SharedPref(this);

        Hook();
        NavigationDrawerMenu();
        displayHomeUpOrHamburger();
        openFragmentHome();
    }

    private void Hook(){
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                            ReplaceFragment(new FragmentHome(), getString(R.string.Home));
                            currentFragment = FRAGMENT_HOME;
                            navigationView.setCheckedItem(R.id.nav_home);
                        }
                        break;
                    case R.id.bottom_nav_search:
                        if(currentFragment != FRAGMENT_SEARCH){
                            ReplaceFragment(new FragmentSearch(), getString(R.string.search));
                            currentFragment = FRAGMENT_SEARCH;
                            navigationView.setCheckedItem(R.id.nav_search);
                        }
                        break;
                    case R.id.bottom_nav_favourite:
                        if(currentFragment != FRAGMENT_FAVOURITE){
                            ReplaceFragment(new FragmentFavourite(), getString(R.string.frag_favourite));
                            currentFragment = FRAGMENT_FAVOURITE;
                            navigationView.setCheckedItem(R.id.nav_favourite);
                        }
                        break;
                    case R.id.bottom_nav_message:
                        if(currentFragment != FRAGMENT_MESSAGE){
                            ReplaceFragment(new FragmentMessage(), getString(R.string.frag_mesage));
                            currentFragment = FRAGMENT_MESSAGE;
                            navigationView.setCheckedItem(R.id.nav_message);
                        }
                        break;
                    case R.id.bottom_nav_selling:
                        if(currentFragment != FRAGMENT_SELLING){
                            ReplaceFragment(new FragmentSelling(), getString(R.string.selling));
                            currentFragment = FRAGMENT_SELLING;
                            navigationView.setCheckedItem(R.id.nav_selling);
                        }
                        break;
                }
                return true;
            }
        });
    }
    private void NavigationDrawerMenu(){
        navigationView.bringToFront();
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        if(methods.isLogged()){
            navigationView.getMenu().getItem(1).getSubMenu().getItem(2).setTitle("Logout");
        }else{
            navigationView.getMenu().getItem(1).getSubMenu().getItem(2).setTitle("Login");
        }

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            if(getSupportFragmentManager().getBackStackEntryCount() != 0){
                getSupportActionBar().setTitle(getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getBackStackEntryCount() - 1).getTag());
                super.onBackPressed();
            }else{
                if(methods.isLogged()){
                    openQuitDialog();
                }else{
                    super.onBackPressed();
                }
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
                    bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);
                }
                break;
            case R.id.nav_search:
                if(FRAGMENT_SEARCH != currentFragment){
                    ReplaceFragment(new FragmentSearch(), getString(R.string.search));
                    currentFragment = FRAGMENT_SEARCH;
                    drawerLayout.closeDrawer(GravityCompat.START);
                    bottomNavigationView.getMenu().findItem(R.id.bottom_nav_search).setChecked(true);
                }
                break;
            case R.id.nav_message:
                if(FRAGMENT_MESSAGE != currentFragment){
                    ReplaceFragment(new FragmentMessage(), getString(R.string.frag_mesage));
                    currentFragment = FRAGMENT_MESSAGE;
                    drawerLayout.closeDrawer(GravityCompat.START);
                    bottomNavigationView.getMenu().findItem(R.id.bottom_nav_message).setChecked(true);
                }
                break;
            case R.id.nav_selling:
                if(FRAGMENT_SELLING != currentFragment){
                    ReplaceFragment(new FragmentSelling(), getString(R.string.selling));
                    currentFragment = FRAGMENT_SELLING;
                    drawerLayout.closeDrawer(GravityCompat.START);
                    bottomNavigationView.getMenu().findItem(R.id.bottom_nav_selling).setChecked(true);
                }
                break;
            case R.id.nav_profile:
                if(FRAGMENT_PROFILE != currentFragment){
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.PROFILE_MODE, Constant.MY_PROFILE);
                    FragmentProfile f = new FragmentProfile();
                    f.setArguments(bundle);
                    ReplaceFragment(f, getString(R.string.frag_profile));
                    currentFragment = FRAGMENT_PROFILE;
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_favourite:
                if(FRAGMENT_FAVOURITE != currentFragment){
                    ReplaceFragment(new FragmentFavourite(), getString(R.string.frag_favourite));
                    currentFragment = FRAGMENT_FAVOURITE;
                    drawerLayout.closeDrawer(GravityCompat.START);
                    bottomNavigationView.getMenu().findItem(R.id.bottom_nav_favourite).setChecked(true);
                }
                break;
            case R.id.nav_logout:
                if(methods.isLogged()){
                    openLogOutDialog();
                }else{
                    super.onBackPressed();
                }
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
                    ReplaceFragment(new FragmentSetting(), getString(R.string.frag_setting));
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
        navigationView.setCheckedItem(R.id.nav_home);
        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);
    }

    private void ReplaceFragment(Fragment fragment, String name){

        int backstackCount = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backstackCount; i++){
            int backStackId = getSupportFragmentManager().getBackStackEntryAt(i).getId();

            getSupportFragmentManager().popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_content, fragment, name);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(name);
    }

    private void openQuitDialog() {
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this);
        alert.setTitle("Message");
        alert.setMessage("Exit application?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finishAffinity();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }
    private void openLogOutDialog() {
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this);
        alert.setTitle("Message");
        alert.setMessage("Log out?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                logOut();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }

    private void displayHomeUpOrHamburger(){
        boolean upBtn = getSupportFragmentManager().getBackStackEntryCount() > 0;

        if(upBtn){
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            if(!mToolbarNavigationListenerIsRegistered){
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                });
                mToolbarNavigationListenerIsRegistered = true;
            }
        }else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setToolbarNavigationClickListener(null);
            mToolbarNavigationListenerIsRegistered = false;
        }
    }

    private void status(String status){
        if(methods.isLogged()){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(Constant.UID);

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", status);

            reference.updateChildren(hashMap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("AAA", "resume");
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("AAA", "pause");
        status("offline");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("AAA", "stop");
    }

    @Override
    protected void onDestroy() {
        status("offline");
        Constant.isLogged = false;
        Log.e("AAA", "Log out");
        Constant.UID = "";
        FirebaseAuth.getInstance().signOut();
        Log.e("AAA", "destroy");
        super.onDestroy();
    }

    private void logOut(){
        sharedPref.setIsAutoLogin(false);
        startActivity(new Intent(MainActivity.this, ActivityLogin.class));
        finish();
    }

    @Override
    public void onBackStackChanged() {
        displayHomeUpOrHamburger();
    }
}