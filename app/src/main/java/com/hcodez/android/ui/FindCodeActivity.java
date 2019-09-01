package com.hcodez.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.hcodez.android.HcodezApp;
import com.hcodez.android.R;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.codeengine.model.Code;
import com.hcodez.codeengine.model.CodeType;
import com.hcodez.codeengine.parser.CodeParser;

public class FindCodeActivity extends AppCompatActivity {

    private static final String TAG = "FindCodeActivity";

    private Button textCodeButton;
    private Button imageCodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            Log.d(TAG, "onCreate: received ACTION_SEND");
            if ("text/plain".equals(type)) {
                Log.d(TAG, "onCreate: received text from intent");
                handleIncomingText(intent);
            } else if (type.startsWith("image/")) {
                Log.d(TAG, "onCreate: received text from intent");
                handleIncomingImage(intent);
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            Log.d(TAG, "onCreate: received ACTION_SEND_MULTIPLE");
            if (type.startsWith("image/")) {
                Log.w(TAG, "onCreate: multiple images are not supported");
            }
        } else {
            Log.d(TAG, "onCreate: no action received, displaying UI");
            setContentView(R.layout.activity_find_code);

            textCodeButton = findViewById(R.id.buttonCode);
            imageCodeButton = findViewById(R.id.buttonGallery);
        }
    }

    private void handleIncomingText(Intent intent) {
        Log.d(TAG, "handleIncomingText() called with: intent = [" + intent + "]");

        final String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText == null) {
            Log.d(TAG, "handleIncomingText: received nothing");
            return;
        }
        Log.d(TAG, "handleIncomingText: received " + sharedText);

        final MutableLiveData<CodeEntity> mutableLiveData = new MutableLiveData<>();

        new Thread(() -> {
            Log.d(TAG, "handleIncomingText: started thread");
            Code code = new CodeParser()
                    .addCodeTypes(CodeType.all())
                    .parseSingle(sharedText);

            if (code == null) {
                Log.d(TAG, "handleIncomingText: no code was parsed");
                runOnUiThread(() -> Toast
                        .makeText(getApplicationContext(), "No code was found", Toast.LENGTH_LONG)
                        .show());
                return;
            }

            CodeEntity entity = new HcodezApp()
                    .getDatabase()
                    .codeDao()
                    .loadCodeByIdentifierSync(code.getIdentifier());

            if (entity == null) {
                Log.d(TAG, "handleIncomingText: null entity");
                mutableLiveData.postValue(new CodeEntity());
                return;
            }

            if (entity.getId() == null || entity.getContentId() == null) {
                Log.d(TAG, "handleIncomingText: no ids");
                mutableLiveData.postValue(new CodeEntity());
                return;
            }

            Log.d(TAG, "handleIncomingText: found good code entity");
            mutableLiveData.postValue(entity);
        }).start();

        mutableLiveData.observe(this, codeEntity -> {
            Log.d(TAG, "handleIncomingText: observed change");
            if (codeEntity == null) {
                Log.d(TAG, "handleIncomingText: null received");
                return;
            }
            if (codeEntity.getId() == null || codeEntity.getContentId() == null) {
                Log.e(TAG, "didn't find a code");
                Toast.makeText(getApplicationContext(), "No matching code was found in the database", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            startActivity(CodeDetailsActivity
                    .craftIntent(getApplicationContext(),
                            codeEntity.getId(),
                            codeEntity.getContentId()));
            finish();
        });
    }

    private void handleIncomingImage(Intent intent) {
        Log.d(TAG, "handleIncomingImage() called with: intent = [" + intent + "]");
        Log.e(TAG, "handleIncomingImage: not implemented yet");
        throw new UnsupportedOperationException("scan shared images is not implemented yet");
    }
}