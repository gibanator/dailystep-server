package com.gibanator.dailystepbackendjava.target.exception;

public class TargetNotFoundException extends RuntimeException {

    public TargetNotFoundException(Long id) {
        super("Target with id " + id + " not found.");
    }
}
