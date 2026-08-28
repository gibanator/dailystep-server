package com.gibanator.dailystepbackendjava.sync.push.dto;

import java.util.List;

public record SyncPushRequestDto(
        List<SyncCategoryMutationDto> categories,
        List<SyncDailyProgressMutationDto> dailyProgress,
        List<SyncDayCompletionMutationDto> dayCompletions,
        List<SyncTargetMutationDto> targets,
        List<SyncTargetSelectionMutationDto> targetSelections
) {
    public SyncPushRequestDto {
        categories = categories == null ? List.of() : categories;
        dailyProgress = dailyProgress == null ? List.of() : dailyProgress;
        dayCompletions = dayCompletions == null ? List.of() : dayCompletions;
        targets = targets == null ? List.of() : targets;
        targetSelections = targetSelections == null ? List.of() : targetSelections;
    }
}
