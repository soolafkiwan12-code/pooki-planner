package com.example.pookiplanner.activities;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pookiplanner.PookiPlannerApplication;
import com.example.pookiplanner.utils.LocaleHelper;
import com.example.pookiplanner.utils.PrefManager;
import com.example.pookiplanner.utils.ThemeHelper;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        PrefManager pm = new PrefManager(newBase);
        super.attachBaseContext(LocaleHelper.applyLocale(newBase, pm.getLanguage()));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        PookiPlannerApplication app = (PookiPlannerApplication) getApplication();
        ThemeHelper.apply(app.getPrefManager().isDarkMode());
        LocaleHelper.updateActivityLocale(this, app.getPrefManager().getLanguage());
        super.onCreate(savedInstanceState);
    }

    protected PrefManager prefs() {
        return ((PookiPlannerApplication) getApplication()).getPrefManager();
    }

    protected void showErrorToast(String stringResName) {
        int id = getResources().getIdentifier(stringResName, "string", getPackageName());
        if (id != 0) android.widget.Toast.makeText(this, id, android.widget.Toast.LENGTH_SHORT).show();
    }
}
