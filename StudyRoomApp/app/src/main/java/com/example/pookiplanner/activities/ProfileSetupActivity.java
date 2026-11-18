package com.example.pookiplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.pookiplanner.R;
import com.example.pookiplanner.database.DatabaseHelper;
import com.example.pookiplanner.models.User;
import com.example.pookiplanner.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileSetupActivity extends BaseActivity {

    public static final String EXTRA_USER_ID = "user_id";

    private long userId;
    private DatabaseHelper db;
    private TextInputLayout universityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        userId = getIntent().getLongExtra(EXTRA_USER_ID, prefs().getUserId());
        db = new DatabaseHelper(this);

        TextInputEditText nameInput = findViewById(R.id.inputName);
        TextInputEditText ageInput = findViewById(R.id.inputAge);
        universityLayout = findViewById(R.id.layoutUniversity);
        TextInputEditText universityInput = findViewById(R.id.inputUniversity);
        RadioGroup educationGroup = findViewById(R.id.groupEducation);
        RadioGroup governorateGroup = findViewById(R.id.groupGovernorate);
        RadioGroup preferenceGroup = findViewById(R.id.groupPreference);
        MaterialButton saveBtn = findViewById(R.id.btnSaveProfile);

        educationGroup.setOnCheckedChangeListener((g, id) -> {
            boolean uni = id == R.id.radioUniversity;
            universityLayout.setVisibility(uni ? View.VISIBLE : View.GONE);
        });

        User existing = db.getUserById(userId);
        if (existing != null && existing.getName() != null) {
            nameInput.setText(existing.getName());
            if (existing.getAge() > 0) ageInput.setText(String.valueOf(existing.getAge()));
        }

        saveBtn.setOnClickListener(v -> {
            String name = text(nameInput);
            String ageStr = text(ageInput);
            String education = educationGroup.getCheckedRadioButtonId() == R.id.radioUniversity
                    ? User.EDUCATION_UNIVERSITY : User.EDUCATION_SCHOOL;
            String university = text(universityInput);
            String governorate = governorateGroup.getCheckedRadioButtonId() == R.id.radioAmman
                    ? User.GOV_AMMAN : User.GOV_IRBID;
            String preference = preferenceGroup.getCheckedRadioButtonId() == R.id.radioBookRoom
                    ? User.PREF_BOOK_ROOM : User.PREF_STUDY_OUTSIDE;

            String err = ValidationUtils.validateProfile(name, ageStr, education, university, governorate, preference);
            if (err != null) { showErrorToast(err); return; }

            User user = db.getUserById(userId);
            if (user == null) return;
            user.setName(name.trim());
            user.setAge(Integer.parseInt(ageStr.trim()));
            user.setEducationType(education);
            user.setUniversityName(User.EDUCATION_UNIVERSITY.equals(education) ? university.trim() : "");
            user.setGovernorate(governorate);
            user.setStudyPreference(preference);
            user.setProfileComplete(true);
            db.updateProfile(user);
            prefs().setLoggedIn(userId);
            Toast.makeText(this, R.string.profile_saved, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private String text(TextInputEditText e) {
        return e.getText() != null ? e.getText().toString() : "";
    }
}
