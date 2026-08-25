package com.gibanator.dailystepbackendjava.target.dto;

import java.time.LocalDate;
import java.util.List;

public record TargetSelectionHistoryResponse(
        Long targetId,
        List<LocalDate> dates
) {
}
