package com.gibanator.dailystepbackendjava.ai;

import com.gibanator.dailystepbackendjava.ai.dto.AiEvalResult;
import com.gibanator.dailystepbackendjava.ai.dto.AiEvaluateRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Пост-обработка ответа модели (провайдеро-независимая защита от «галлюцинаций» и кривого JSON):
 * <ul>
 *   <li>гарантирует, что в ответе есть запись по КАЖДОМУ присланному categoryId/targetId
 *       (если модель что-то пропустила — добавляем completed=false);</li>
 *   <li>выкидывает id, которых не было в запросе;</li>
 *   <li>обрезает комментарии до 100 символов, заменяет null на пустую строку.</li>
 * </ul>
 */
@Component
public class AiResultNormalizer {

    private static final int MAX_COMMENT_LENGTH = 100;

    public AiEvalResult normalize(AiEvaluateRequest req, AiEvalResult raw) {
        Map<Long, AiEvalResult.CategoryResult> byCategory = indexCategories(raw);
        Map<Long, AiEvalResult.TargetResult> byTarget = indexTargets(raw);

        List<AiEvalResult.CategoryResult> categories = namedOrEmpty(req.categories()).stream()
                .map(named -> {
                    AiEvalResult.CategoryResult r = byCategory.get(named.id());
                    boolean completed = r != null && r.completed();
                    String comment = sanitizeComment(r == null ? null : r.comment());
                    return new AiEvalResult.CategoryResult(named.id(), completed, comment);
                })
                .toList();

        List<AiEvalResult.TargetResult> targets = namedOrEmpty(req.targets()).stream()
                .map(named -> {
                    AiEvalResult.TargetResult r = byTarget.get(named.id());
                    boolean completed = r != null && r.completed();
                    return new AiEvalResult.TargetResult(named.id(), completed);
                })
                .toList();

        String daySummary = raw == null || raw.daySummary() == null ? "" : raw.daySummary();
        return new AiEvalResult(daySummary, categories, targets);
    }

    private Map<Long, AiEvalResult.CategoryResult> indexCategories(AiEvalResult raw) {
        if (raw == null || raw.categories() == null) {
            return Map.of();
        }
        return raw.categories().stream()
                .filter(c -> c.categoryId() != null)
                .collect(Collectors.toMap(
                        AiEvalResult.CategoryResult::categoryId,
                        Function.identity(),
                        (a, b) -> a));
    }

    private Map<Long, AiEvalResult.TargetResult> indexTargets(AiEvalResult raw) {
        if (raw == null || raw.targets() == null) {
            return Map.of();
        }
        return raw.targets().stream()
                .filter(t -> t.targetId() != null)
                .collect(Collectors.toMap(
                        AiEvalResult.TargetResult::targetId,
                        Function.identity(),
                        (a, b) -> a));
    }

    private List<AiEvaluateRequest.Named> namedOrEmpty(List<AiEvaluateRequest.Named> items) {
        return items == null ? List.of() : items;
    }

    private String sanitizeComment(String comment) {
        if (comment == null) {
            return "";
        }
        String trimmed = comment.strip();
        return trimmed.length() <= MAX_COMMENT_LENGTH
                ? trimmed
                : trimmed.substring(0, MAX_COMMENT_LENGTH);
    }
}
