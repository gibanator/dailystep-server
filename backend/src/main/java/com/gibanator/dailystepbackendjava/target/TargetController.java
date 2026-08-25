package com.gibanator.dailystepbackendjava.target;

import com.gibanator.dailystepbackendjava.auth.security.UserPrincipal;
import com.gibanator.dailystepbackendjava.target.dto.CreateTargetRequest;
import com.gibanator.dailystepbackendjava.target.dto.TargetListResponse;
import com.gibanator.dailystepbackendjava.target.dto.TargetResponse;
import com.gibanator.dailystepbackendjava.target.dto.TargetSelectionHistoryResponse;
import com.gibanator.dailystepbackendjava.target.dto.TargetSelectionResponse;
import com.gibanator.dailystepbackendjava.target.dto.UpdateTargetRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/targets")
@RequiredArgsConstructor
public class TargetController {

    private final TargetService service;

    @GetMapping
    public ResponseEntity<TargetListResponse> findAllForDate(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(service.findAllForDate(user.getId(), date));
    }

    @PostMapping
    public ResponseEntity<TargetResponse> create(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody CreateTargetRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(user.getId(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TargetResponse> update(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody UpdateTargetRequest request
    ) {
        return ResponseEntity.ok(service.update(user.getId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        service.delete(user.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/selections/{date}")
    public ResponseEntity<TargetSelectionResponse> select(
            @PathVariable Long id,
            @PathVariable LocalDate date,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok(service.select(user.getId(), id, date));
    }

    @DeleteMapping("/{id}/selections/{date}")
    public ResponseEntity<TargetSelectionResponse> deselect(
            @PathVariable Long id,
            @PathVariable LocalDate date,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok(service.deselect(user.getId(), id, date));
    }

    @GetMapping("/{id}/selections")
    public ResponseEntity<TargetSelectionHistoryResponse> getSelectionHistory(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok(service.getSelectionHistory(user.getId(), id));
    }
}
