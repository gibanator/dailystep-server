package com.gibanator.dailystepbackendjava.target.dto;

import java.time.LocalDate;
import java.util.UUID;

public record TargetSelectionResponse(
        UUID targetId,
        LocalDate date,
        boolean selected,
        int daysSelected
) {
}
