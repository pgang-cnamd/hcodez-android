package com.hcodez.android.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import com.hcodez.android.HcodezApp;
import com.hcodez.android.R;
import com.hcodez.android.services.CodeService;
import com.hcodez.android.viewmodel.CodeViewModel;

public class CodeDetailsActivity extends MainMenuActivity{

    private static final String TAG = "CodeDetailsActivity";

    private TextView  mTextView;
    private Button    mDeleteCodeButton;

    private CodeService codeService;

    private CodeViewModel codeViewModel;

    private View.OnClickListener deleteButtonOnClickListener = new View.OnClickListener() {

        private static final String TAG = "SaveButtonOnClick";
        @Override
        public void onClick(View v) {
            codeViewModel.getObservableCode().observe(CodeDetailsActivity.this, codeEntity -> {
                if (codeEntity == null) {
                    Log.d(TAG, "onClick: null code entity");
                    return;
                }

                LiveData<Boolean> codeEntityLiveData = codeService.delete(codeEntity);

                codeEntityLiveData.observe(CodeDetailsActivity.this, result -> {
                    if (result == null) {
                        Log.d(TAG, "null result");
                        return;
                    }
                    if (!result) {
                        Log.e(TAG, "failed to delete code");
                        return;
                    }
                    Log.i(TAG, "deleted code successfully");
                    finish();
                });
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        setContentView(R.layout.activity_code_details);

        mDeleteCodeButton = findViewById(R.id.delete_code_button);
        mTextView = findViewById(R.id.show_code_identifier_text_view);

        mDeleteCodeButton.setOnClickListener(deleteButtonOnClickListener);

        codeService = CodeService.getInstance(new HcodezApp());

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.e(TAG, "onCreate: no extras received");
            return;
        }

        int codeId = extras.getInt("code_id", -1);
        if (codeId == -1) {
            Log.e(TAG, "onCreate: no code id received");
            return;
        }
        int contentId = extras.getInt("content_id", -1);
        if (contentId == -1) {
            Log.e(TAG, "onCreate: no content id received");
            return;
        }

        codeViewModel = new CodeViewModel.Factory(new HcodezApp(), codeId, contentId)
                .create(CodeViewModel.class);

        codeViewModel.getObservableCode().observe(this, codeEntity -> {
            if (codeEntity == null) {
                Log.d(TAG, "onCreate: null code entity");
                return;
            }
            mTextView.setText(codeEntity.toString());
        });
    }
}
