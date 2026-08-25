package com.gibanator.dailystepbackendjava.ai;

import com.gibanator.dailystepbackendjava.ai.dto.AiEvaluateRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Единый построитель промпта для ВСЕХ провайдеров. System-инструкция и язык
 * user-сообщения выбираются реализацией провайдера.
 * Саму JSON-схему ответа в промпт добавлять не нужно — это делает Spring AI
 * при {@code .entity(AiEvalResult.class)}.
 */
@Component
public class PromptBuilder {

    public static final String SYSTEM_RU = """
            Ты — помощник в приложении-трекере жизни. Пользователь описывает, как прошёл его день.
            Тебе дают список его категорий и список его целей. Определи по тексту, что выполнено сегодня.
            Для каждой категории верни completed (true/false) и короткий comment (до 100 символов или пусто).
            Для каждой цели верни completed (true/false).
            Правила:
            - Оценивай ТОЛЬКО по тексту пользователя, не выдумывай факты.
            - Если про категорию или цель в тексте ничего не сказано — completed=false.
            - Это бинарная отметка: только выполнено/не выполнено, без баллов и оценок.
            - Верни оценку по КАЖДОЙ присланной категории и КАЖДОЙ присланной цели, используя их id.
            """;

    public static final String SYSTEM_EN = """
            You are an assistant in a life-tracking application. The user describes how their day went.
            You receive a list of their categories and a list of their goals. Determine what was completed today based on the text.
            For each category, return completed (true/false) and a short comment (up to 100 characters or empty).
            For each goal, return completed (true/false).
            Rules:
            - Evaluate ONLY based on the user's text. Do not invent facts.
            - If the text says nothing about a category or goal, completed=false.
            - This is a binary mark: completed/not completed only, no scores or ratings.
            - Return an evaluation for EVERY category and EVERY goal from the request, using their ids.
            """;

    public static final String SYSTEM = SYSTEM_RU;

    public String buildUserMessageRu(AiEvaluateRequest req) {
        String cats = formatNamed(req.categories(), "(категорий нет)");
        String tgts = formatNamed(req.targets(), "(целей нет)");

        return """
                Категории пользователя (id — название):
                %s

                Цели пользователя (id — название):
                %s

                Текст дня пользователя:
                "%s"
                """.formatted(cats, tgts, req.dayText());
    }

    public String buildUserMessageEn(AiEvaluateRequest req) {
        String categories = formatNamed(req.categories(), "(no categories)");
        String targets = formatNamed(req.targets(), "(no goals)");

        return """
                User categories (id - name):
                %s

                User goals (id - name):
                %s

                User's day text:
                "%s"
                """.formatted(categories, targets, req.dayText());
    }

    public String buildUserMessage(AiEvaluateRequest req) {
        return buildUserMessageRu(req);
    }

    private String formatNamed(List<AiEvaluateRequest.Named> items, String emptyText) {
        if (items == null || items.isEmpty()) {
            return emptyText;
        }
        return items.stream()
                .map(it -> it.id() + " — " + it.name())
                .collect(Collectors.joining("\n"));
    }
}
