package com.gibanator.dailystepbackendjava.ai;

import com.gibanator.dailystepbackendjava.ai.dto.AiEvalResult;
import com.gibanator.dailystepbackendjava.ai.dto.AiEvaluateRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;

/**
 * Реализация оценки дня через DeepSeek.
 *
 * <p>DeepSeek предоставляет OpenAI-compatible Chat Completions API, поэтому
 * используем {@link OpenAiChatModel}, настроенный через {@code spring.ai.openai.*}.
 */
@Component
public class DeepSeekEvaluator implements DayEvaluator {

    private final ChatClient chatClient;
    private final PromptBuilder promptBuilder;

    public DeepSeekEvaluator(OpenAiChatModel chatModel, PromptBuilder promptBuilder) {
        this.chatClient = ChatClient.create(chatModel);
        this.promptBuilder = promptBuilder;
    }

    @Override
    public AiProvider provider() {
        return AiProvider.DEEPSEEK;
    }

    @Override
    public AiEvalResult evaluate(AiEvaluateRequest req) {
        return chatClient.prompt()
                .system(PromptBuilder.SYSTEM_EN)
                .user(promptBuilder.buildUserMessageEn(req))
                .call()
                .entity(AiEvalResult.class);
    }
}
