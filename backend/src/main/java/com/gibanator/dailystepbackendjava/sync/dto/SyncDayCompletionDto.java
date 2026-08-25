package com.gibanator.dailystepbackendjava.sync.dto;

import java.time.LocalDate;

public record SyncDayCompletionDto(
        LocalDate date,
        boolean completed
) {
}
