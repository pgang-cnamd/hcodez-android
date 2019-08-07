package com.hcodez.android.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hcodez.android.R;

public class MainActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loginscreen);

        Name = (EditText) findViewById(R.id.loginName);
        Password = (EditText) findViewById(R.id.loginPass);
        Login = (Button) findViewById(R.id.loginButton);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });

    }

    private void validate(String userName, String userPassword){

        if((userName.equals("")) && (userPassword.equals(""))){
            Intent intent = new Intent(MainActivity.this, MainMenu.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"WRONG NAME AND PASSWORD!", Toast.LENGTH_SHORT).show();
        }

    }

}
