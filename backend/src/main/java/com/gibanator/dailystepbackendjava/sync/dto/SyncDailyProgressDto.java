package com.gibanator.dailystepbackendjava.sync.dto;

import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

public record SyncDailyProgressDto(
        LocalDate date,
        UUID categoryId,
        boolean completed,
        String comment,
        Instant updatedAt
) {
}
