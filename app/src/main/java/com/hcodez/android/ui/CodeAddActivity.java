package com.hcodez.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.hcodez.android.DataRepository;
import com.hcodez.android.HcodezApp;
import com.hcodez.android.R;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.db.entity.ContentEntity;
import com.hcodez.codeengine.model.CodeType;

import org.joda.time.Instant;

import java.net.URI;
import java.util.List;
import java.util.Objects;

public class CodeAddActivity extends MainMenuActivity {

    private EditText        mCodeNameEditText;
    private Switch          mSwitch;
    private EditText        mPasscodeEditText;
    private Button          mSaveButton;

    private View.OnClickListener saveButtonOnClickListener = new View.OnClickListener() {

        private static final String TAG = "SaveButtonOnClick";
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
              Start the database handling thread
             */
            new Thread(() -> { // FIXME: 2019-08-28 missing content support, always adding a placeholder one
                /*
                  Get the data repository object
                */
                final DataRepository dataRepository = new HcodezApp().getRepository();

                /*
                  Build the content
                 */
                final ContentEntity contentEntity = ContentEntity.builder()
                        .description("placeholder")
                        .resourceURI(URI.create("https://example.com"))
                        .build();

                /*
                  Insert the content
                 */
                Long contentId = dataRepository.insertContent(contentEntity);

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
                        .contentId(contentId.intValue())
                        .build();

                /*
                  Insert the code into the database
                 */
                dataRepository.insertCode(code);

            }).start();
            finish();
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
        mSwitch = findViewById(R.id.codeSwitch);
        mPasscodeEditText = findViewById(R.id.codePassword);
        mSaveButton = findViewById(R.id.codeSave);

        mSaveButton.setOnClickListener(saveButtonOnClickListener);
    }
}