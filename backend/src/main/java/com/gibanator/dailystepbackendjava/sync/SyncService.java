package com.gibanator.dailystepbackendjava.sync;

import com.gibanator.dailystepbackendjava.category.CategoryEntity;
import com.gibanator.dailystepbackendjava.category.CategoryRepository;
import com.gibanator.dailystepbackendjava.dailycategoryprogress.DailyCategoryProgressEntity;
import com.gibanator.dailystepbackendjava.dailycategoryprogress.DailyCategoryProgressRepository;
import com.gibanator.dailystepbackendjava.daycompletion.DayCompletionEntity;
import com.gibanator.dailystepbackendjava.daycompletion.DayCompletionRepository;
import com.gibanator.dailystepbackendjava.sync.dto.*;
import com.gibanator.dailystepbackendjava.target.TargetEntity;
import com.gibanator.dailystepbackendjava.target.TargetRepository;
import com.gibanator.dailystepbackendjava.targetselection.TargetSelectionEntity;
import com.gibanator.dailystepbackendjava.targetselection.TargetSelectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SyncService {

    private final CategoryRepository categoryRepository;
    private final DailyCategoryProgressRepository dailyProgressRepository;
    private final DayCompletionRepository dayCompletionRepository;
    private final TargetRepository targetRepository;
    private final TargetSelectionRepository targetSelectionRepository;

    @Transactional(readOnly = true)
    public SyncResponseDto pull(Long userId, Instant since) {
        boolean fullSync = since == null;

        Instant serverTime = Instant.now();

        List<CategoryEntity> categories = fullSync
                ? categoryRepository.findAllByUserIdOrderBySortOrderAscCreatedAtAsc(userId)
                : categoryRepository.findChangedSince(userId, since);

        List<DailyCategoryProgressEntity> progress = fullSync
                ? dailyProgressRepository.findAllByUserId(userId)
                : dailyProgressRepository.findChangedSince(userId, since);

        List<DayCompletionEntity> dayCompletions = fullSync
                ? dayCompletionRepository.findAllByIdUserIdOrderByIdDateAsc(userId)
                : dayCompletionRepository.findChangedSince(userId, since);

        List<TargetEntity> targets = fullSync
                ? targetRepository.findAllByUserIdOrderByCreatedAtAsc(userId)
                : targetRepository.findChangedSince(userId, since);

        List<TargetSelectionEntity> targetSelections = fullSync
                ? targetSelectionRepository.findAllByUserId(userId)
                : targetSelectionRepository.findChangedSince(userId, since);

        return new SyncResponseDto(
                categories.stream().map(this::toCategoryDto).toList(),
                progress.stream().map(this::toDailyProgressDto).toList(),
                dayCompletions.stream().map(this::toDayCompletionDto).toList(),
                List.of(),
                targets.stream().map(this::toTargetDto).toList(),
                targetSelections.stream().map(this::toTargetSelectionDto).toList(),
                serverTime
        );
    }

    private SyncCategoryDto toCategoryDto(CategoryEntity category) {
        return new SyncCategoryDto(
                category.getId(),
                category.getName(),
                category.getNameKey(),
                category.getSortOrder(),
                category.isActive(),
                category.isSystem(),
                category.isVisible(),
                category.isDeleted(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    private SyncDailyProgressDto toDailyProgressDto(DailyCategoryProgressEntity progress) {
        return new SyncDailyProgressDto(
                progress.getId().getDate(),
                progress.getId().getCategoryId(),
                progress.isCompleted(),
                progress.getComment(),
                progress.getUpdatedAt()
        );
    }

    private SyncDayCompletionDto toDayCompletionDto(DayCompletionEntity completion) {
        return new SyncDayCompletionDto(
                completion.getId().getDate(),
                completion.isDeleted(),
                completion.getUpdatedAt()
        );
    }

    private SyncTargetDto toTargetDto(TargetEntity target) {
        return new SyncTargetDto(
                target.getId(),
                target.getName(),
                target.getDays(),
                target.getDaysSelected(),
                target.isCompleted(),
                target.getDeadline(),
                target.isDeleted(),
                target.getCreatedAt(),
                target.getUpdatedAt()
        );
    }

    private SyncTargetSelectionDto toTargetSelectionDto(TargetSelectionEntity selection) {
        return new SyncTargetSelectionDto(
                selection.getId().getTargetId(),
                selection.getId().getDate(),
                selection.isDeleted(),
                selection.getUpdatedAt()
        );
    }


}
