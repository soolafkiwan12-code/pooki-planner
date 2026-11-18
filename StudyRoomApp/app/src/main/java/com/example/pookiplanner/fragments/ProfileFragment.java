package com.example.pookiplanner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.pookiplanner.PookiPlannerApplication;
import com.example.pookiplanner.R;
import com.example.pookiplanner.activities.LoginActivity;
import com.example.pookiplanner.activities.ProfileSetupActivity;
import com.example.pookiplanner.database.DataProvider;
import com.example.pookiplanner.database.DatabaseHelper;
import com.example.pookiplanner.models.User;
import com.example.pookiplanner.utils.LocaleHelper;
import com.example.pookiplanner.utils.PrefManager;
import com.example.pookiplanner.utils.ThemeHelper;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PrefManager prefs = ((PookiPlannerApplication) requireActivity().getApplication()).getPrefManager();
        DatabaseHelper db = new DatabaseHelper(requireContext());
        User user = db.getUserById(prefs.getUserId());
        if (user == null) return;

        boolean ar = "ar".equals(prefs.getLanguage());
        ((TextView) view.findViewById(R.id.textProfileName)).setText(user.getName());
        ((TextView) view.findViewById(R.id.textProfileEmail)).setText(user.getEmail());
        ((TextView) view.findViewById(R.id.textProfileAge)).setText(String.valueOf(user.getAge()));
        String edu = User.EDUCATION_UNIVERSITY.equals(user.getEducationType())
                ? getString(R.string.university) + (user.getUniversityName() != null ? " - " + user.getUniversityName() : "")
                : getString(R.string.school);
        ((TextView) view.findViewById(R.id.textProfileEducation)).setText(
                getString(R.string.education_label, edu));
        ((TextView) view.findViewById(R.id.textProfileGov)).setText(
                DataProvider.getGovernorateDisplayName(user.getGovernorate(), ar));
        String pref = User.PREF_BOOK_ROOM.equals(user.getStudyPreference())
                ? getString(R.string.book_room) : getString(R.string.study_outside);
        ((TextView) view.findViewById(R.id.textProfilePreference)).setText(
                getString(R.string.preference_label, pref));
        ((TextView) view.findViewById(R.id.textTotalBookings)).setText(
                getString(R.string.total_bookings, db.getBookingCount(user.getId())));
        ((TextView) view.findViewById(R.id.textProfileStreak)).setText(
                getString(R.string.study_streak, prefs.getStudyStreak()));

        SwitchCompat dark = view.findViewById(R.id.switchDarkMode);
        dark.setChecked(prefs.isDarkMode());
        dark.setOnCheckedChangeListener((b, checked) -> {
            prefs.setDarkMode(checked);
            ThemeHelper.apply(checked);
            requireActivity().recreate();
        });

        RadioGroup langGroup = view.findViewById(R.id.groupLanguage);
        if ("ar".equals(prefs.getLanguage())) langGroup.check(R.id.radioArabic);
        else langGroup.check(R.id.radioEnglish);
        langGroup.setOnCheckedChangeListener((g, id) -> {
            String lang = id == R.id.radioArabic ? "ar" : "en";
            if (!lang.equals(prefs.getLanguage())) {
                prefs.setLanguage(lang);
                LocaleHelper.updateActivityLocale(requireContext(), lang);
                requireActivity().recreate();
            }
        });

        view.findViewById(R.id.btnEditProfile).setOnClickListener(v -> {
            Intent i = new Intent(requireContext(), ProfileSetupActivity.class);
            i.putExtra(ProfileSetupActivity.EXTRA_USER_ID, user.getId());
            startActivity(i);
        });

        MaterialButton logout = view.findViewById(R.id.btnLogout);
        logout.setOnClickListener(v -> new AlertDialog.Builder(requireContext())
                .setTitle(R.string.logout_title)
                .setMessage(R.string.logout_message)
                .setPositiveButton(R.string.yes, (d, w) -> {
                    prefs.logout();
                    startActivity(new Intent(requireContext(), LoginActivity.class));
                    requireActivity().finish();
                })
                .setNegativeButton(R.string.no, null)
                .show());
    }

    @Override
    public void onResume() {
        super.onResume();
        View v = getView();
        if (v == null) return;
        DatabaseHelper db = new DatabaseHelper(requireContext());
        PrefManager prefs = ((PookiPlannerApplication) requireActivity().getApplication()).getPrefManager();
        User user = db.getUserById(prefs.getUserId());
        if (user != null) {
            ((TextView) v.findViewById(R.id.textTotalBookings)).setText(
                    getString(R.string.total_bookings, db.getBookingCount(user.getId())));
        }
    }
}
