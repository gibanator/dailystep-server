package com.gibanator.dailystepbackendjava.sync.push.dto;

import java.time.LocalDate;
import java.util.UUID;

public record SyncTargetSelectionMutationDto(
        LocalDate date,

        UUID targetId,

        boolean deleted
) {
}
