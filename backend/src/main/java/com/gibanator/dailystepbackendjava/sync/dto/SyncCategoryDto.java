package com.gibanator.dailystepbackendjava.sync.dto;

import java.time.LocalDateTime;

public record SyncCategoryDto(
        Long id,
        String name,
        String nameKey,
        int sortOrder,
        boolean active,
        boolean system,
        boolean visible,
        boolean deleted,
        LocalDateTime createdAt
) {
}
