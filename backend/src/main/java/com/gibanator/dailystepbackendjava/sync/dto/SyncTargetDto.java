package com.gibanator.dailystepbackendjava.sync.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SyncTargetDto(
        Long id,
        String name,
        int days,
        int daysSelected,
        boolean completed,
        LocalDate deadline,
        LocalDateTime createdAt
) {
}
