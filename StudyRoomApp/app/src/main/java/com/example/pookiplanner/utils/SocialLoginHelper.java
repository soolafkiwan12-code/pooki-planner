package com.example.pookiplanner.utils;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pookiplanner.R;
import com.example.pookiplanner.database.DatabaseHelper;
import com.example.pookiplanner.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public final class SocialLoginHelper {

    public interface Callback {
        void onAuthenticated(User user);
    }

    private final AppCompatActivity activity;
    private final DatabaseHelper db;
    private final PrefManager prefs;
    private final Callback callback;
    private GoogleSignInClient googleClient;
    private ActivityResultLauncher<Intent> googleLauncher;

    public SocialLoginHelper(AppCompatActivity activity, DatabaseHelper db,
                             PrefManager prefs, Callback callback) {
        this.activity = activity;
        this.db = db;
        this.prefs = prefs;
        this.callback = callback;
        initGoogle();
    }

    private void initGoogle() {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        googleClient = GoogleSignIn.getClient(activity, options);
        googleLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getData() == null) return;
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        if (account.getEmail() != null) {
                            finishSocial(account.getEmail(), account.getDisplayName(), "google",
                                    R.string.signed_in_google);
                        }
                    } catch (ApiException e) {
                        Toast.makeText(activity, R.string.google_sign_in_failed, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void bindButtons(MaterialButton btnGoogle, MaterialButton btnApple, MaterialButton btnGuest) {
        btnGoogle.setOnClickListener(v -> googleLauncher.launch(googleClient.getSignInIntent()));
        btnApple.setOnClickListener(v -> showAppleDialog());
        if (btnGuest != null) {
            btnGuest.setOnClickListener(v ->
                    finishSocial("guest@pooki.app", "Guest Student", "guest", R.string.signed_in_guest));
        }
    }

    private void showAppleDialog() {
        TextInputEditText input = new TextInputEditText(activity);
        input.setHint(R.string.hint_email);
        input.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setPadding(48, 32, 48, 32);

        new AlertDialog.Builder(activity)
                .setTitle(R.string.apple_sign_in_title)
                .setMessage(R.string.apple_sign_in_message)
                .setView(input)
                .setPositiveButton(R.string.btn_login, (d, w) -> {
                    String email = input.getText() != null ? input.getText().toString().trim() : "";
                    if (email.isEmpty() || !email.contains("@")) {
                        Toast.makeText(activity, R.string.err_email, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String name = email.contains("@") ? email.substring(0, email.indexOf('@')) : "Apple User";
                    finishSocial(email, name, "apple", R.string.signed_in_apple);
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void finishSocial(String email, String name, String provider, int toastRes) {
        User user = db.loginOrRegisterSocial(email, name, provider);
        if (user == null) return;
        prefs.setLoggedIn(user.getId());
        prefs.updateStudyStreak();
        Toast.makeText(activity, toastRes, Toast.LENGTH_SHORT).show();
        callback.onAuthenticated(user);
    }
}
