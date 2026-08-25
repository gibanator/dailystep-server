package com.gibanator.dailystepbackendjava.dailycategoryprogress;

import com.gibanator.dailystepbackendjava.category.CategoryEntity;
import com.gibanator.dailystepbackendjava.category.CategoryRepository;
import com.gibanator.dailystepbackendjava.category.exception.CategoryNotFoundException;
import com.gibanator.dailystepbackendjava.dailycategoryprogress.dto.DailyProgressResponse;
import com.gibanator.dailystepbackendjava.dailycategoryprogress.dto.SaveDailyProgressRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DailyCategoryProgressService {

    private final DailyCategoryProgressRepository progressRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void saveDailyProgress(Long userId, SaveDailyProgressRequest req){
        if (req.items() == null || req.items().isEmpty()) {
            return;
        }

        // set of category ids in request
        Set<Long> categoryIds = req.items().stream()
                .map(SaveDailyProgressRequest.Item::categoryId)
                .collect(Collectors.toSet());

        // finding all categories from request in repository and then checking accordance
        List<CategoryEntity> categories = categoryRepository
                .findAllByIdInAndUserIdAndDeletedFalse(categoryIds, userId);

        if (categories.size() != categoryIds.size()) {
            throw new CategoryNotFoundException(categoryIds);
        }

        // category map for found categories type <id, CategoryEntity>
        Map<Long, CategoryEntity> categoryMap = categories.stream()
                .collect(Collectors.toMap(CategoryEntity::getId, Function.identity()));

        List<DailyCategoryProgressEntity> existingRows =
                progressRepository.findAllByUserAndDateAndCategoryIds(
                        req.date(),
                        userId,
                        categoryIds
                );

        Map<Long, DailyCategoryProgressEntity> existingMap = existingRows.stream()
                .collect(Collectors.toMap(
                        e -> e.getId().getCategoryId(),
                        Function.identity()
                ));

        List<DailyCategoryProgressEntity> toSave = new ArrayList<>();
        List<DailyCategoryProgressEntity> toDelete = new ArrayList<>();

        for (SaveDailyProgressRequest.Item i : req.items()){
            String comment = i.comment();
            if (comment != null && comment.isBlank()) {
                comment = null;
            }
            // item is saved only if there is a comment, or if it is active
            boolean shouldExist = i.completed() || comment != null;

            CategoryEntity category = categoryMap.get(i.categoryId());

            DailyCategoryProgressEntity existing = existingMap.get(i.categoryId());

            if (!shouldExist) {
                if (existing != null) {
                    toDelete.add(existing);
                }
                continue;
            }

            if (existing == null) {
                existing = new DailyCategoryProgressEntity();
                DailyCategoryProgressId id = new DailyCategoryProgressId(
                        req.date(),
                        i.categoryId()
                );

                existing.setId(id);
                existing.setCategory(category);
            }
            existing.setCompleted(i.completed());
            existing.setComment(i.comment());

            toSave.add(existing);
        }

        progressRepository.saveAll(toSave);
        progressRepository.deleteAll(toDelete);
    }

    @Transactional(readOnly = true)
    public DailyProgressResponse getDailyProgress(Long userId, LocalDate date){
        // find categories for user which are active
        List<CategoryEntity> categories = categoryRepository.findAllActiveByUser(userId);

        // find existing rows for day
        List<DailyCategoryProgressEntity> progressRows =
                progressRepository.findAllByUserAndDate(date, userId);

        // map progress to categoryId
        Map<Long, DailyCategoryProgressEntity> progressMap = progressRows.stream()
                .collect(Collectors.toMap(
                        i -> i.getId().getCategoryId(),
                        Function.identity()
                ));

        // response
        List<DailyProgressResponse.Item> items = new ArrayList<>();

        for (CategoryEntity category : categories){
            DailyCategoryProgressEntity progress = progressMap.get(category.getId());
            DailyProgressResponse.Item i = new DailyProgressResponse.Item(
                    category.getId(),
                    category.getName(),
                    progress != null && progress.isCompleted(),
                    progress != null ? progress.getComment() : null
            );
            items.add(i);
        }
        return new DailyProgressResponse(date, items);
    }
}
