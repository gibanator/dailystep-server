package com.gibanator.dailystepbackendjava.sync;

import com.gibanator.dailystepbackendjava.category.CategoryEntity;
import com.gibanator.dailystepbackendjava.category.CategoryRepository;
import com.gibanator.dailystepbackendjava.dailycategoryprogress.DailyCategoryProgressEntity;
import com.gibanator.dailystepbackendjava.dailycategoryprogress.DailyCategoryProgressRepository;
import com.gibanator.dailystepbackendjava.daycompletion.DayCompletionEntity;
import com.gibanator.dailystepbackendjava.daycompletion.DayCompletionRepository;
import com.gibanator.dailystepbackendjava.sync.dto.SyncCategoryDto;
import com.gibanator.dailystepbackendjava.sync.dto.SyncCommentTemplateDto;
import com.gibanator.dailystepbackendjava.sync.dto.SyncDailyProgressDto;
import com.gibanator.dailystepbackendjava.sync.dto.SyncDayCompletionDto;
import com.gibanator.dailystepbackendjava.sync.dto.SyncSnapshotDto;
import com.gibanator.dailystepbackendjava.sync.dto.SyncTargetDto;
import com.gibanator.dailystepbackendjava.sync.dto.SyncTargetSelectionDto;
import com.gibanator.dailystepbackendjava.target.TargetEntity;
import com.gibanator.dailystepbackendjava.target.TargetRepository;
import com.gibanator.dailystepbackendjava.targetselection.TargetSelectionEntity;
import com.gibanator.dailystepbackendjava.targetselection.TargetSelectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public SyncSnapshotDto snapshot(Long userId) {
        return new SyncSnapshotDto(
                categoryRepository.findAllByUserIdOrderByIdAsc(userId).stream()
                        .map(this::toCategoryDto)
                        .toList(),
                dailyProgressRepository.findAllByUser(userId).stream()
                        .map(this::toDailyProgressDto)
                        .toList(),
                dayCompletionRepository.findAllByIdUserIdOrderByIdDateAsc(userId).stream()
                        .map(this::toDayCompletionDto)
                        .toList(),
                List.<SyncCommentTemplateDto>of(),
                targetRepository.findAllByUserIdOrderByIdAsc(userId).stream()
                        .map(this::toTargetDto)
                        .toList(),
                targetSelectionRepository.findAllByUserId(userId).stream()
                        .map(this::toTargetSelectionDto)
                        .toList()
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
                category.getCreatedAt()
        );
    }

    private SyncDailyProgressDto toDailyProgressDto(DailyCategoryProgressEntity progress) {
        return new SyncDailyProgressDto(
                progress.getId().getDate(),
                progress.getId().getCategoryId(),
                progress.isCompleted(),
                progress.getComment()
        );
    }

    private SyncDayCompletionDto toDayCompletionDto(DayCompletionEntity completion) {
        return new SyncDayCompletionDto(
                completion.getId().getDate(),
                true
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
                target.getCreatedAt()
        );
    }

    private SyncTargetSelectionDto toTargetSelectionDto(TargetSelectionEntity selection) {
        return new SyncTargetSelectionDto(
                selection.getId().getTargetId(),
                selection.getId().getDate(),
                true
        );
    }
}
