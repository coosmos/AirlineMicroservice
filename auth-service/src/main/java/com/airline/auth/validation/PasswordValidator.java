package com.airline.auth.validation;

public class PasswordValidator {

    private static final String PASSWORD_REGEX = "^(?=.*\\d).{6,}$";

    public static boolean isValid(String password) {
        return password != null && password.matches(PASSWORD_REGEX);
    }
}
