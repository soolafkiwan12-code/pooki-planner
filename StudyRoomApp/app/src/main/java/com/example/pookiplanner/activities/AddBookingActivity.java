package com.example.pookiplanner.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pookiplanner.R;
import com.example.pookiplanner.database.DataProvider;
import com.example.pookiplanner.database.DatabaseHelper;
import com.example.pookiplanner.models.Booking;
import com.example.pookiplanner.models.StudyRoom;
import com.example.pookiplanner.models.User;
import com.example.pookiplanner.utils.DateUtils;
import com.example.pookiplanner.utils.ValidationUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddBookingActivity extends BaseActivity {

    private String selectedDate = "";
    private String selectedTime = "";
    private DatabaseHelper db;
    private List<StudyRoom> rooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_booking);
        db = new DatabaseHelper(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        User user = db.getUserById(prefs().getUserId());
        String gov = user != null ? user.getGovernorate() : User.GOV_AMMAN;
        rooms = DataProvider.getStudyRooms(gov);

        Spinner spinner = findViewById(R.id.spinnerRoom);
        String[] names = new String[rooms.size()];
        for (int i = 0; i < rooms.size(); i++) names[i] = rooms.get(i).getName();
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names));

        TextInputEditText dateInput = findViewById(R.id.inputBookingDate);
        TextInputEditText timeInput = findViewById(R.id.inputBookingTime);
        MaterialButton saveBtn = findViewById(R.id.btnSaveBooking);

        dateInput.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog d = new DatePickerDialog(this, (view, y, m, day) -> {
                cal.set(y, m, day);
                selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());
                dateInput.setText(new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(cal.getTime()));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            d.getDatePicker().setMinDate(System.currentTimeMillis());
            d.show();
        });

        timeInput.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new TimePickerDialog(this, (view, h, min) -> {
                selectedTime = DateUtils.formatTime(h, min);
                timeInput.setText(selectedTime);
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();
        });

        saveBtn.setOnClickListener(v -> {
            if (rooms.isEmpty()) return;
            StudyRoom room = rooms.get(spinner.getSelectedItemPosition());
            String err = ValidationUtils.validateBooking(room.getId(), selectedDate, selectedTime);
            if (err != null) { showErrorToast(err); return; }
            if (db.isRoomBooked(room.getId(), selectedDate, selectedTime)) {
                showErrorToast("room_already_booked");
                return;
            }
            db.addBooking(new Booking(room.getId(), room.getName(), selectedDate, selectedTime, prefs().getUserId()));
            Toast.makeText(this, getString(R.string.booking_reminder, room.getName(), selectedDate, selectedTime),
                    Toast.LENGTH_LONG).show();
            finish();
        });
    }
}
