package com.hcodez.android.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hcodez.android.R;

public class EnterTextContentActivity extends AppCompatActivity {

    public static final String TAG = "EnterTextContentActivit";

    public static final String INTENT_STRING_EXTRA_KEY = "result";

    private EditText contentResourceUriEditText;

    private Button resultButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        setContentView(R.layout.activity_enter_text_content);

        contentResourceUriEditText = findViewById(R.id.enter_text_content_edit_text);
        resultButton = findViewById(R.id.enter_text_result_button);

        resultButton.setOnClickListener(v -> {
            Log.d(TAG, "resultButton.onClick() called");
            if (contentResourceUriEditText.getText().toString().equals("")) {
                Log.d(TAG, "resultButton.onClick(): empty text");
                Toast.makeText(getApplicationContext(), "No resource Uri entered", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent data = new Intent()
                    .putExtra(INTENT_STRING_EXTRA_KEY,
                            contentResourceUriEditText.getText().toString())
                    .putExtra(TAG, true);
            if (getParent() == null) {
                setResult(RESULT_OK, data);
            } else {
                getParent().setResult(RESULT_OK, data);
            }
            finish();
        });
    }
}
