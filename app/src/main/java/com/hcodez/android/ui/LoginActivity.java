package com.hcodez.android.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hcodez.android.R;

public class LoginActivity extends MainMenuActivity {

    private static final String TAG = "LoginActivity";

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button   mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        skip();
        setContentView(R.layout.activity_login);

        mUsernameEditText = findViewById(R.id.loginName);
        mPasswordEditText = findViewById(R.id.loginPass);
        mLoginButton      = findViewById(R.id.loginButton);

        mLoginButton.setOnClickListener(
                view -> login(mUsernameEditText.getText().toString(),
                        mPasswordEditText.getText().toString()));
    }

    /**
     * Perform a login
     * @param userName the user name
     * @param userPassword the user password(ignored)
     */
    private void login(String userName, String userPassword) {
        Log.d(TAG, "login() called with: userName = [" + userName + "], userPassword = [" + userPassword + "]");

        if (userName == null) {
            Log.e(TAG, "login: null user name");
            return;
        }

        if (userName.equals("")) {
            Log.d(TAG, "login: empty user name");
            Toast.makeText(getApplicationContext(), "Empty username", Toast.LENGTH_LONG).show();
            return;
        }

        if (userName.length() < 4) {
            Log.d(TAG, "login: user name is too short");
            Toast.makeText(getApplicationContext(), "Username is too short(at least 4 characters long)", Toast.LENGTH_LONG).show();
            return;
        }

        if (userName.length() > 16) {
            Log.d(TAG, "login: user name is too long");
            Toast.makeText(getApplicationContext(), "Username is too long(at most 16 characters long)", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d(TAG, "login: saving username " + userName);
        getSharedPreferences("login_prefs", MODE_PRIVATE)
                .edit()
                .putString("owner", userName)
                .putBoolean("logged_in", true)
                .apply();

        startMainMenuActivity();
    }

    /**
     * Starts the main menu activity
     */
    private void startMainMenuActivity() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Checks whether it should skip over the login activity(if the user is already logged in
     */
    private void skip() {
        if (getPreferences(MODE_PRIVATE).getBoolean("logged_in", false)) {
            startMainMenuActivity();
        }
    }
}
