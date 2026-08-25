package com.gibanator.dailystepbackendjava.ai;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Список поддерживаемых нейросетевых провайдеров.
 * Приложение присылает выбор пользователя в поле {@code provider} запроса.
 * Реально доступны только те, для которых в контексте есть бин {@link DayEvaluator}
 * (см. {@code GET /api/v1/ai/models}).
 */
public enum AiProvider {
    GIGACHAT,
    CLAUDE,
    QWEN,
    DEEPSEEK;

    /** Принимаем значение в любом регистре ("gigachat", "GigaChat", "GIGACHAT"). */
    @JsonCreator
    public static AiProvider from(String value) {
        return AiProvider.valueOf(value.trim().toUpperCase());
    }
}
