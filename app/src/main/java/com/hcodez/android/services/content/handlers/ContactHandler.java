package com.hcodez.android.services.content.handlers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.hcodez.android.services.content.ContentHandler;

public class ContactHandler implements ContentHandler {

    private static final String TAG = "ContactHandler";

    private final Context context;

    private final Uri uri;

    public ContactHandler(Context context, Uri uri) {
        Log.d(TAG, "ContactHandler() called with: context = [" + context + "], uri = [" + uri + "]");
        this.context = context;
        this.uri = uri;
    }

    @Override
    public Intent getOpenerIntent() {
        Log.d(TAG, "getOpenerIntent() called");

        Intent intent = null;
        Cursor cursor;
        String phoneNumber;
        try {
            cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
            } else {
                Log.e(TAG, "getOpenerIntent: null cursor");
                return null;
            }
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNumber = cursor.getString(phoneIndex);
            cursor.close();

            intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        } catch (NullPointerException npe) {
            Log.e(TAG, "getOpenerIntent: error", npe);
            npe.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "getOpenerIntent: error getting contact", e);
            e.printStackTrace();
        }
        return intent;
    }

    @Override
    public Intent getCreatorIntent() {
        Log.d(TAG, "getCreatorIntent() called");
        return new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts")).setType("vnd.android.cursor.dir/phone_v2");
    }
}
