package com.gibanator.dailystepbackendjava.target.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TargetResponse(
        Long id,
        String name,
        int days,
        int daysSelected,
        boolean completed,
        LocalDate deadline,
        LocalDateTime createdAt
) {
}
