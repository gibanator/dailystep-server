package com.gibanator.dailystepbackendjava.sync;

import com.gibanator.dailystepbackendjava.auth.security.UserPrincipal;
import com.gibanator.dailystepbackendjava.sync.dto.SyncSnapshotDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sync")
@RequiredArgsConstructor
public class SyncController {

    private final SyncService syncService;

    @GetMapping("/snapshot")
    public ResponseEntity<SyncSnapshotDto> snapshot(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok(syncService.snapshot(user.getId()));
    }
}
