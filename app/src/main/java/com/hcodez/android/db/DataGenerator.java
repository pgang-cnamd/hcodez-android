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

    private static class CodePrepoulateValues {
        static final CodeType CODE_TYPE = CodeType.PRIVATE;
        static final Instant CREATE_TIME = Instant.now();
        static final int ID = 0;
        static final String IDENTIFIER = "aB12";
        static final String NAME = "default";
        static final String OWNER = "owner";
        static final String PASSCODE = "DefaultPasscode123";
        static final Instant UPDATE_TIME = Instant.now();
        static final URL mURL;
        static {
            URL mURL1;
            try {
                mURL1 = new URL("https://www.example.com");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                mURL1 = null;
            }
            mURL = mURL1;
        }
        static final int CONTENT_ID = 0;
    }

    private static class ContentPrepopulateValues {
        static final int ID = 0;
        static final String DESCRIPTION = "A dummy content";
        static final URI RESOURCE_URI = URI.create("https://example.com");
        static final int CODE_ID = 0;
    }

    static List<CodeEntity> generateCodes() {
        List<CodeEntity> codes = new ArrayList<>(1);

        final CodeEntity code = new CodeEntity();

        code.setCodeType(CodePrepoulateValues.CODE_TYPE);
        code.setCreateTime(CodePrepoulateValues.CREATE_TIME);
        code.setId(CodePrepoulateValues.ID);
        code.setIdentifier(CodePrepoulateValues.IDENTIFIER);
        code.setName(CodePrepoulateValues.NAME);
        code.setOwner(CodePrepoulateValues.OWNER);
        code.setPasscode(CodePrepoulateValues.PASSCODE);
        code.setUpdateTime(CodePrepoulateValues.UPDATE_TIME);
        code.setUrl(CodePrepoulateValues.mURL);
        code.setContentId(CodePrepoulateValues.CONTENT_ID);

        codes.add(code);

        return codes;
    }

    static List<ContentEntity> generateContent() {
        List <ContentEntity> contentList = new ArrayList<>(1);

        final ContentEntity contentEntity = new ContentEntity();

        contentEntity.setId(ContentPrepopulateValues.ID);
        contentEntity.setDescription(ContentPrepopulateValues.DESCRIPTION);
        contentEntity.setResourceURI(ContentPrepopulateValues.RESOURCE_URI);
        contentEntity.setCodeId(ContentPrepopulateValues.CODE_ID);

        contentList.add(contentEntity);

        return contentList;
    }
}
