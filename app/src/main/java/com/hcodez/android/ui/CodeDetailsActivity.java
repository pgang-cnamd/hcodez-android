package com.hcodez.android.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;

import com.hcodez.android.HcodezApp;
import com.hcodez.android.R;
import com.hcodez.android.services.CodeService;
import com.hcodez.android.services.content.ContentHandler;
import com.hcodez.android.viewmodel.CodeViewModel;
import com.hcodez.codeengine.model.CodeType;

public class CodeDetailsActivity extends AppCompatActivity {

    private static final String TAG = "CodeDetailsActivity";

    private TextView      mCodeStringValue;

    private TextView      mCodeNameTextView;

    private Button        mOpenContentButton;

    private Button        mCopyCodeButton;

    private ImageView     mItemPublicStatus;

    private ImageView     mItemPasscodeProtected;

    private Toolbar       toolbar;

    private CodeService   codeService;

    private CodeViewModel codeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        setContentView(R.layout.activity_code_details);

        mCodeStringValue       = findViewById(R.id.code_details_code_string_text_view);
        mCodeNameTextView      = findViewById(R.id.code_details_code_name_text_view);
        mOpenContentButton     = findViewById(R.id.code_details_open_content_button);
        mCopyCodeButton        = findViewById(R.id.code_details_copy_code_button);
        mItemPublicStatus      = findViewById(R.id.code_details_public_status);
        mItemPasscodeProtected = findViewById(R.id.code_details_passcode_protected);
        toolbar                = findViewById(R.id.code_details_toolbar);
        setSupportActionBar(toolbar);

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

            mItemPublicStatus.setImageResource(
                    codeEntity.getCodeType() != CodeType.PRIVATE ?
                            R.drawable.public_code
                            : R.drawable.private_code);
            mItemPasscodeProtected.setImageResource(
                    codeEntity.getCodeType() != CodeType.PRIVATE ?
                            codeEntity.getPasscode() != null ?
                                    codeEntity.getPasscode().length() > 0 ?
                                            R.drawable.passcode_protected_code
                                            : R.drawable.no_passcode_protected_code
                                    : R.drawable.no_passcode_protected_code
                            : R.drawable.passcode_protected_code);

            mCopyCodeButton.setOnClickListener(v -> {
                Log.d(TAG, "codeLongClickCallback.onLongClick() called with: codeEntity = [" + codeEntity + "]");

                ClipboardManager clipboard = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);

                ClipData clip = ClipData.newPlainText("code", codeEntity.toString());

                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Code copied to clipboard", Toast.LENGTH_SHORT).show();
            });
        });

        mOpenContentButton.setOnClickListener(v ->
                codeViewModel.getObservableContent().observe(CodeDetailsActivity.this, contentEntity -> {
                    if (contentEntity == null) {
                        Log.d(TAG, "openContentOnClick: null content entity");
                        return;
                    }
                    if (contentEntity.getResourceURI() == null) {
                        Log.e(TAG, "openContentOnClick: null resource uri");
                        return;
                    }
                    if (contentEntity.getContentType() == null) {
                        Log.e(TAG, "onCreate: null content type");
                    }
                    ContentHandler opener = contentEntity.getContentHandler(getApplicationContext());
                    if (opener == null) {
                        Log.e(TAG, "onCreate: cannot open content");
                        Toast.makeText(getApplicationContext(), "Cannot open content", Toast.LENGTH_LONG).show();
                        return;
                    }
                    startActivity(opener.getOpenerIntent());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.code_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete_code:
                onDeleteCodeClick();
                return false;

            case R.id.action_edit_code:
                startActivity(new Intent(CodeDetailsActivity.this, AddCodeActivity.class));
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onDeleteCodeClick(){
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
}
