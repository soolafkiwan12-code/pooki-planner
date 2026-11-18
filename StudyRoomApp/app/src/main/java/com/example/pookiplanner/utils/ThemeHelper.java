package com.example.pookiplanner.utils;

import androidx.appcompat.app.AppCompatDelegate;

public final class ThemeHelper {

    private ThemeHelper() {}

    public static void apply(boolean dark) {
        AppCompatDelegate.setDefaultNightMode(
                dark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }
}
