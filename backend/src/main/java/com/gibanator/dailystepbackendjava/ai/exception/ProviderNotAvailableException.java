package com.gibanator.dailystepbackendjava.ai.exception;

import com.gibanator.dailystepbackendjava.ai.AiProvider;

/**
 * Приложение запросило провайдера, для которого на бэкенде не настроен {@code DayEvaluator}
 * (например, не подключён стартер или нет ключа). Мапится в 400 Bad Request.
 */
public class ProviderNotAvailableException extends RuntimeException {
    public ProviderNotAvailableException(AiProvider provider) {
        super("AI provider not available: " + provider);
    }
}
