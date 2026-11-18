package com.example.pookiplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.pookiplanner.R;
import com.example.pookiplanner.database.DatabaseHelper;
import com.example.pookiplanner.models.User;
import com.example.pookiplanner.utils.SocialLoginHelper;
import com.example.pookiplanner.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends BaseActivity {

    private DatabaseHelper db;
    private SocialLoginHelper socialLoginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DatabaseHelper(this);

        TextInputEditText emailInput = findViewById(R.id.inputEmail);
        TextInputEditText passInput = findViewById(R.id.inputPassword);
        MaterialButton loginBtn = findViewById(R.id.btnLogin);
        TextView signUpLink = findViewById(R.id.linkSignUp);

        socialLoginHelper = new SocialLoginHelper(this, db, prefs(), this::goAfterAuth);
        socialLoginHelper.bindButtons(
                findViewById(R.id.btnGoogle),
                findViewById(R.id.btnApple),
                findViewById(R.id.btnGuest));

        loginBtn.setOnClickListener(v -> {
            String email = text(emailInput);
            String pass = text(passInput);
            String err = ValidationUtils.validateEmail(email);
            if (err != null) { showErrorToast(err); return; }
            err = ValidationUtils.validatePassword(pass);
            if (err != null) { showErrorToast(err); return; }

            User user = db.login(email.trim(), pass);
            if (user == null) {
                showErrorToast("err_login_failed");
                return;
            }
            prefs().setLoggedIn(user.getId());
            prefs().updateStudyStreak();
            goAfterAuth(user);
        });

        signUpLink.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));
    }

    private void goAfterAuth(User user) {
        if (user.isProfileComplete()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Intent i = new Intent(this, ProfileSetupActivity.class);
            i.putExtra(ProfileSetupActivity.EXTRA_USER_ID, user.getId());
            startActivity(i);
        }
        finish();
    }

    private String text(TextInputEditText e) {
        return e.getText() != null ? e.getText().toString() : "";
    }
}
