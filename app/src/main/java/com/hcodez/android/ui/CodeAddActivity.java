package com.hcodez.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.hcodez.android.DataRepository;
import com.hcodez.android.HcodezApp;
import com.hcodez.android.R;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.codeengine.model.CodeType;

import org.joda.time.Instant;

import java.net.MalformedURLException;
import java.net.URL;

public class CodeAddActivity extends MainMenuActivity {

    private EditText        mCodeNameEditText;
    private Switch          mSwitch;
    private EditText        mPasscodeEditText;
    private Button          mSaveButton;

    private View.OnClickListener saveButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*
              Temporary variables for building the code
             */
            boolean buildingPublicCode = mSwitch.isChecked();

            /*
              Filter out bad usage
             */
            if (mCodeNameEditText.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Insert a code name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mCodeNameEditText.getText().toString().length() > 16) {
                Toast.makeText(getApplicationContext(), "Code name longer than 16 characters", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mPasscodeEditText.getText().toString().length() > 25 && buildingPublicCode) {
                Toast.makeText(getApplicationContext(), "Password longer than 25 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            /*
              Hardcoded variables
             */
            String hardcodedOwner = "Mirel";
            String hardcodedIdentifier = "1234";

            /*
              Build the code
             */
            final CodeEntity code = CodeEntity.builder()
                    .passcode(
                            buildingPublicCode ? mPasscodeEditText.getText().toString() : null
                    )
                    .codeType(
                            buildingPublicCode ?
                                    mPasscodeEditText.getText().toString().length() != 0 ?
                                            CodeType.PUBLIC_WITH_PASSCODE : CodeType.PUBLIC_NO_PASSCODE
                                    : CodeType.PRIVATE
                    )
                    .createTime(Instant.now())
                    .identifier(hardcodedIdentifier)
                    .updateTime(Instant.now())
                    .name(mCodeNameEditText.getText().toString())
                    .owner(hardcodedOwner)
                    .build();
            /*
              Insert the code into the database
             */
            DataRepository dataRepository = new HcodezApp().getRepository();
            dataRepository.insertCode(code);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_add_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        mCodeNameEditText = findViewById(R.id.codeName);
        mSwitch = (Switch) findViewById(R.id.codeSwitch);
        mPasscodeEditText = findViewById(R.id.codePassword);
        mSaveButton = findViewById(R.id.codeSave);

        mSaveButton.setOnClickListener(saveButtonOnClickListener);
    }
}