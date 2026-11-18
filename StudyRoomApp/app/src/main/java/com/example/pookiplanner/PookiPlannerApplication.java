package com.example.pookiplanner;

import android.app.Application;

import com.example.pookiplanner.utils.PrefManager;
import com.example.pookiplanner.utils.ThemeHelper;

public class PookiPlannerApplication extends Application {

    private PrefManager prefManager;

    @Override
    public void onCreate() {
        super.onCreate();
        prefManager = new PrefManager(this);
        ThemeHelper.apply(prefManager.isDarkMode());
    }

    public PrefManager getPrefManager() {
        return prefManager;
    }
}
