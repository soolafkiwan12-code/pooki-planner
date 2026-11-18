package com.example.pookiplanner.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.pookiplanner.R;
import com.example.pookiplanner.database.DatabaseHelper;
import com.example.pookiplanner.models.StudyTask;
import com.example.pookiplanner.utils.ValidationUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends BaseActivity {

    private String selectedDate = "";
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        db = new DatabaseHelper(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        TextInputEditText titleInput = findViewById(R.id.inputTaskTitle);
        TextInputEditText descInput = findViewById(R.id.inputTaskDescription);
        TextInputEditText dateInput = findViewById(R.id.inputTaskDate);
        MaterialButton saveBtn = findViewById(R.id.btnSaveTask);

        dateInput.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) -> {
                cal.set(y, m, d);
                selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());
                dateInput.setText(new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(cal.getTime()));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        saveBtn.setOnClickListener(v -> {
            String title = text(titleInput);
            String err = ValidationUtils.validateTask(title, selectedDate);
            if (err != null) { showErrorToast(err); return; }
            StudyTask task = new StudyTask(title, text(descInput), selectedDate, prefs().getUserId());
            db.addTask(task);
            Toast.makeText(this, R.string.task_saved, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private String text(TextInputEditText e) {
        return e.getText() != null ? e.getText().toString().trim() : "";
    }
}
