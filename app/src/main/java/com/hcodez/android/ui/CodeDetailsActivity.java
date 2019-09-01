package com.hcodez.android.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.hcodez.android.HcodezApp;
import com.hcodez.android.R;
import com.hcodez.android.services.CodeService;
import com.hcodez.android.services.ContentHandler;
import com.hcodez.android.viewmodel.CodeViewModel;

public class CodeDetailsActivity extends MainMenuActivity{

    private static final String TAG = "CodeDetailsActivity";

    private TextView  mCodeStringValue;
    private TextView  mCodeNameTextView;
    private Button    mDeleteCodeButton;
    private Button    mOpenContentButton;
    private ImageView mCopyCodeImageView;

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

    private View.OnClickListener codeLongClickCallback = codeEntity -> {
        Log.d(TAG, "codeLongClickCallback.onLongClick() called with: codeEntity = [" + codeEntity + "]");

        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText("code", codeEntity.toString());

        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Code copied to clipboard", Toast.LENGTH_SHORT).show();
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        setContentView(R.layout.activity_code_details);

        mDeleteCodeButton = findViewById(R.id.code_details_delete_button);
        mCodeStringValue = findViewById(R.id.code_details_code_string_text_view);
        mCodeNameTextView = findViewById(R.id.code_details_code_name_text_view);
        mOpenContentButton = findViewById(R.id.code_details_open_content_button);
        mCopyCodeImageView = findViewById(R.id.copy_code_view);

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

            mCopyCodeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "codeLongClickCallback.onLongClick() called with: codeEntity = [" + codeEntity + "]");

                    ClipboardManager clipboard = (ClipboardManager)
                            getSystemService(Context.CLIPBOARD_SERVICE);

                    ClipData clip = ClipData.newPlainText("code", codeEntity.toString());

                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Code copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });
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
                    startActivity(ContentHandler.get(contentEntity.getResourceURI()).getIntent());
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
