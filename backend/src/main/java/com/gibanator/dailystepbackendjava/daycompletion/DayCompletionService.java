package com.gibanator.dailystepbackendjava.daycompletion;

import com.gibanator.dailystepbackendjava.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DayCompletionService {

    private final DayCompletionRepository dayCompletionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void markCompleted(Long userId, LocalDate date) {
        DayCompletionId id = new DayCompletionId(userId, date);

        if (dayCompletionRepository.existsById(id)) {
            return;
        }

        DayCompletionEntity entity = new DayCompletionEntity();
        entity.setId(id);
        entity.setUser(userRepository.getReferenceById(userId));

        dayCompletionRepository.save(entity);
    }

    @Transactional
    public void unmarkCompleted(Long userId, LocalDate date) {
        DayCompletionId id = new DayCompletionId(userId, date);

        if (!dayCompletionRepository.existsById(id)) {
            return;
        }

        dayCompletionRepository.deleteById(id);
    }
}
