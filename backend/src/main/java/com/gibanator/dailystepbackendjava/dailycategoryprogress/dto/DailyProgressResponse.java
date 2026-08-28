package com.gibanator.dailystepbackendjava.dailycategoryprogress.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record DailyProgressResponse(
        LocalDate date,
        List<Item> items
) {
    public record Item(
            UUID categoryId,
            String categoryName,
            boolean completed,
            String comment
    ){}
}
