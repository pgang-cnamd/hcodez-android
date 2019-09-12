package com.hcodez.android.services.content;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactOpener implements ContentOpener {

    private static final String TAG = "ContactOpener";

    private final Context context;

    private final Uri uri;

    public ContactOpener(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;
    }

    @Override
    public Intent getOpenerIntent() {

        Intent intent = null;
        Cursor cursor;
        String phoneNumber;
        try {
            cursor = context.getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNumber = cursor.getString(phoneIndex);
            cursor.close();

            intent = new IntentOpener(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber))
                    .getOpenerIntent();
        } catch (NullPointerException npe) {
            Log.e(TAG, "getOpenerIntent: error", npe);
            npe.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "getOpenerIntent: error getting contact", e);
            e.printStackTrace();
        }
        return intent;
    }
}
