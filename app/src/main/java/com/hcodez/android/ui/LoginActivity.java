package com.hcodez.android.ui;

import android.content.Intent;
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
        setContentView(R.layout.activity_login);

        mUsernameEditText = findViewById(R.id.loginName);
        mPasswordEditText = findViewById(R.id.loginPass);
        mLoginButton      = findViewById(R.id.loginButton);

        mLoginButton.setOnClickListener(view -> {
            validate(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
        });
    }

    /**
     * Validate login credentials
     * @param userName the user name
     * @param userPassword the user password
     */
    private void validate(String userName, String userPassword) {
        Log.d(TAG, "validate() called with: userName = [" + userName + "], userPassword = [" + userPassword + "]");
        if ((userName.equals("")) && (userPassword.equals(""))) {
            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "WRONG NAME AND PASSWORD!", Toast.LENGTH_SHORT).show();
        }
    }
}
