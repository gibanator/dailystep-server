package com.gibanator.dailystepbackendjava.sync.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record SyncTargetDto(
        UUID id,
        String name,
        int days,
        int daysSelected,
        boolean completed,
        LocalDate deadline,
        boolean deleted,
        Instant createdAt,
        Instant updatedAt
) {
}
