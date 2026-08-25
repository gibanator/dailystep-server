package com.gibanator.dailystepbackendjava.ai;

import com.gibanator.dailystepbackendjava.ai.dto.AiEvalResult;
import com.gibanator.dailystepbackendjava.ai.dto.AiEvaluateRequest;

/**
 * Стратегия оценки дня одним провайдером. На каждый провайдер — свой бин-реализация.
 * Все реализации Spring собирает в {@code Map<AiProvider, DayEvaluator>} внутри
 * {@link AiEvaluationService}, который роутит запрос по {@link AiEvaluateRequest#provider()}.
 *
 * <p>Добавление нового провайдера = новый класс, реализующий этот интерфейс.
 * Остальной код не меняется.
 */
public interface DayEvaluator {

    /** Какой провайдер обслуживает эта реализация. */
    AiProvider provider();

    /** Вызвать модель и вернуть «сырой» результат (нормализацию делает сервис). */
    AiEvalResult evaluate(AiEvaluateRequest req);
}
