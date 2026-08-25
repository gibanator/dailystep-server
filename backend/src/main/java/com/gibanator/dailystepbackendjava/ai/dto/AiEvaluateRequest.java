package com.gibanator.dailystepbackendjava.ai.dto;

import com.gibanator.dailystepbackendjava.ai.AiProvider;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Вход от приложения: текст дня + список категорий и целей пользователя + выбранный провайдер.
 * Категории и цели приходят из приложения, потому что на бэкенде целей в БД нет,
 * а состав категорий у каждого пользователя свой.
 */
public record AiEvaluateRequest(
        @NotNull(message = "Provider not specified.")
        AiProvider provider,

        @NotNull(message = "Date not specified.")
        String date,            // "YYYY-MM-DD"

        @NotNull(message = "dayText not specified.")
        String dayText,

        List<Named> categories, // категории пользователя
        List<Named> targets     // цели пользователя (могут быть пустыми)
) {
    public record Named(Long id, String name) {}
}
