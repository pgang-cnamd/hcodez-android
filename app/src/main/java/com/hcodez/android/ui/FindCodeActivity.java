package com.hcodez.android.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.MutableLiveData;

import com.hcodez.android.HcodezApp;
import com.hcodez.android.R;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.scanner.CodeScanner;
import com.hcodez.codeengine.model.Code;
import com.hcodez.codeengine.model.CodeType;
import com.hcodez.codeengine.parser.CodeParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FindCodeActivity extends AppCompatActivity {

    private static final String TAG = "FindCodeActivity";

    private Button textCodeButton;
    private Button imageCodeButton;
    private Button scanButton;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private String currentPhotoPath;

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

            textCodeButton = findViewById(R.id.find_code_enter_text_button);
            imageCodeButton = findViewById(R.id.find_code_parse_image_button);
            scanButton = findViewById(R.id.find_code_scan_code_button);
            scanButton.setOnClickListener(v -> dispatchTakePictureIntent());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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