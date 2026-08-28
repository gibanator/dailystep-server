package com.gibanator.dailystepbackendjava.daycompletion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.Instant;
import java.util.List;
import java.util.Optional;


public interface DayCompletionRepository extends JpaRepository<DayCompletionEntity, DayCompletionId> {

    Optional<DayCompletionEntity> findByIdDateAndIdUserId(
            LocalDate date,
            Long userId
    );

    boolean existsByIdDateAndIdUserId(
            LocalDate date,
            Long userId
    );

    List<DayCompletionEntity> findAllByIdUserIdOrderByIdDateAsc(Long userId);

    @Query("""
    SELECT dc
    FROM DayCompletionEntity dc
    WHERE dc.user.id = :userId
      AND dc.updatedAt > :since
    ORDER BY dc.id.date
    """)
    List<DayCompletionEntity> findChangedSince(
            Long userId,
            Instant since
    );
}
