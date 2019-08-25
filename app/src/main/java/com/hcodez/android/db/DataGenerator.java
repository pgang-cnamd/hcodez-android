package com.hcodez.android.db;

import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.codeengine.model.CodeType;

import org.joda.time.Instant;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataGenerator {

    private static class CodePrepopulateValues {
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

        code.setCodeType(CodePrepopulateValues.CODE_TYPE);
        code.setCreateTime(CodePrepopulateValues.CREATE_TIME);
        code.setId(CodePrepopulateValues.ID);
        code.setIdentifier(CodePrepopulateValues.IDENTIFIER);
        code.setName(CodePrepopulateValues.NAME);
        code.setOwner(CodePrepopulateValues.OWNER);
        code.setPasscode(CodePrepopulateValues.PASSCODE);
        code.setUpdateTime(CodePrepopulateValues.UPDATE_TIME);
        code.setUrl(CodePrepopulateValues.mURL);

        return codes;
    }
}
