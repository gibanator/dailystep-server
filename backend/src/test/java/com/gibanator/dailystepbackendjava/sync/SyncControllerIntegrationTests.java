package com.gibanator.dailystepbackendjava.sync;

import com.gibanator.dailystepbackendjava.auth.security.UserPrincipal;
import com.gibanator.dailystepbackendjava.category.CategoryEntity;
import com.gibanator.dailystepbackendjava.category.CategoryService;
import com.gibanator.dailystepbackendjava.dailycategoryprogress.DailyCategoryProgressService;
import com.gibanator.dailystepbackendjava.dailycategoryprogress.dto.SaveDailyProgressRequest;
import com.gibanator.dailystepbackendjava.daycompletion.DayCompletionService;
import com.gibanator.dailystepbackendjava.target.TargetService;
import com.gibanator.dailystepbackendjava.target.dto.CreateTargetRequest;
import com.gibanator.dailystepbackendjava.target.dto.TargetResponse;
import com.gibanator.dailystepbackendjava.user.UserEntity;
import com.gibanator.dailystepbackendjava.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SyncControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DailyCategoryProgressService dailyProgressService;

    @Autowired
    private DayCompletionService dayCompletionService;

    @Autowired
    private TargetService targetService;

    private UserEntity user;
    private UsernamePasswordAuthenticationToken authentication;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setEmail("sync-user@example.com");
        user.setFirebaseUid("sync-user-firebase-id");
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
    void returnsFullSnapshotAndDeletedCategoryMarkerForAuthenticatedUser() throws Exception {
        LocalDate date = LocalDate.of(2026, 8, 6);
        CategoryEntity activeCategory = categoryService.create(user.getId(), "Sport");
        CategoryEntity deletedCategory = categoryService.create(user.getId(), "Old habit");

        dailyProgressService.saveDailyProgress(
                user.getId(),
                new SaveDailyProgressRequest(
                        date,
                        List.of(
                                new SaveDailyProgressRequest.Item(activeCategory.getId(), true, "Run"),
                                new SaveDailyProgressRequest.Item(deletedCategory.getId(), true, "Archive me")
                        )
                )
        );
        categoryService.delete(deletedCategory.getId(), user.getId());

        dayCompletionService.markCompleted(user.getId(), date);
        TargetResponse target = targetService.create(
                user.getId(),
                new CreateTargetRequest("Read", 3, null)
        );
        targetService.select(user.getId(), target.id(), date);

        UserEntity otherUser = new UserEntity();
        otherUser.setEmail("other-sync-user@example.com");
        otherUser.setFirebaseUid("other-sync-user-firebase-id");
        otherUser = userRepository.save(otherUser);
        categoryService.create(otherUser.getId(), "Other user category");

        mockMvc.perform(get("/api/v1/sync/snapshot")
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories", hasSize(2)))
                .andExpect(jsonPath("$.categories[*].id",
                        containsInAnyOrder(activeCategory.getId().intValue(), deletedCategory.getId().intValue())))
                .andExpect(jsonPath("$.categories[?(@.id == %d)].deleted", deletedCategory.getId()).value(true))
                .andExpect(jsonPath("$.categories[?(@.id == %d)].deleted", activeCategory.getId()).value(false))
                .andExpect(jsonPath("$.dailyProgress", hasSize(2)))
                .andExpect(jsonPath("$.dayCompletions", hasSize(1)))
                .andExpect(jsonPath("$.dayCompletions[0].date").value(date.toString()))
                .andExpect(jsonPath("$.dayCompletions[0].completed").value(true))
                .andExpect(jsonPath("$.commentTemplates", hasSize(0)))
                .andExpect(jsonPath("$.targets", hasSize(1)))
                .andExpect(jsonPath("$.targets[0].id").value(target.id()))
                .andExpect(jsonPath("$.targetSelections", hasSize(1)))
                .andExpect(jsonPath("$.targetSelections[0].targetId").value(target.id()))
                .andExpect(jsonPath("$.targetSelections[0].date").value(date.toString()))
                .andExpect(jsonPath("$.targetSelections[0].selected").value(true));
    }
}
