package com.gibanator.dailystepbackendjava.dailycategoryprogress;

import com.gibanator.dailystepbackendjava.auth.security.UserPrincipal;
import com.gibanator.dailystepbackendjava.dailycategoryprogress.dto.DailyProgressResponse;
import com.gibanator.dailystepbackendjava.dailycategoryprogress.dto.SaveDailyProgressRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/progress")
@RequiredArgsConstructor
public class DailyCategoryProgressController {
    private final DailyCategoryProgressService service;

    @PostMapping
    public ResponseEntity<?> saveProgressForDay(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody SaveDailyProgressRequest req
            ){

        service.saveDailyProgress(user.getId(), req);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<DailyProgressResponse> getProgressForDay(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam LocalDate date
            ){

        return ResponseEntity.ok(
                service.getDailyProgress(user.getId(), date)
        );

    }
}
