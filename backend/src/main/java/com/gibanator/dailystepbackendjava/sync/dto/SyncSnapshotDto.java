package com.gibanator.dailystepbackendjava.sync.dto;

import java.util.List;

public record SyncSnapshotDto(
        List<SyncCategoryDto> categories,
        List<SyncDailyProgressDto> dailyProgress,
        List<SyncDayCompletionDto> dayCompletions,
        List<SyncCommentTemplateDto> commentTemplates,
        List<SyncTargetDto> targets,
        List<SyncTargetSelectionDto> targetSelections
) {
}
