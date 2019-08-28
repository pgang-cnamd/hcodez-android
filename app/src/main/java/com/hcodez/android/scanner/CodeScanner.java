package com.hcodez.android.scanner;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.hcodez.android.R;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CodeScanner {

    private static final String TAG = "CodeScanner";

    /**
     * CodeScanner instance
     */
    private static CodeScanner sInstance;

    /**
     * The google cloud vision object
     */
    private final Vision vision;

    /**
     * The feature type used for retrieving the text from images
     */
    private static final String FEATURE_TYPE = "TEXT_DETECTION";


    /**
     * Create a new CodeScanner instance
     * @param apiKey the api key for the google cloud vision api
     */
    private CodeScanner(final String apiKey) {
        Vision.Builder visionBuilder = new Vision.Builder(
                new NetHttpTransport(),
                new AndroidJsonFactory(),
                null);

        visionBuilder.setVisionRequestInitializer(
                new VisionRequestInitializer(apiKey));

        this.vision = visionBuilder.build();
    }


    /**
     * Get the CodeScanner instance
     * @return the CodeScanner
     */
    public static CodeScanner getInstance(final Context context) {
        Log.d(TAG, "getInstance: requested CodeScanner instance");
        if (sInstance == null) {
            synchronized (CodeScanner.class) {
                if (sInstance == null) {
                    sInstance = new CodeScanner(context.getString(R.string.google_cloud_vision_api_key));
                    Log.i(TAG, "getInstance: created new CodeScanner instance");
                }
            }
        }
        return sInstance;
    }

    /**
     * Get an image for the given byte array
     * @param imageData the byte array
     * @return the Image
     */
    public static Image getImageFromByteStream(byte[] imageData) {
        Log.d(TAG, "getImageFromByteStream() called with: imageData = [" + Arrays.toString(imageData) + "]");
        final Image image = new Image();
        image.encodeContent(imageData);
        Log.d(TAG, "getImageFromByteStream() returned: " + image);
        return image;
    }

    /**
     * Get an image from the given file
     * @param file the file
     * @return the image
     * @throws IOException thrown by a used method
     */
    public static Image getImageFromFile(final File file) throws IOException {
        Log.d(TAG, "getImageFromFile() called with: file = [" + file + "]");
        InputStream inputStream = new FileInputStream(file);
        return getImageFromByteStream(IOUtils.toByteArray(inputStream));
    }

    /**
     * Get an image from the file described by the given path
     * @param filePath the file path
     * @return the image
     * @throws IOException thrown by a used method
     */
    public static Image getImageFromFile(final String filePath) throws IOException {
        Log.d(TAG, "getImageFromFile() called with: filePath = [" + filePath + "]");
        return getImageFromFile(new File(filePath));
    }

    /**
     * Get the text from an image(synchronously)
     * @param image the image
     * @return the text
     * @throws IOException thrown by a used method
     */
    public String getTextFromImageSync(final Image image) throws IOException {
        Log.d(TAG, "getTextFromImageSync() called with: image = [" + image + "]");
        Feature textFromImage = new Feature();
        textFromImage.setType(FEATURE_TYPE);

        AnnotateImageRequest request = new AnnotateImageRequest();
        request.setImage(image);
        request.setFeatures(Collections.singletonList(textFromImage));

        BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();
        batchRequest.setRequests(Collections.singletonList(request));

        BatchAnnotateImagesResponse batchResponse = vision.images().annotate(batchRequest).execute();

        StringBuilder stringBuilder = new StringBuilder();

        List<AnnotateImageResponse> responseList = batchResponse.getResponses();
        for (AnnotateImageResponse response : responseList) {
            Log.d(TAG, "getTextFromImageSync: iterating over response " + response);
            stringBuilder.append(response != null ?
                    response.getFullTextAnnotation() != null ?
                            response.getFullTextAnnotation().getText()
                            : ""
                    : "");
        }

        Log.d(TAG, "getTextFromImageSync() returned: " + stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * Get the text from an image
     * @param image the image
     * @return a live data object that will contain the output; in case of error, null will be posted
     */
    public LiveData<String> getTextFromImage(final Image image) {
        Log.d(TAG, "getTextFromImage() called with: image = [" + image + "]");
        final MutableLiveData<String> processingOutput = new MutableLiveData<>();

        new Thread(() -> {
            Log.d(TAG, "getTextFromImage: processing thread started");
            try {
                processingOutput.postValue(getTextFromImageSync(image));
                Log.d(TAG, "getTextFromImage: successfully processed image");
            } catch (IOException e) {
                Log.e(TAG, "getTextFromImage: error while processing the image", e);
                e.printStackTrace();
                processingOutput.postValue(null);
            }
        }).start();

        Log.d(TAG, "getTextFromImage() returned: " + processingOutput);
        return processingOutput;
    }
}
