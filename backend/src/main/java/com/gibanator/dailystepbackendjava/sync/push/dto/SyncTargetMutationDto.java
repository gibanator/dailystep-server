package com.gibanator.dailystepbackendjava.sync.push.dto;

import java.time.LocalDate;
import java.util.UUID;

public record SyncTargetMutationDto(
        UUID id,
        String name,
        int days,
        int daysSelected,
        boolean completed,
        LocalDate deadline,
        boolean deleted
) {
}
