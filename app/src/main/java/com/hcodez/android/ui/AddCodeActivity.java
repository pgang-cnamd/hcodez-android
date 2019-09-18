package com.hcodez.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.hcodez.android.HcodezApp;
import com.hcodez.android.R;
import com.hcodez.android.db.AppDatabase;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.db.entity.ContentEntity;
import com.hcodez.android.services.CodeService;
import com.hcodez.android.services.content.ContentType;
import com.hcodez.codeengine.model.CodeType;

import org.joda.time.Instant;

public class AddCodeActivity extends AppCompatActivity {

    private static final String TAG = "AddCodeActivity";

    private EditText        mCodeNameEditText;

    private Switch          mSwitch;

    private EditText        mPasscodeEditText;

    private Button          mSaveButton;

    private Button          mAddContentButton;

    private CodeService     codeService;
    private AppDatabase appDatabase;

    private Uri         currentContentUri  = null;
    private ContentType currentContentType = null;

    /*
      database entities
     */
    private CodeEntity codeEntity       = null;
    private ContentEntity contentEntity = null;

    /**
     * Level that specifies how the activity was spawned
     *
     * -1 -> from within the activity
     *  1 -> from "Add code" via easy share
     *  2 -> from "Edit code"
     */
    private int spawnLevel = SPAWN_LEVEL_INTERNAL;

    private static final int SPAWN_LEVEL_INTERNAL = -1;
    private static final int SPAWN_LEVEL_EASY_SHARE = 1;
    private static final int SPAWN_LEVEL_EDIT_CODE = 2;

    /**
     * Method used for hiding the keyboard when touching outside the text
     */
    public void hideKeyboard(View view) {
        Log.d(TAG, "hideKeyboard() called with: view = [" + view + "]");
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

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
                Log.d(TAG, "onClick: password is too long");
                Toast.makeText(getApplicationContext(), "Password longer than 16 characters", Toast.LENGTH_SHORT).show();
                return;
            }
            if (contentEntity.getResourceURI() == null && currentContentUri == null) {
                Log.d(TAG, "onClick: empty resource uri");
                Toast.makeText(getApplicationContext(), "No content", Toast.LENGTH_SHORT).show();
                return;
            }
            if (contentEntity.getContentType() == null && currentContentType == null) {
                Log.e(TAG, "onClick: missing content type");
                return;
            }
            Log.d(TAG, "onClick: filtered out bad usage");

            /*
              Build the content
             */
            if (contentEntity == null) {
                Log.d(TAG, "onClick: null content entity, building a new one");
                contentEntity = ContentEntity.builder()
                        .description("placeholder")
                        .resourceURI(currentContentUri)
                        .contentType(currentContentType)
                        .build();
            } else {
                Log.d(TAG, "onClick: reusing content entity");
            }

            /*
              Build the code
             */
            if (codeEntity == null) {
                Log.d(TAG, "onClick: null code entity, building a new one");
                codeEntity = CodeEntity.builder()
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
                        .owner(getSharedPreferences("login_prefs", MODE_PRIVATE)
                                .getString("owner", null))
                        .build();
            } else {
                Log.d(TAG, "onClick: reusing code entity");
            }

            /*
              check if a new content was selected if editing
             */
            if (spawnLevel == SPAWN_LEVEL_EDIT_CODE) {
                Log.d(TAG, "onClick: checking if a new content was selected");
                if (contentEntity.getContentType() != null) {
                        if (!contentEntity.getContentType().toString().equals(currentContentType.toString())
                                || !contentEntity.getResourceURI().toString().equals(currentContentUri.toString())) {
                            Log.d(TAG, "onClick: new content was selected, deleting old one");
                            new Thread(() -> appDatabase.contentDao().delete(contentEntity)).start();
                            Log.d(TAG, "onClick: deleted content entity, creating a new one");
                            contentEntity = ContentEntity.builder()
                                    .description("placeholder")
                                    .contentType(currentContentType)
                                    .resourceURI(currentContentUri)
                                    .build();
                        }
                }
            }

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

        mCodeNameEditText = findViewById(R.id.add_code_name_edit_text);
        mSwitch           = findViewById(R.id.add_code_public_flag_switch);
        mPasscodeEditText = findViewById(R.id.add_code_password_edit_text);
        mSaveButton       = findViewById(R.id.add_code_save_button);
        mAddContentButton = findViewById(R.id.add_content_button);

