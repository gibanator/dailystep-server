package com.gibanator.dailystepbackendjava.dailycategoryprogress.dto;

import java.time.LocalDate;
import java.util.List;

public record DailyProgressResponse(
        LocalDate date,
        List<Item> items
) {
    public record Item(
            Long categoryId,
            String categoryName,
            boolean completed,
            String comment
    ){}
}
