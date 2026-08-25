package com.gibanator.dailystepbackendjava.auth;

import com.gibanator.dailystepbackendjava.auth.dto.UserResponse;
import com.gibanator.dailystepbackendjava.auth.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {

    @GetMapping
    public UserResponse me(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return new UserResponse(
                user.getId(),
                user.getFirebaseUid(),
                user.getEmail()
        );
    }

}
