package com.gibanator.dailystepbackendjava.sync.dto;

import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

public record SyncTargetSelectionDto(
        UUID targetId,
        LocalDate date,
        boolean deleted,
        Instant updatedAt
) {
}
