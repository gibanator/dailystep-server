package com.gibanator.dailystepbackendjava.auth.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPrincipal {
    private Long id;
    private String firebaseUid;
    private String email;
}
