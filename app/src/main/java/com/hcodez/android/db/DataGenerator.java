package com.hcodez.android.db;

import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.db.entity.ContentEntity;
import com.hcodez.codeengine.model.CodeType;

import org.joda.time.Instant;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataGenerator {

    private static class CodePrepoulateValues {
        public static final CodeType CODE_TYPE = CodeType.PRIVATE;
        public static final Instant CREATE_TIME = Instant.now();
        public static final int ID = 0;
        public static final String IDENTIFIER = "aB12";
        public static final String NAME = "default";
        public static final String OWNER = "owner";
        public static final String PASSCODE = "DefaultPasscode123";
        public static final Instant UPDATE_TIME = Instant.now();
        public static final URL mURL;
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
    }

    private static class ContentPrepopulateValues {
        // TODO: 2019-07-30 add dummy content
    }

    public static List<CodeEntity> generateCodes() {
        List<CodeEntity> codes = new ArrayList<>(1);

        CodeEntity code = new CodeEntity();

        code.setCodeType(CodePrepoulateValues.CODE_TYPE);
        code.setCreateTime(CodePrepoulateValues.CREATE_TIME);
        code.setId(CodePrepoulateValues.ID);
        code.setIdentifier(CodePrepoulateValues.IDENTIFIER);
        code.setName(CodePrepoulateValues.NAME);
        code.setOwner(CodePrepoulateValues.OWNER);
        code.setPasscode(CodePrepoulateValues.PASSCODE);
        code.setUpdateTime(CodePrepoulateValues.UPDATE_TIME);
        code.setUrl(CodePrepoulateValues.mURL);

        // FIXME: 2019-07-30 codes.add(code);

        return codes;
    }

    public static List<ContentEntity> generateContent() {
        List <ContentEntity> contentList = new ArrayList<>(1);
        // TODO: 2019-07-30 finish implementation
        return contentList;
    }
}
