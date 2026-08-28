package com.gibanator.dailystepbackendjava.sync.push;

import com.gibanator.dailystepbackendjava.auth.exceptions.UserNotFoundException;
import com.gibanator.dailystepbackendjava.category.CategoryEntity;
import com.gibanator.dailystepbackendjava.category.CategoryRepository;
import com.gibanator.dailystepbackendjava.category.exception.CategoryNotFoundException;
import com.gibanator.dailystepbackendjava.dailycategoryprogress.DailyCategoryProgressEntity;
import com.gibanator.dailystepbackendjava.dailycategoryprogress.DailyCategoryProgressId;
import com.gibanator.dailystepbackendjava.dailycategoryprogress.DailyCategoryProgressRepository;
import com.gibanator.dailystepbackendjava.daycompletion.DayCompletionEntity;
import com.gibanator.dailystepbackendjava.daycompletion.DayCompletionId;
import com.gibanator.dailystepbackendjava.daycompletion.DayCompletionRepository;
import com.gibanator.dailystepbackendjava.sync.push.dto.*;
import com.gibanator.dailystepbackendjava.target.TargetEntity;
import com.gibanator.dailystepbackendjava.target.TargetRepository;
import com.gibanator.dailystepbackendjava.target.exception.TargetNotFoundException;
import com.gibanator.dailystepbackendjava.targetselection.TargetSelectionEntity;
import com.gibanator.dailystepbackendjava.targetselection.TargetSelectionId;
import com.gibanator.dailystepbackendjava.targetselection.TargetSelectionRepository;
import com.gibanator.dailystepbackendjava.user.UserEntity;
import com.gibanator.dailystepbackendjava.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PushSyncService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final DailyCategoryProgressRepository dailyProgressRepository;
    private final DayCompletionRepository dayCompletionRepository;
    private final TargetRepository targetRepository;
    private final TargetSelectionRepository targetSelectionRepository;


    @Transactional

    public void push(Long userId, SyncPushRequestDto request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Parents first because progress/selections can reference them.

        pushCategories(user, request.categories());
        pushTargets(user, request.targets());
        pushDailyProgress(user, request.dailyProgress());
        pushDayCompletions(user, request.dayCompletions());
        pushTargetSelections(user, request.targetSelections());

    }

    private void pushCategories(
            UserEntity user,
            List<SyncCategoryMutationDto> mutations
    ) {
        for (SyncCategoryMutationDto mutation : mutations) {
            CategoryEntity category = categoryRepository
                    .findByIdAndUserId(mutation.id(), user.getId())
                    .orElseGet(() -> {
                        CategoryEntity entity = new CategoryEntity();
                        entity.setId(mutation.id());
                        entity.setUser(user);
                        return entity;
                    });
            category.setName(mutation.name());
            category.setNameKey(mutation.nameKey());
            category.setSortOrder(mutation.sortOrder());
            category.setActive(mutation.active());
            category.setSystem(mutation.system());
            category.setVisible(mutation.visible());
            category.setDeleted(mutation.deleted());
            categoryRepository.save(category);
        }
    }

    private void pushTargets(
            UserEntity user,
            List<SyncTargetMutationDto> mutations
    ) {
        for (SyncTargetMutationDto mutation : mutations) {
            TargetEntity target = targetRepository
                    .findByIdAndUserId(mutation.id(), user.getId())
                    .orElseGet(() -> {
                        TargetEntity entity = new TargetEntity();
                        entity.setId(mutation.id());
                        entity.setUser(user);
                        return entity;
                    });
            target.setName(mutation.name());
            target.setDays(mutation.days());
            target.setDaysSelected(mutation.daysSelected());
            target.setCompleted(mutation.completed());
            target.setDeadline(mutation.deadline());
            target.setDeleted(mutation.deleted());
            targetRepository.save(target);
        }
    }

    private void pushDailyProgress(
            UserEntity user,
            List<SyncDailyProgressMutationDto> mutations
    ) {
        for (SyncDailyProgressMutationDto mutation : mutations) {
            CategoryEntity category = categoryRepository
                    .findByIdAndUserId(mutation.categoryId(), user.getId())
                    .orElseThrow(() -> new CategoryNotFoundException(
                            mutation.categoryId()
                    ));

            DailyCategoryProgressId id = new DailyCategoryProgressId(
                    mutation.date(),
                    mutation.categoryId()
            );

            DailyCategoryProgressEntity progress = dailyProgressRepository
                    .findById(id)
                    .orElseGet(() -> {
                        DailyCategoryProgressEntity entity =
                                new DailyCategoryProgressEntity();
                        entity.setId(id);
                        entity.setCategory(category);

                        return entity;
                    });
            progress.setCompleted(mutation.completed());
            progress.setComment(mutation.comment());
            dailyProgressRepository.save(progress);
        }
    }

    private void pushDayCompletions(
            UserEntity user,
            List<SyncDayCompletionMutationDto> mutations
    ) {
        for (SyncDayCompletionMutationDto mutation : mutations) {
            DayCompletionId id = new DayCompletionId(
                    user.getId(),
                    mutation.date()
            );

            DayCompletionEntity completion = dayCompletionRepository
                    .findById(id)
                    .orElseGet(() -> {
                        DayCompletionEntity entity = new DayCompletionEntity();
                        entity.setId(id);
                        entity.setUser(user);

                        return entity;
                    });

            completion.setDeleted(mutation.deleted());
            dayCompletionRepository.save(completion);
        }
    }

    private void pushTargetSelections(
            UserEntity user,
            List<SyncTargetSelectionMutationDto> mutations
    ) {

        for (SyncTargetSelectionMutationDto mutation : mutations) {

            TargetEntity target = targetRepository
                    .findByIdAndUserId(mutation.targetId(), user.getId())
                    .orElseThrow(() -> new TargetNotFoundException(
                            mutation.targetId()
                    ));

            TargetSelectionId id = new TargetSelectionId(
                    mutation.targetId(),
                    mutation.date()
            );

            TargetSelectionEntity selection = targetSelectionRepository
                    .findById(id)
                    .orElseGet(() -> {
                        TargetSelectionEntity entity =
                                new TargetSelectionEntity();
                        entity.setId(id);
                        entity.setTarget(target);
                        return entity;
                    });
            selection.setDeleted(mutation.deleted());
            targetSelectionRepository.save(selection);
        }
    }
}