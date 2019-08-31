package com.hcodez.android.ui;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.hcodez.android.HcodezApp;
import com.hcodez.android.R;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.db.entity.ContentEntity;
import com.hcodez.android.services.CodeService;
import com.hcodez.codeengine.model.CodeType;

import org.joda.time.Instant;

import java.net.URI;

public class AddCodeActivity extends MainMenuActivity {

    private static final String TAG = "AddCodeActivity";

    private EditText        mCodeNameEditText;
    private Switch          mSwitch;
    private EditText        mPasscodeEditText;
    private Button          mSaveButton;

    private CodeService     codeService;

    private View.OnClickListener saveButtonOnClickListener = new View.OnClickListener() {

        private static final String TAG = "SaveButtonOnClick";
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick() called with: v = [" + v + "]");
            /*
              Temporary variables for building the code
             */
            boolean buildingPublicCode = mSwitch.isChecked();

            /*
              Filter out bad usage
             */
            if (mCodeNameEditText.getText().toString().length() == 0) {
                Log.d(TAG, "onClick: empty code name");
                Toast.makeText(getApplicationContext(), "Insert a code name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mCodeNameEditText.getText().toString().length() > 16) {
                Log.d(TAG, "onClick: code name is too long");
                Toast.makeText(getApplicationContext(), "Code name longer than 16 characters", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mPasscodeEditText.getText().toString().length() > 16 && buildingPublicCode) {
                Log.d(TAG, "onClick: pasword is too long");
                Toast.makeText(getApplicationContext(), "Password longer than 16 characters", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d(TAG, "onClick: filtered out bas usage");

            /*
              Hardcoded variables
             */
            String hardcodedOwner = "Mirel";

            /*
              Build the content
             */
            final ContentEntity contentEntity = ContentEntity.builder()
                    .description("placeholder")
                    .resourceURI(URI.create("https://example.com"))
                    .build();

            /*
              Build the code
             */
            final CodeEntity codeEntity = CodeEntity.builder()
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
                    .updateTime(Instant.now())
                    .name(mCodeNameEditText.getText().toString())
                    .owner(hardcodedOwner)
                    .build();

            Log.d(TAG, "onClick: built entities");

            LiveData<CodeEntity> codeEntityLiveData = codeService.addNew(codeEntity, contentEntity);
            codeEntityLiveData.observe(AddCodeActivity.this, observedCodeEntity -> {
                Log.d(TAG, "onClick: code entity change observed");
                if (observedCodeEntity.getId() == null) {
                    Log.e(TAG, "onClick: encountered an error when saving the code");
                } else {
                    Log.d(TAG, "onClick: code saved successfully");
                }
            });

            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        setContentView(R.layout.activity_add_code);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        mCodeNameEditText = findViewById(R.id.add_code_name_edit_text);
        mSwitch = findViewById(R.id.add_code_public_flag_switch);
        mPasscodeEditText = findViewById(R.id.add_code_password_edit_text);
        mSaveButton = findViewById(R.id.add_code_save_button);

        mSaveButton.setOnClickListener(saveButtonOnClickListener);

        codeService = CodeService.getInstance(new HcodezApp());
    }
}