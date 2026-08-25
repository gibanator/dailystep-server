package com.gibanator.dailystepbackendjava.ai;

import com.gibanator.dailystepbackendjava.ai.dto.AiEvalResult;
import com.gibanator.dailystepbackendjava.ai.dto.AiEvaluateRequest;
import com.gibanator.dailystepbackendjava.ai.exception.ProviderNotAvailableException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Точка входа для оценки дня. Собирает все доступные {@link DayEvaluator} в карту по провайдеру
 * и роутит запрос на нужный. Результат прогоняется через {@link AiResultNormalizer}.
 *
 * <p>Spring внедряет в конструктор СПИСОК всех бинов {@link DayEvaluator}, найденных в контексте,
 * поэтому доступный набор провайдеров = то, что реально подключено и сконфигурировано.
 */
@Service
public class AiEvaluationService {

    private final Map<AiProvider, DayEvaluator> evaluators;
    private final AiResultNormalizer normalizer;

    public AiEvaluationService(List<DayEvaluator> evaluatorList, AiResultNormalizer normalizer) {
        this.evaluators = evaluatorList.stream()
                .collect(Collectors.toUnmodifiableMap(DayEvaluator::provider, Function.identity()));
        this.normalizer = normalizer;
    }

    public AiEvalResult evaluate(AiEvaluateRequest req) {
        DayEvaluator evaluator = evaluators.get(req.provider());
        if (evaluator == null) {
            throw new ProviderNotAvailableException(req.provider());
        }
        AiEvalResult raw = evaluator.evaluate(req);
        return normalizer.normalize(req, raw);
    }

    /** Какие провайдеры реально доступны — для меню выбора в приложении. */
    public Set<AiProvider> availableProviders() {
        return evaluators.keySet();
    }
}
