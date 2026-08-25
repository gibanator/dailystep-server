package com.gibanator.dailystepbackendjava.sync.dto;

import java.time.LocalDate;

public record SyncTargetSelectionDto(
        Long targetId,
        LocalDate date,
        boolean selected
) {
}
