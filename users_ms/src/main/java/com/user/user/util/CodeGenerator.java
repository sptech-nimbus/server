package com.user.user.util;

import java.util.UUID;

public class CodeGenerator {
    public static String codeGen(Integer numerOfChars, Boolean upperCase) {
        String code = UUID.randomUUID().toString().substring(0, numerOfChars);
        return upperCase
                ? code.toUpperCase()
                : code;
    }
}
