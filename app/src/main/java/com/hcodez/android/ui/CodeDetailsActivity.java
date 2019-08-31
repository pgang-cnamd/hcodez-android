package com.hcodez.android.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.hcodez.android.HcodezApp;
import com.hcodez.android.R;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.services.CodeService;
import com.hcodez.codeengine.model.Code;

public class CodeDetailsActivity extends MainMenuActivity implements LifecycleOwner {

    private TextView  mTextView;
    private String    mCodeItem;
    private Button    mDeleteCodeButton;

    private CodeService codeService;

    private Bundle bundle = getIntent().getExtras();

    private View.OnClickListener deleteButtonOnClickListener = new View.OnClickListener() {

        private static final String TAG = "SaveButtonOnClick";
        @Override
        public void onClick(View v) {
            Object object = bundle.getSerializable("entity");

            if (object instanceof CodeEntity) {
                final CodeEntity codeEntity = (CodeEntity) object;

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
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_details);

        mCodeItem     = bundle != null ? bundle.getString("codeItem") : null;

        mDeleteCodeButton = findViewById(R.id.delete_code_button);
        mTextView = findViewById(R.id.show_code_identifier_text_view);
        mTextView.setText(mCodeItem != null ? mCodeItem : "empty");

        mDeleteCodeButton.setOnClickListener(deleteButtonOnClickListener);

        codeService = CodeService.getInstance(new HcodezApp());
    }
}
