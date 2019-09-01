package com.hcodez.android.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import com.hcodez.android.HcodezApp;
import com.hcodez.android.R;
import com.hcodez.android.services.CodeService;
import com.hcodez.android.services.contentopener.ContentOpener;
import com.hcodez.android.viewmodel.CodeViewModel;

public class CodeDetailsActivity extends MainMenuActivity{

    private static final String TAG = "CodeDetailsActivity";

    private TextView mCodeStringValue;
    private TextView mCodeNameTextView;
    private Button   mDeleteCodeButton;
    private Button   mOpenContentButton;

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
        mCodeStringValue = findViewById(R.id.show_code_identifier_text_view);
        mCodeNameTextView = findViewById(R.id.show_code_name_text_view);
        mOpenContentButton = findViewById(R.id.open_content_button);

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
            mCodeNameTextView.setText(codeEntity.getName());
            mCodeStringValue.setText(codeEntity.toString());
        });

        mDeleteCodeButton.setOnClickListener(deleteButtonOnClickListener);
        mOpenContentButton.setOnClickListener(v ->
                codeViewModel.getObservableContent().observe(CodeDetailsActivity.this, contentEntity -> {
                    if (contentEntity == null) {
                        Log.d(TAG, "openContentOnClick: null content entity");
                        return;
                    }
                    if (contentEntity.getResourceURI() == null) {
                        Log.d(TAG, "openContentOnClick: null resource uri");
                        return;
                    }
                    startActivity(ContentOpener.get(contentEntity.getResourceURI()).getIntent());
        }));
    }

    public static Intent craftIntent(Context context, Integer codeId, Integer contentId) {
        Log.d(TAG, "craftIntent() called with: context = [" + context + "], codeId = [" + codeId + "], contentId = [" + contentId + "]");

        Intent intent = new Intent(context, CodeDetailsActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt("code_id", codeId);
        bundle.putInt("content_id", contentId);

        intent.putExtras(bundle);

        return intent;
    }
}
