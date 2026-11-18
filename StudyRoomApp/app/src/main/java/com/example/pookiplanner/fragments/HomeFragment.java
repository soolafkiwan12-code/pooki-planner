package com.example.pookiplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pookiplanner.PookiPlannerApplication;
import com.example.pookiplanner.R;
import com.example.pookiplanner.database.DataProvider;
import com.example.pookiplanner.database.DatabaseHelper;
import com.example.pookiplanner.models.Booking;
import com.example.pookiplanner.models.StudyTask;
import com.example.pookiplanner.models.User;
import com.example.pookiplanner.utils.DateUtils;
import com.example.pookiplanner.utils.PrefManager;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        View v = getView();
        if (v == null) return;

        PrefManager prefs = ((PookiPlannerApplication) requireActivity().getApplication()).getPrefManager();
        DatabaseHelper db = new DatabaseHelper(requireContext());
        User user = db.getUserById(prefs.getUserId());
        if (user == null) return;

        boolean ar = "ar".equals(prefs.getLanguage());
        ((TextView) v.findViewById(R.id.textWelcome)).setText(
                getString(R.string.welcome_message, user.getName() != null ? user.getName() : ""));
        ((TextView) v.findViewById(R.id.textStreak)).setText(
                getString(R.string.study_streak, prefs.getStudyStreak()));
        ((TextView) v.findViewById(R.id.textGovernorate)).setText(
                getString(R.string.your_governorate,
                        DataProvider.getGovernorateDisplayName(user.getGovernorate(), ar)));

        Booking booking = db.getUpcomingBooking(user.getId());
        TextView bookingText = v.findViewById(R.id.textUpcomingBooking);
        if (booking != null) {
            bookingText.setText(getString(R.string.upcoming_booking_detail,
                    booking.getRoomName(), booking.getDate(), booking.getTime()));
        } else {
            bookingText.setText(R.string.no_upcoming_booking);
        }

        StringBuilder tasks = new StringBuilder();
        for (StudyTask t : db.getTasksForDate(user.getId(), DateUtils.todayStorage())) {
            tasks.append("• ").append(t.getTitle()).append("\n");
        }
        ((TextView) v.findViewById(R.id.textTodayTasks)).setText(
                tasks.length() > 0 ? tasks.toString().trim() : getString(R.string.no_tasks_today));
    }
}
