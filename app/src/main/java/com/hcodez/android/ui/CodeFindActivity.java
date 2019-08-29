package com.hcodez.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.hcodez.android.R;
import com.hcodez.android.scanner.CodeScanner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CodeFindActivity extends Activity {

    private static final String TAG = "FindCodeActivity";

    private Button scanButton;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_find_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width  = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .2));

        scanButton = findViewById(R.id.buttonScan);

        scanButton.setOnClickListener(v -> dispatchTakePictureIntent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: received result from REQUEST_IMAGE_CAPTURE");
            processImage();
        }
        finish();
    }

    private void dispatchTakePictureIntent() {
        Log.d(TAG, "dispatchTakePictureIntent() called");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Log.d(TAG, "dispatchTakePictureIntent: camera available");

            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.d(TAG, "dispatchTakePictureIntent: created photo file");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "dispatchTakePictureIntent: error while creating photo file", e);
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.hcodez.android.scanner",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Toast.makeText(this, "Failed to launch camera", Toast.LENGTH_LONG).show();
            }
        }
        Log.d(TAG, "dispatchTakePictureIntent() returned");
    }

    private void processImage() {
        new Thread(() -> {
            Log.d(TAG, "processImage: start thread");

            Log.d(TAG, "processImage: open image and rescale before processing");
            Bitmap img = BitmapFactory.decodeFile(currentPhotoPath);
            if (img == null) {
                Log.e(TAG, "processImage: could not open image file");
                return;
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 25, stream);
            img.recycle();

            final CodeScanner codeScanner = CodeScanner.getInstance(getApplicationContext());
            final String processedText;
            String processedText1;
            try {
                Log.d(TAG, "processImage: start processing");
                processedText1 = codeScanner.getTextFromImageSync(CodeScanner.getImageFromByteStream(stream.toByteArray()));
                Log.i(TAG, "processImage: successfully processed image");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "processImage: error", e);
                processedText1 = null;
            }
            processedText = processedText1;
            runOnUiThread(() -> {
                Toast.makeText(getApplicationContext(),
                    processedText != null ?
                        "Processed text: " + processedText
                        : "Could not get processed image",
                    Toast.LENGTH_LONG).show();
            });
        }).start();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}