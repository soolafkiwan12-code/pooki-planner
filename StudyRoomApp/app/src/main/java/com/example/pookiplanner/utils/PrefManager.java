package com.example.pookiplanner.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PrefManager {

    private static final String PREF = "pooki_prefs";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_DARK = "dark_mode";
    private static final String KEY_LANG = "language";
    private static final String KEY_STREAK = "streak";
    private static final String KEY_LAST_DATE = "last_date";
    private static final String DATE_FMT = "yyyy-MM-dd";

    private final SharedPreferences prefs;

    public PrefManager(Context context) {
        prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() { return prefs.getBoolean(KEY_LOGGED_IN, false); }

    public void setLoggedIn(long userId) {
        prefs.edit().putBoolean(KEY_LOGGED_IN, true).putLong(KEY_USER_ID, userId).apply();
    }

    public void logout() {
        prefs.edit().putBoolean(KEY_LOGGED_IN, false).remove(KEY_USER_ID).apply();
    }

    public long getUserId() { return prefs.getLong(KEY_USER_ID, -1); }

    public boolean isDarkMode() { return prefs.getBoolean(KEY_DARK, false); }
    public void setDarkMode(boolean dark) { prefs.edit().putBoolean(KEY_DARK, dark).apply(); }

    public String getLanguage() { return prefs.getString(KEY_LANG, "en"); }
    public void setLanguage(String lang) { prefs.edit().putString(KEY_LANG, lang).apply(); }

    public int getStudyStreak() { return prefs.getInt(KEY_STREAK, 0); }

    public void updateStudyStreak() {
        String today = new SimpleDateFormat(DATE_FMT, Locale.getDefault()).format(new Date());
        String last = prefs.getString(KEY_LAST_DATE, "");
        int streak = prefs.getInt(KEY_STREAK, 0);
        if (today.equals(last)) return;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        String yesterday = new SimpleDateFormat(DATE_FMT, Locale.getDefault()).format(cal.getTime());
        if (last.equals(yesterday)) streak++;
        else streak = 1;
        prefs.edit().putInt(KEY_STREAK, streak).putString(KEY_LAST_DATE, today).apply();
    }
}
