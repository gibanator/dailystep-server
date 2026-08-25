package com.gibanator.dailystepbackendjava.ai.dto;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;

/**
 * Результат модели (Structured Output) и одновременно ответ приложению.
 * Аннотации {@link JsonPropertyDescription} попадают в JSON-схему, которую Spring AI
 * ({@code .entity(AiEvalResult.class)}) добавляет в промпт — это помогает модели
 * вернуть строго нужную форму.
 */
public record AiEvalResult(
        @JsonPropertyDescription("Краткий итог дня одним предложением на русском")
        String daySummary,

        @JsonPropertyDescription("Оценка по каждой категории из запроса")
        List<CategoryResult> categories,

        @JsonPropertyDescription("Оценка по каждой цели из запроса")
        List<TargetResult> targets
) {
    public record CategoryResult(
            @JsonPropertyDescription("id категории из запроса")
            Long categoryId,
            @JsonPropertyDescription("true, если активность по этой категории была сегодня, иначе false")
            boolean completed,
            @JsonPropertyDescription("Короткий комментарий до 100 символов или пустая строка")
            String comment
    ) {}

    public record TargetResult(
            @JsonPropertyDescription("id цели из запроса")
            Long targetId,
            @JsonPropertyDescription("true, если пользователь сегодня занимался этой целью, иначе false")
            boolean completed
    ) {}
}
