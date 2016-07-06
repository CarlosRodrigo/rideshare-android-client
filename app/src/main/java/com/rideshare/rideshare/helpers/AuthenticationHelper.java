package com.rideshare.rideshare.helpers;

import android.text.TextUtils;

public class AuthenticationHelper {

    public static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

}
