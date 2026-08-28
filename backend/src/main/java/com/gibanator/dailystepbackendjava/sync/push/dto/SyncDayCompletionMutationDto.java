package com.gibanator.dailystepbackendjava.sync.push.dto;

import java.time.LocalDate;

public record SyncDayCompletionMutationDto(
        LocalDate date,
        boolean deleted
) {
}
