package com.gibanator.dailystepbackendjava.sync.dto;

import java.time.LocalDate;
import java.time.Instant;

public record SyncDayCompletionDto(
        LocalDate date,
        boolean deleted,
        Instant updatedAt
) {
}
