package com.hcodez.android.ui;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.hcodez.android.R;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.codeengine.model.CodeType;

import java.net.MalformedURLException;
import java.net.URL;
import org.joda.time.Instant;

public class CodeAddActivity extends MainMenuActivity {

    private EditText mCodeNameEditText;
    private EditText mUrlEditText;
    private Switch mSwitch;
    private EditText mPasscodeEditText;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_add_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        CodeCreator();
    }

    public void CodeCreator() {
        mCodeNameEditText = findViewById(R.id.codeName);
        mUrlEditText = findViewById(R.id.url);
        mSwitch = (Switch) findViewById(R.id.codeSwitch);
        mPasscodeEditText = findViewById(R.id.codePassword);
        mSaveButton = findViewById(R.id.codeSave);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean mSwitchState = false;
                boolean mApplicable = false;
                URL mUrl = null;
                String mCodeName = null;
                String mPasscode = null;
                CodeType mCodeType = null;
                String mGlobalOwner = "Mirel";
                String mGlobalIdentifier = "1234";
                Instant mCreateTime = Instant.now();
                Instant mUpdateTime = Instant.now();
                mSwitchState = mSwitch.isChecked();

                if (mCodeNameEditText.getTextSize() != 0 && mCodeNameEditText.getTextSize() <= 16) {
                    mApplicable = true;
                    mCodeName = mCodeNameEditText.getText().toString();
                } else if (mCodeNameEditText.getTextSize() == 0) {
                    mApplicable = false;
                    Toast.makeText(getApplicationContext(), "Insert a code name", Toast.LENGTH_SHORT).show();
                } else if (mCodeNameEditText.getTextSize() > 16) {
                    mApplicable = false;
                    Toast.makeText(getApplicationContext(), "Code name longer than 16 characters", Toast.LENGTH_SHORT).show();
                }

                if (mUrlEditText.getTextSize() != 0) {
                    mApplicable = true;
                    try {
                        mUrl = new URL(mUrlEditText.getText().toString());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else {
                    mApplicable = true;
                }

                if (mSwitchState) {
                    if (mPasscodeEditText.getTextSize() != 0 && mPasscodeEditText.getTextSize() <= 25) {
                        mApplicable = true;
                        mPasscode = mPasscodeEditText.getText().toString();
                        mCodeType = CodeType.PUBLIC_WITH_PASSCODE;
                    } else if (mPasscodeEditText.getTextSize() == 0) {
                        mPasscode = null;
                        mApplicable = true;
                        mCodeType = CodeType.PUBLIC_NO_PASSCODE;
                    } else if (mPasscodeEditText.getTextSize() > 25) {
                        mApplicable = false;
                        Toast.makeText(getApplicationContext(), "Password longer than 25 characters", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mPasscode = null;
                    mApplicable = true;
                    mCodeType = CodeType.PRIVATE;
                }

                //if (mApplicable) {
                    CodeEntity code = CodeEntity.builder()
                            .passcode(mPasscode)
                            .codeType(mCodeType)
                            .createTime(mCreateTime)
                            .identifier(mGlobalIdentifier)
                            .updateTime(mUpdateTime)
                            .name(mCodeName)
                            .owner(mGlobalOwner)
                            .build();
                //}
            }
        });
    }
}