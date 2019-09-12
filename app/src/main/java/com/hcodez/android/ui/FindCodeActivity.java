package com.hcodez.android.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nonnull;

public class FindCodeActivity extends AppCompatActivity {

    private static final String TAG = "FindCodeActivity";

    /**
     * Quality of the bitmap sent further to Google Cloud Vision
     * for processing
     */
    private static final int    BITMAP_PROCESSING_QUALITY = 20;

    private Button textCodeButton;
    private Button imageCodeButton;
    private Button scanButton;

    private boolean spawnedByIntent = false;

    /**
     * Request code for requesting a photo to be captured in order to be scanned
     */
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * Current path of the captured photo(in order to delete it after processing)
     */
    private Uri currentPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            Log.d(TAG, "onCreate: received ACTION_SEND");

            spawnedByIntent = true;

            if (!isNetworkAvailable(this)) {
                Toast.makeText(this, "Internet connectivity is required for code scanning", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            if ("text/plain".equals(type)) {
                Log.d(TAG, "onCreate: received text from intent");
                handleIncomingText(intent
                        .getStringExtra(Intent.EXTRA_TEXT));
            } else if (type.startsWith("image/")) {
                Log.d(TAG, "onCreate: received image from intent");
                handleIncomingImage(intent.getParcelableExtra(Intent.EXTRA_STREAM));
            }
        } else {
            Log.d(TAG, "onCreate: no action received, displaying UI");

            spawnedByIntent = false;

            setContentView(R.layout.activity_find_code);

            textCodeButton = findViewById(R.id.find_code_enter_text_button);
            imageCodeButton = findViewById(R.id.find_code_parse_image_button);
            scanButton = findViewById(R.id.find_code_scan_code_button);
            scanButton.setOnClickListener(v -> dispatchTakePictureIntent());
        }
    }

    /**
     * Handle incoming text(parse codes)
     * @param sharedText the text that needs to be handled
     */
    private void handleIncomingText(final String sharedText) {
        Log.d(TAG, "handleIncomingText() called with: sharedText = [" + sharedText + "]");

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
                mutableLiveData.postValue(CodeEntity.builder().id(-1).build());
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
            if (codeEntity.getId() == null) {
                Log.w(TAG, "didn't find a code in the database");
                Toast.makeText(getApplicationContext(), "No matching code was found in the database", Toast.LENGTH_LONG).show();
                if (spawnedByIntent)
                    finish();
                return;
            } else if (codeEntity.getContentId() == null) {
                Log.w(TAG, "handleIncomingText: no code was parsed");
                Toast.makeText(getApplicationContext(), "No code was scanned", Toast.LENGTH_LONG).show();
                if (spawnedByIntent)
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

    /**
     * Handle an incoming image for processing
     * @param imageUri the Uri to the image
     */
    private void handleIncomingImage(Uri imageUri) {
        Log.d(TAG, "handleIncomingImage() called with: imageUri = [" + imageUri + "]");
        if (imageUri == null) {
            Log.d(TAG, "handleIncomingImage: received nothing");
            return;
        }
        if (imageUri.getPath() == null) {
            Log.d(TAG, "handleIncomingImage: can't retrieve path from uri");
            return;
        }
        processImage(imageUri);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: received result from REQUEST_IMAGE_CAPTURE");
            processImage(currentPhotoUri);
        }
    }

    /**
     * Dispatch an intent requesting the camera app to take a picture and save it
     */
    private void dispatchTakePictureIntent() {
        Log.d(TAG, "dispatchTakePictureIntent() called");

        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Internet connectivity is required for code scanning", Toast.LENGTH_LONG).show();
            return;
        }

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

    /**
     * Process the image(to find codes)
     * @param imageUri the uri to the image
     */
    private void processImage(Uri imageUri) {
        final MutableLiveData<String> textLiveData = new MutableLiveData<>();

        new Thread(() -> {
            Log.d(TAG, "processImage: start thread");

            Log.d(TAG, "processImage: open image and rescale before processing");
            Bitmap img = null;
            try {
                img = BitmapFactory.decodeStream(getInputStreamFromUri(imageUri));
            } catch (FileNotFoundException e) {
                Log.e(TAG, "processImage: failed to open image file", e);
                e.printStackTrace();
                return;
            }
            if (img == null) {
                Log.e(TAG, "processImage: could not open image file");
                return;
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, BITMAP_PROCESSING_QUALITY, stream);
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
            if (processedText != null) {
                textLiveData.postValue(processedText);
            } else {
                textLiveData.removeObservers(this);
                runOnUiThread(() -> Toast
                        .makeText(getApplicationContext(), "No code found in image", Toast.LENGTH_LONG).show());
            }
            if (!imageUri.equals(currentPhotoUri)) {
                Log.d(TAG, "processImage: input image not created by hcodez, not deleting");
                return;
            }
            try {
                File file = new File(imageUri.getPath());
                if (file.delete()) {
                    Log.d(TAG, "processImage: successfully deleted scanned photo");
                } else {
                    Log.w(TAG, "processImage: could not delete scanned photo");
                }
            } catch (Exception e){
                Log.w(TAG, "processImage: could not delete scanned photo", e);
            }
        }).start();

        textLiveData.observe(this, text -> {
            if (text == null) {
                Log.d(TAG, "processImage: null text received");
                return;
            }
            if (text.equals("")) {
                Log.d(TAG, "processImage: empty text received");
                return;
            }
            handleIncomingText(text);
        });
    }

    /**
     * Create a file for the picture to be stored in
     * @return the file for the image
     * @throws IOException exception regarding the filesystem
     */
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
        currentPhotoUri = Uri.fromFile(image);
        return image;
    }

    /**
     * Extract an InputStream from an Uri(to be used with content Uris)
     * @param uri the Uri to be used
     * @return an InputStream
     * @throws FileNotFoundException if the file was not found
     */
    private InputStream getInputStreamFromUri(@Nonnull Uri uri) throws FileNotFoundException {
        Log.d(TAG, "getInputStreamFromUri() called with: uri = [" + uri + "]");

        if (uri.getScheme() == null) {
            Log.d(TAG, "getInputStreamFromUri: null uri received");
            throw new FileNotFoundException("missing uri scheme");
        }
        if (uri.getScheme().equals("file")) {
            Log.d(TAG, "getInputStreamFromUri: file scheme");
            return new FileInputStream(new File(URI.create(uri.toString())));
        }
        if (uri.getScheme().equals("content")) {
            Log.d(TAG, "getInputStreamFromUri: content scheme");
            return getContentResolver().openInputStream(uri);
        }
        throw new FileNotFoundException("bad uri scheme");
    }

    /**
     * Checks whether the device is connected to the internet or not
     * @return network connectivity status
     */
    public boolean isNetworkAvailable(Context context) {
        Log.d(TAG, "isNetworkAvailable() called");

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null) {
            Log.w(TAG, "isNetworkAvailable: network info not available");
            return false;
        }

        if (!activeNetworkInfo.isConnected()) {
            Log.w(TAG, "isNetworkAvailable: device is not connected to the internet");
            return false;
        }

        Log.d(TAG, "isNetworkAvailable: device is connected to the internet");
        return true;
    }
}