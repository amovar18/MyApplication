package com.example.amogha.newsupdates;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    protected DrawerLayout drawer;
    protected Toolbar toolbar;
    public ActionBarDrawerToggle toggle;
    public NavigationView nav;
    Fragment fragment=null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar)findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.Open, R.string.Close);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        nav = (NavigationView) findViewById(R.id.nv);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new getnews()).commit();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.world_news:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new getnews()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        Toast.makeText(getApplicationContext(), "Covid-19 World News", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.world_tracker:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new cov_world()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        Toast.makeText(getApplicationContext(), "Covid-19 World Count", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.india_tracker:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new cov_india()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        Toast.makeText(getApplicationContext(), "Covid-19 India Count", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.helpline:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new helpline()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        Toast.makeText(getApplicationContext(), "Covid-19 Helpline", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.faq:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new faq()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        Toast.makeText(getApplicationContext(), "FAQ's", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}