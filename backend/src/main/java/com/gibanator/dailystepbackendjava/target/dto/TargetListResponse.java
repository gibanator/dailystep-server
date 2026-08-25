package com.gibanator.dailystepbackendjava.target.dto;

import java.time.LocalDate;
import java.util.List;

public record TargetListResponse(
        LocalDate date,
        List<TargetResponse> targets,
        List<Long> selectedTargetIds
) {
}
