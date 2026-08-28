package com.gibanator.dailystepbackendjava.target.exception;

import java.util.UUID;

public class TargetNotFoundException extends RuntimeException {

    public TargetNotFoundException(UUID id) {
        super("Target with id " + id + " not found.");
    }
}
