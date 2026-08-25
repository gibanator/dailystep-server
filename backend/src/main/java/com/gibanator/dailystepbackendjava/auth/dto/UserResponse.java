package com.gibanator.dailystepbackendjava.auth.dto;

public record UserResponse(
        Long id,
        String firebaseUid,
        String email
) {
}