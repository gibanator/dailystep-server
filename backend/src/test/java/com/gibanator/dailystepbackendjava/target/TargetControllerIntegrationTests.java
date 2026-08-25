package com.gibanator.dailystepbackendjava.target;

import com.gibanator.dailystepbackendjava.auth.security.UserPrincipal;
import com.gibanator.dailystepbackendjava.user.UserEntity;
import com.gibanator.dailystepbackendjava.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TargetControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private UsernamePasswordAuthenticationToken authentication;

    @BeforeEach
    void setUp() {
        UserEntity user = new UserEntity();
        user.setEmail("target-controller-user@example.com");
        user.setFirebaseUid("target-controller-firebase-id");
        user = userRepository.save(user);

        UserPrincipal principal = new UserPrincipal(
                user.getId(),
                user.getFirebaseUid(),
                user.getEmail()
        );
        authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                Collections.emptyList()
        );
    }

    @Test
    void rejectsInvalidCreateRequest() throws Exception {
        mockMvc.perform(post("/api/v1/targets")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "   ",
                                  "days": 0,
                                  "deadline": null
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed."));
    }

    @Test
    void mapsMissingOrForeignTargetToNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/targets/999999/selections")
                        .with(authentication(authentication)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not found."))
                .andExpect(jsonPath("$.msg").value("Target with id 999999 not found."));
    }
}
