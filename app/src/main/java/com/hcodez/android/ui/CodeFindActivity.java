package com.hcodez.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.hcodez.android.R;
import com.hcodez.android.scanner.CodeScanner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CodeFindActivity extends Activity {

    private static final String TAG = "FindCodeActivity";

    private Button scanButton;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_find_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width  = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .2));

        scanButton.setOnClickListener(v -> {
            dispatchTakePictureIntent();
        });
    }

    private void dispatchTakePictureIntent() {
        Log.d(TAG, "dispatchTakePictureIntent() called");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        Log.d(TAG, "dispatchTakePictureIntent() returned");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: received result from REQUEST_IMAGE_CAPTURE");
            Bundle extras = data.getExtras();
            if (extras != null) {
                Log.d(TAG, "onActivityResult: received data");
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (imageBitmap != null) {
                    Log.d(TAG, "onActivityResult: image exists");
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    processImage(stream.toByteArray());
                    imageBitmap.recycle();

                } else {
                    Log.w(TAG, "onActivityResult: image is null");
                }
            } else {
                Toast.makeText(this, "Could not retireve image", Toast.LENGTH_LONG).show();
            }
        }
        finish();
    }

    private void processImage(byte[] imageData) {
        new Thread(() -> {
            Log.d(TAG, "processImage: start thread");
            final CodeScanner codeScanner = CodeScanner.getInstance(getApplicationContext());
            String processedText = null;
            try {
                Log.d(TAG, "processImage: start processing");
                processedText = codeScanner.getTextFromImageSync(CodeScanner.getImageFromByteStream(imageData));
                Log.i(TAG, "processImage: successfully processed image");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "processImage: error", e);
            }
            Toast.makeText(getApplicationContext(),
                    processedText != null ?
                        "Processed text: " + processedText
                        : "Could not get processed image",
                    Toast.LENGTH_LONG).show();
        }).start();
    }
}