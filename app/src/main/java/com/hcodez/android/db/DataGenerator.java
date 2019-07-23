package com.hcodez.android.db;

import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.codeengine.model.CodeType;

import org.joda.time.Instant;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataGenerator {

    private static class CodePrepoulateValues {
        public static CodeType CODE_TYPE = CodeType.PRIVATE;
        public static Instant CREATE_TIME = Instant.now();
        public static int ID = 0;
        public static String IDENTIFIER = "aB12";
        public static String NAME = "default";
        public static String OWNER = "owner";
        public static String PASSCODE = "DefaultPasscode123";
        public static Instant UPDATE_TIME = Instant.now();
        public static URL mURL;
        static {
            try {
                mURL = new URL("https://www.example.com");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
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

        return codes;
    }
}
