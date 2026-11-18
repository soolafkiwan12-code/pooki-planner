package com.example.pookiplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.example.pookiplanner.R;
import com.example.pookiplanner.fragments.HomeFragment;
import com.example.pookiplanner.fragments.ProfileFragment;
import com.example.pookiplanner.fragments.RoomingFragment;
import com.example.pookiplanner.fragments.TasksFragment;
import com.example.pookiplanner.fragments.TimerFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNav = findViewById(R.id.bottomNavigation);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment f;
            String title;
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                f = new HomeFragment(); title = getString(R.string.nav_home);
            } else if (id == R.id.nav_tasks) {
                f = new TasksFragment(); title = getString(R.string.nav_tasks);
            } else if (id == R.id.nav_timer) {
                f = new TimerFragment(); title = getString(R.string.nav_timer);
            } else if (id == R.id.nav_rooming) {
                f = new RoomingFragment(); title = getString(R.string.nav_rooming);
            } else {
                f = new ProfileFragment(); title = getString(R.string.nav_profile);
            }
            if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, f).commit();
            return true;
        });

        if (savedInstanceState == null) bottomNav.setSelectedItemId(R.id.nav_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_task) {
            startActivity(new Intent(this, AddTaskActivity.class));
            return true;
        }
        if (id == R.id.action_add_booking) {
            startActivity(new Intent(this, AddBookingActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
