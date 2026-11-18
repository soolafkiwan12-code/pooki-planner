package com.example.pookiplanner.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public final class LocaleHelper {

    private LocaleHelper() {}

    public static Context applyLocale(Context context, String languageCode) {
        Locale locale = "ar".equals(languageCode) ? new Locale("ar") : Locale.ENGLISH;
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        return context.createConfigurationContext(config);
    }

    public static void updateActivityLocale(Context context, String languageCode) {
        Locale locale = "ar".equals(languageCode) ? new Locale("ar") : Locale.ENGLISH;
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = res.getConfiguration();
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}
