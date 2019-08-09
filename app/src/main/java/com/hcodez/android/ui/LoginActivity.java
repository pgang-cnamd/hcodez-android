package com.hcodez.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hcodez.android.R;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button   mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameEditText = findViewById(R.id.loginName);
        mPasswordEditText = findViewById(R.id.loginPass);
        mLoginButton = findViewById(R.id.loginButton);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
                finish();
            }
        });

    }

    /**
     * Validate login credentials
     * @param userName the user name
     * @param userPassword the user password
     */
    private void validate(String userName, String userPassword) {

        if ((userName.equals("")) && (userPassword.equals(""))) {
            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "WRONG NAME AND PASSWORD!", Toast.LENGTH_SHORT).show();
        }

    }

}
