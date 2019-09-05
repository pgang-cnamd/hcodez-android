package com.hcodez.android.db;

import android.net.Uri;
import android.util.Log;

import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.db.entity.ContentEntity;
import com.hcodez.codeengine.model.CodeType;

import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.List;

class DataGenerator {

    private static final String TAG = "DataGenerator";

    private static class CodePrepopulateValues {
        public static CodeType CODE_TYPE = CodeType.PRIVATE;
        public static Instant CREATE_TIME = Instant.now();
        public static Integer ID = 0;
        public static String IDENTIFIER = "aB12";
        public static String NAME = "default";
        public static String OWNER = "owner";
        public static String PASSCODE = "DefaultPasscode123";
        public static Instant UPDATE_TIME = Instant.now();
        static final Integer CONTENT_ID = ContentPrepopulateValues.ID;
    }

    private static class ContentPrepopulateValues {
        static final Integer ID = 0;
        static final String DESCRIPTION = "A dummy content";
        static final Uri RESOURCE_URI = Uri.parse("https://example.com");
    }

    static List<CodeEntity> generateCodes() {
        Log.d(TAG, "generateCodes() called");

        List<CodeEntity> codes = new ArrayList<>(1);

        final CodeEntity code = new CodeEntity();

        code.setCodeType(CodePrepopulateValues.CODE_TYPE);
        code.setCreateTime(CodePrepopulateValues.CREATE_TIME);
        code.setId(CodePrepopulateValues.ID);
        code.setIdentifier(CodePrepopulateValues.IDENTIFIER);
        code.setName(CodePrepopulateValues.NAME);
        code.setOwner(CodePrepopulateValues.OWNER);
        code.setPasscode(CodePrepopulateValues.PASSCODE);
        code.setUpdateTime(CodePrepopulateValues.UPDATE_TIME);
        code.setContentId(CodePrepopulateValues.CONTENT_ID);

        codes.add(code);

        return codes;
    }

    static List<ContentEntity> generateContent() {
        Log.d(TAG, "generateContent() called");

        List <ContentEntity> contentList = new ArrayList<>(1);

        final ContentEntity contentEntity = new ContentEntity();

        contentEntity.setId(ContentPrepopulateValues.ID);
        contentEntity.setDescription(ContentPrepopulateValues.DESCRIPTION);
        contentEntity.setResourceURI(ContentPrepopulateValues.RESOURCE_URI);
        contentList.add(contentEntity);

        return contentList;
    }
}
