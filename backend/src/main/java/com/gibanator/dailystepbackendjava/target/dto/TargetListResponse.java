package com.gibanator.dailystepbackendjava.target.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TargetListResponse(
        LocalDate date,
        List<TargetResponse> targets,
        List<UUID> selectedTargetIds
) {
}
