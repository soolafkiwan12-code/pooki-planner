package com.example.pookiplanner.utils;

import android.util.Patterns;

import com.example.pookiplanner.models.User;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) return "err_email";
        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) return "err_email";
        return null;
    }

    public static String validatePassword(String password) {
        if (password == null || password.length() < 6) return "err_password";
        return null;
    }

    public static String validateSignUp(String email, String password, String confirm) {
        String e = validateEmail(email);
        if (e != null) return e;
        String p = validatePassword(password);
        if (p != null) return p;
        if (!password.equals(confirm)) return "err_password_match";
        return null;
    }

    public static String validateProfile(String name, String ageStr, String education,
                                         String university, String governorate, String preference) {
        if (name == null || name.trim().length() < 2) return "err_name";
        try {
            int age = Integer.parseInt(ageStr.trim());
            if (age < 10 || age > 80) return "err_age";
        } catch (NumberFormatException ex) {
            return "err_age";
        }
        if (User.EDUCATION_UNIVERSITY.equals(education) &&
                (university == null || university.trim().isEmpty())) return "err_university";
        if (!User.GOV_IRBID.equals(governorate) && !User.GOV_AMMAN.equals(governorate))
            return "err_governorate";
        if (preference == null || preference.isEmpty()) return "err_governorate";
        return null;
    }

    public static String validateTask(String title, String dueDate) {
        if (title == null || title.trim().isEmpty()) return "hint_task_title";
        if (dueDate == null || dueDate.isEmpty()) return "hint_due_date";
        return null;
    }

    public static String validateBooking(String roomId, String date, String time) {
        if (roomId == null || roomId.isEmpty()) return "select_room";
        if (date == null || date.isEmpty()) return "hint_booking_date";
        if (time == null || time.isEmpty()) return "hint_booking_time";
        return null;
    }
}
