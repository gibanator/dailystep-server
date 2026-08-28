package com.gibanator.dailystepbackendjava.sync.push.dto;

import java.time.LocalDate;
import java.util.UUID;

public record SyncDailyProgressMutationDto(
        LocalDate date,

        UUID categoryId,

        boolean completed,
        String comment
) {
}
