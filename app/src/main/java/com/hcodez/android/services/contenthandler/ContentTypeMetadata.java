package com.hcodez.android.services.contenthandler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.hcodez.android.ui.EnterTextContentActivity;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Container for the metadata required for any kind of operations
 * that involves Content
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentTypeMetadata {

    private static final String TAG = "ContentTypeMetadata";

    /**
     * Class that contains information about the intents(used to create
     * or to open a content type)
     */
    @Data
    public class IntentData {

        @SerializedName("enter_text_activity") private Boolean             enterTextActivity;
        @SerializedName("action")              private String              action;
        @SerializedName("uri")                 private String              uri;
        @SerializedName("extra")               private Map<String, Object> extra;
        @SerializedName("categories")          private Set<String>         categories;
        @SerializedName("flags")               private Set<Integer>        flags;
        @SerializedName("type")                private String              type;
        @SerializedName("request_code")        private Integer             requestCode;
    }

    @SerializedName("schemes")          private Set<String> schemes;
    @SerializedName("validation_regex") private String      validationRegex;
    @SerializedName("creator")          private IntentData  creator;
    @SerializedName("opener")           private IntentData  opener;

    private transient Pattern validationPattern;

    public Pattern getValidationPattern() {
        if (validationRegex == null) {
            synchronized (this) {
                if (validationRegex == null) {
                    return null;
                }
            }
        }
        if (validationPattern == null) {
            synchronized (this) {
                if (validationPattern == null) {
                    validationPattern = Pattern.compile(validationRegex);
                }
            }
        }
        return validationPattern;
    }

    public Intent buildOpenerIntent(Uri uri) {
        Log.d(TAG, "buildOpenerIntent() called with: uri = [" + uri + "]");

        final Intent intent = new Intent(opener.getAction(), uri);

        if (creator.getExtra() != null) {
            Log.d(TAG, "buildOpenerIntent: adding extra");
            intent.putExtras(mapToBundle(creator.getExtra()));
        }

        if (opener.getCategories() != null) {
            Log.d(TAG, "buildOpenerIntent: adding categories");
            for (String category : opener.getCategories()) {
                intent.addCategory(category);
            }
        }

        if (opener.getFlags() != null) {
            Log.d(TAG, "buildOpenerIntent: adding flags");
            for (Integer flag : opener.getFlags()) {
                intent.addFlags(flag);
            }
        }

        if (opener.getType() != null) {
            Log.d(TAG, "buildOpenerIntent: adding type");
            intent.setType(opener.getType());
        }

        return intent;
    }

    public Intent buildCreatorIntent(Context context) {
        Log.d(TAG, "buildCreatorIntent() called with: context = [" + context + "]");

        if (creator.getEnterTextActivity() != null) {
            if (creator.getEnterTextActivity()) {
                Log.d(TAG, "buildCreatorIntent: building enter text content intent");
                return new Intent(context, EnterTextContentActivity.class);
            }
        }
        Log.d(TAG, "buildCreatorIntent: building complex intent");

        final Intent intent = new Intent(creator.getAction(), Uri.parse(creator.getUri()));

        if (creator.getExtra() != null) {
            Log.d(TAG, "buildOpenerIntent: adding extra");
            intent.putExtras(mapToBundle(creator.getExtra()));
        }

        if (creator.getCategories() != null) {
            Log.d(TAG, "buildCreatorIntent: adding categories");
            for (String category : creator.getCategories()) {
                intent.addCategory(category);
            }
        }

        if (creator.getFlags() != null) {
            Log.d(TAG, "buildCreatorIntent: adding flags");
            for (Integer flag : creator.getFlags()) {
                intent.addFlags(flag);
            }
        }

        if (creator.getType() != null) {
            Log.d(TAG, "buildCreatorIntent: adding type");
            intent.setType(creator.getType());
        }

        return intent;
    }

    private Bundle mapToBundle(Map<String, Object> map) {
        Log.d(TAG, "mapToBundle() called with: map = [" + map + "]");

        final Bundle bundle = new Bundle();

        for (String key: map.keySet()) {
            if (key == null) {
                Log.d(TAG, "mapToBundle: null key, skipping");
                continue;
            }
            if (key.equals("")) {
                Log.d(TAG, "mapToBundle: empty key, skipping");
                continue;
            }

            Object object = map.get(key);
            if (object instanceof Integer) {
                printLogMessage(key, int.class, object);
                bundle.putInt(key, (int) object);
                continue;
            }
            if (object instanceof Double) {
                printLogMessage(key, double.class, object);
                bundle.putDouble(key, (double) object);
                continue;
            }
            if (object instanceof Float) {
                printLogMessage(key, float.class, object);
                bundle.putFloat(key, (float) object);
                continue;
            }
            if (object instanceof String) {
                printLogMessage(key, String.class, object);
                bundle.putString(key, (String) object);
                continue;
            }
            if (object instanceof Short) {
                printLogMessage(key, short.class, object);
                bundle.putShort(key, (short) object);
                continue;
            }
            if (object instanceof Long) {
                printLogMessage(key, long.class, object);
                bundle.putLong(key, (long) object);
                continue;
            }
            if (object instanceof Boolean) {
                printLogMessage(key, boolean.class, object);
                bundle.putBoolean(key, (boolean) object);
                continue;
            }
            Log.d(TAG, "mapToBundle: key " + key + " has unsupported type");
        }
        return bundle;
    }

    private void printLogMessage(String key, Class clazz, Object object) {
        Log.d(TAG, "mapToBundle: key "
                + key
                + ", class "
                + clazz.getName()
                + " , object "
                + object.toString());
    }
}
