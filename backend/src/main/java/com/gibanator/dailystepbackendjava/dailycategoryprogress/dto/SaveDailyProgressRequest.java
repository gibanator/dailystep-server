package com.gibanator.dailystepbackendjava.dailycategoryprogress.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record SaveDailyProgressRequest(
        @NotNull(message = "Date not specified.")
        LocalDate date,
        List<Item> items
) {
    public record Item(
            UUID categoryId,
            boolean completed,
            String comment
    ) {}
}
