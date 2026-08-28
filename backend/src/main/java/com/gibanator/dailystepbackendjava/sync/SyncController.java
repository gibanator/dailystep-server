package com.gibanator.dailystepbackendjava.sync;

import com.gibanator.dailystepbackendjava.auth.security.UserPrincipal;
import com.gibanator.dailystepbackendjava.sync.dto.SyncResponseDto;
import com.gibanator.dailystepbackendjava.sync.push.PushSyncService;
import com.gibanator.dailystepbackendjava.sync.push.dto.SyncPushRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/sync")
@RequiredArgsConstructor
public class SyncController {

    private final SyncService syncService;
    private final PushSyncService pushSyncService;

    @GetMapping
    public ResponseEntity<SyncResponseDto> sync(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(required = false) Instant since
    ) {
        return ResponseEntity.ok(
                syncService.pull(user.getId(), since)
        );
    }

    @PostMapping
    public ResponseEntity<Void> push(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody SyncPushRequestDto request
    ) {
        pushSyncService.push(user.getId(), request);
        return ResponseEntity.noContent().build();
    }
}
