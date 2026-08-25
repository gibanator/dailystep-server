package com.gibanator.dailystepbackendjava.target.dto;

import java.time.LocalDate;

public record TargetSelectionResponse(
        Long targetId,
        LocalDate date,
        boolean selected,
        int daysSelected
) {
}
