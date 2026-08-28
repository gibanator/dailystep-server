package com.gibanator.dailystepbackendjava.sync.push.dto;

import java.util.UUID;

public record SyncCategoryMutationDto(
        UUID id,
        String name,
        String nameKey,
        int sortOrder,
        boolean active,
        boolean system,
        boolean visible,
        boolean deleted
) {
}
