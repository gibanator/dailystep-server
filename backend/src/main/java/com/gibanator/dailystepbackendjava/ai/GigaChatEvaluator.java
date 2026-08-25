package com.gibanator.dailystepbackendjava.ai;

import chat.giga.springai.GigaChatModel;
import com.gibanator.dailystepbackendjava.ai.dto.AiEvalResult;
import com.gibanator.dailystepbackendjava.ai.dto.AiEvaluateRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

/**
 * Реализация оценки дня через GigaChat (Sber) поверх Spring AI.
 *
 * <p>Внедряем конкретный {@link GigaChatModel}, чтобы при подключении других
 * {@code ChatModel}-провайдеров Spring не выбирал модель неоднозначно.
 */
@Component
public class GigaChatEvaluator implements DayEvaluator {

    private final ChatClient chatClient;
    private final PromptBuilder promptBuilder;

    public GigaChatEvaluator(GigaChatModel chatModel, PromptBuilder promptBuilder) {
        this.chatClient = ChatClient.create(chatModel);
        this.promptBuilder = promptBuilder;
    }

    @Override
    public AiProvider provider() {
        return AiProvider.GIGACHAT;
    }

    @Override
    public AiEvalResult evaluate(AiEvaluateRequest req) {
        return chatClient.prompt()
                .system(PromptBuilder.SYSTEM_RU)
                .user(promptBuilder.buildUserMessageRu(req))
                .call()
                .entity(AiEvalResult.class);
    }
}
