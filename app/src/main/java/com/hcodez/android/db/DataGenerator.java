package com.hcodez.android.db;

import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.db.entity.ContentEntity;
import com.hcodez.codeengine.model.CodeType;

import org.joda.time.Instant;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class DataGenerator {

    private static class CodePrepopulateValues {
        public static CodeType CODE_TYPE = CodeType.PRIVATE;
        public static Instant CREATE_TIME = Instant.now();
        public static int ID = 0;
        public static String IDENTIFIER = "aB12";
        public static String NAME = "default";
        public static String OWNER = "owner";
        public static String PASSCODE = "DefaultPasscode123";
        public static Instant UPDATE_TIME = Instant.now();
        static final int CONTENT_ID = 0;
    }

    private static class ContentPrepopulateValues {
        static final int ID = 0;
        static final String DESCRIPTION = "A dummy content";
        static final URI RESOURCE_URI = URI.create("https://example.com");
    }

    static List<CodeEntity> generateCodes() {
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
        List <ContentEntity> contentList = new ArrayList<>(1);

        final ContentEntity contentEntity = new ContentEntity();

        contentEntity.setId(ContentPrepopulateValues.ID);
        contentEntity.setDescription(ContentPrepopulateValues.DESCRIPTION);
        contentEntity.setResourceURI(ContentPrepopulateValues.RESOURCE_URI);
        contentList.add(contentEntity);

        return contentList;
    }
}
