package com.gibanator.dailystepbackendjava.sync.dto;

import java.time.LocalDate;

public record SyncDailyProgressDto(
        LocalDate date,
        Long categoryId,
        boolean completed,
        String comment
) {
}
