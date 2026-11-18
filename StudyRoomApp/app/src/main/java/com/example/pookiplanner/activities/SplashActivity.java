package com.example.pookiplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.pookiplanner.R;
import com.example.pookiplanner.database.DatabaseHelper;
import com.example.pookiplanner.models.User;

public class SplashActivity extends BaseActivity {

    private static final long DELAY = 2200L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!prefs().isLoggedIn()) {
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                User user = new DatabaseHelper(this).getUserById(prefs().getUserId());
                if (user != null && user.isProfileComplete()) {
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    Intent i = new Intent(this, ProfileSetupActivity.class);
                    if (user != null) i.putExtra(ProfileSetupActivity.EXTRA_USER_ID, user.getId());
                    startActivity(i);
                }
            }
            finish();
        }, DELAY);
    }
}
