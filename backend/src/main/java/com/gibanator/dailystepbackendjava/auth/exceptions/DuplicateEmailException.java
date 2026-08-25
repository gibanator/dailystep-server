package com.gibanator.dailystepbackendjava.auth.exceptions;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("Email '" + email + "' already exists in the database.");
    }
}
