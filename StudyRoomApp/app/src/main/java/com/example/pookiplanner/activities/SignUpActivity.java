package com.example.pookiplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pookiplanner.R;
import com.example.pookiplanner.database.DatabaseHelper;
import com.example.pookiplanner.models.User;
import com.example.pookiplanner.utils.SocialLoginHelper;
import com.example.pookiplanner.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextInputEditText emailInput = findViewById(R.id.inputEmail);
        TextInputEditText passInput = findViewById(R.id.inputPassword);
        TextInputEditText confirmInput = findViewById(R.id.inputConfirmPassword);
        MaterialButton signUpBtn = findViewById(R.id.btnSignUp);
        TextView loginLink = findViewById(R.id.linkLogin);

        signUpBtn.setOnClickListener(v -> {
            String email = text(emailInput).trim();
            String pass = text(passInput);
            String confirm = text(confirmInput);
            String err = ValidationUtils.validateSignUp(email, pass, confirm);
            if (err != null) { showErrorToast(err); return; }

            DatabaseHelper db = new DatabaseHelper(this);
            if (db.emailExists(email)) {
                showErrorToast("err_email_exists");
                return;
            }
            long userId = db.registerUser(email, pass);
            prefs().setLoggedIn(userId);
            Toast.makeText(this, R.string.account_created, Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, ProfileSetupActivity.class);
            i.putExtra(ProfileSetupActivity.EXTRA_USER_ID, userId);
            startActivity(i);
            finish();
        });

        loginLink.setOnClickListener(v -> finish());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        DatabaseHelper db = new DatabaseHelper(this);
        SocialLoginHelper social = new SocialLoginHelper(this, db, prefs(), user -> {
            if (user.isProfileComplete()) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                Intent i = new Intent(this, ProfileSetupActivity.class);
                i.putExtra(ProfileSetupActivity.EXTRA_USER_ID, user.getId());
                startActivity(i);
            }
            finish();
        });
        social.bindButtons(
                findViewById(R.id.btnGoogle),
                findViewById(R.id.btnApple),
                findViewById(R.id.btnGuest));
    }

    private String text(TextInputEditText e) {
        return e.getText() != null ? e.getText().toString() : "";
    }
}
