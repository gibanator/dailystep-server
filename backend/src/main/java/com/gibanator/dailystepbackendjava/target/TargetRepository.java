package com.gibanator.dailystepbackendjava.target;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TargetRepository extends JpaRepository<TargetEntity, UUID> {

    List<TargetEntity> findAllByUserIdOrderByCreatedAtAsc(Long userId);

    boolean existsByIdAndUserId(UUID targetId, Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select t
            from TargetEntity t
            where t.id = :targetId and t.user.id = :userId
            """)
    Optional<TargetEntity> findOwnedByIdForUpdate(
            @Param("targetId") UUID targetId,
            @Param("userId") Long userId
    );

    @Query("""
    SELECT t
    FROM TargetEntity t
    WHERE t.user.id = :userId
      AND t.updatedAt > :since
    """)
    List<TargetEntity> findChangedSince(
            Long userId,
            Instant since
    );

    Optional<TargetEntity> findByIdAndUserId(UUID id, Long userId);
}
