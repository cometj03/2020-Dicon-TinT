package com.sunrin.tint.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckString {

    public static boolean isValidEmail(String email) {
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);

        return m.matches();
    }
}
