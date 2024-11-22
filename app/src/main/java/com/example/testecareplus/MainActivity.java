package com.example.testecareplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                }

                if(item.getItemId() == R.id.nav_artigos) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ArtigosFragment()).commit();
                }

                if(item.getItemId() == R.id.nav_pront) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProntuarioFragment()).commit();
                }

                if(item.getItemId() == R.id.nav_agenda) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AnotacoesFragment()).commit();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        if (savedInstanceState == null) {
//            getSupportActionBar().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
//
//            navigationView.setCheckedItem(R.id.nav_home);
//        }
    }
    @Override
    public void onBackPressed() {
         if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
             drawerLayout.closeDrawer(GravityCompat.START);
         }else {
             super.onBackPressed();
         }
    }
}