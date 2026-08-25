package com.gibanator.dailystepbackendjava.ai;

import com.gibanator.dailystepbackendjava.ai.dto.AiEvalResult;
import com.gibanator.dailystepbackendjava.ai.dto.AiEvaluateRequest;
import com.gibanator.dailystepbackendjava.auth.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * AI-оценка дня. Эндпоинт защищён той же Firebase-аутентификацией, что и остальные
 * ({@code SecurityConfig} требует аутентификацию для всех путей, кроме swagger).
 * Эндпоинт только ОЦЕНИВАЕТ день (возвращает отметки 1/0) и ничего не пишет в БД —
 * сохранение делает уже существующий {@code POST /api/v1/progress} после подтверждения пользователем.
 */
@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiEvaluationService service;

    @PostMapping("/evaluate")
    public ResponseEntity<AiEvalResult> evaluate(
            @AuthenticationPrincipal UserPrincipal user,   // на будущее: лимиты по user.getId()
            @Valid @RequestBody AiEvaluateRequest request
    ) {
        return ResponseEntity.ok(service.evaluate(request));
    }

    /** Список реально доступных провайдеров — приложение строит из него меню выбора модели. */
    @GetMapping("/models")
    public ResponseEntity<Set<AiProvider>> availableModels(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok(service.availableProviders());
    }
}
