package com.gibanator.dailystepbackendjava.daycompletion;

import com.gibanator.dailystepbackendjava.auth.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/day-completion")
public class DayCompletionController {
    private final DayCompletionService service;

    @PostMapping
    public ResponseEntity<Void> markCompleted(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam LocalDate date
    ) {
        service.markCompleted(user.getId(), date);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unmarkCompleted(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam LocalDate date
    ) {
        service.unmarkCompleted(user.getId(), date);
        return ResponseEntity.noContent().build();
    }
}