        mSaveButton.setOnClickListener(saveButtonOnClickListener);
        mAddContentButton.setOnClickListener(addContentClick);
        mSwitch.setOnCheckedChangeListener(this::onCheckedChanged);

        codeService = CodeService.getInstance(new HcodezApp());
        appDatabase = new HcodezApp().getDatabase();

        Log.d(TAG, "onCreate: checking spawn level");
        if (getIntent() == null) {
            Log.d(TAG, "onCreate: no intent");
            return;
        }
        Log.d(TAG, "onCreate: intent " + getIntent());

        if (getIntent().getClipData() == null && getIntent().getExtras() == null) {
            Log.d(TAG, "onCreate: spawned internally");
            return;
        }
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().isEmpty()) {
                Log.d(TAG, "onCreate: spawned internally");
                return;
            }
        }

        int codeId = -1;
        int contentId = -1;
        if (getIntent().getClipData() != null) {
            Log.d(TAG, "onCreate: spawned by easy share");
            spawnLevel = SPAWN_LEVEL_EASY_SHARE;
        } else {
            codeId = getIntent().getIntExtra("code_id", -1);
            contentId = getIntent().getIntExtra("content_id", -1);
            if (codeId == -1 || contentId == -1) {
                Log.w(TAG, "onCreate: no ids received");
                finish();
                return;
            }
            spawnLevel = SPAWN_LEVEL_EDIT_CODE;
        }

        switch (spawnLevel) {
            case SPAWN_LEVEL_EASY_SHARE:
                Log.d(TAG, "onCreate: spawn level easy share");
                contentEntity = new ContentEntity();
                contentEntity.setContentType(ContentType.MEDIA);
                contentEntity.setResourceURI(getIntent().getClipData().getItemAt(0).getUri());
                return;
            case SPAWN_LEVEL_EDIT_CODE:
                Log.d(TAG, "onCreate: spawn level edit code");
                appDatabase.codeDao().loadCode(codeId).observe(this, entity -> {
                    if (entity == null) {
                        Log.d(TAG, "onCreate: null code entity");
                        return;
                    }
                    Log.d(TAG, "onCreate: loaded edit code code entity");
                    codeEntity = entity;
                    mCodeNameEditText.setText(codeEntity.getName());
                });
                appDatabase.contentDao().loadContent(contentId).observe(this, entity -> {
                    if (entity == null) {
                        Log.d(TAG, "onCreate: null content entity");
                        return;
                    }
                    Log.d(TAG, "onCreate: loaded edit code content entity");
                    contentEntity = entity;
                });
        }
    }

    public void onCheckedChanged (CompoundButton buttonView, boolean isChecked){

        switch (buttonView.getId()) {
            case R.id.add_code_public_flag_switch:
                if (isChecked) {
                    mPasscodeEditText.setVisibility(View.VISIBLE);
                    mPasscodeEditText.setText(null);
                } else {
                    mPasscodeEditText.setVisibility(View.GONE);
                }
                break;
        }
    }

    private View.OnClickListener addContentClick = v -> {
        Intent intent = new Intent(AddCodeActivity.this, AddContentActivity.class);
        startActivityForResult(intent, 0);
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");

        Runnable errorToast = () -> {
            Toast.makeText(this, "Could not create content, please try again", Toast.LENGTH_LONG).show();
        };

        if (data == null) {
            Log.d(TAG, "onActivityResult: received null data");
            return;
        }

        if (resultCode != RESULT_OK) {
            runOnUiThread(errorToast);
        }
        Log.d(TAG, "onActivityResult: data " + data.toString());

        String uriString = data.getData() != null ?
                data.getData().toString()
                : null;
        if (uriString == null) {
            runOnUiThread(errorToast);
            Log.w(TAG, "onActivityResult: null resource uri");
            return;
        }
        Uri currentContentUri = Uri.parse(uriString);
        Log.d(TAG, "onActivityResult: content uri " + currentContentUri);
        currentContentType = data
                .getStringExtra(AddContentActivity.INTENT_CONTENT_TYPE_KEY) != null ?
                        ContentType.valueOf(
                                data.getStringExtra(AddContentActivity.INTENT_CONTENT_TYPE_KEY))
                        : null;
        Log.d(TAG, "onActivityResult: content type " + currentContentType);
        if (currentContentUri == null || currentContentType == null) {
            Log.w(TAG, "onActivityResult: error getting uri and/or content type");
            return;
        }
    }
}