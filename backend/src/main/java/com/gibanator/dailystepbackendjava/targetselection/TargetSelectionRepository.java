package com.gibanator.dailystepbackendjava.targetselection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TargetSelectionRepository
        extends JpaRepository<TargetSelectionEntity, TargetSelectionId> {

    @Query("""
            select ts.id.targetId
            from TargetSelectionEntity ts
            where ts.id.date = :date and ts.target.user.id = :userId
            order by ts.target.createdAt
            """)
    List<UUID> findSelectedTargetIds(
            @Param("userId") Long userId,
            @Param("date") LocalDate date
    );

    @Query("""
            select ts.id.date
            from TargetSelectionEntity ts
            where ts.id.targetId = :targetId and ts.target.user.id = :userId
            order by ts.id.date
            """)
    List<LocalDate> findSelectionDates(
            @Param("targetId") UUID targetId,
            @Param("userId") Long userId
    );

    @Query("""
            select ts
            from TargetSelectionEntity ts
            where ts.target.user.id = :userId
            order by ts.id.date, ts.target.createdAt
            """)
    List<TargetSelectionEntity> findAllByUserId(@Param("userId") Long userId);

    @Query("""
    SELECT ts
    FROM TargetSelectionEntity ts
    WHERE ts.target.user.id = :userId
      AND ts.updatedAt > :since
    ORDER BY ts.id.targetId, ts.id.date
    """)
    List<TargetSelectionEntity> findChangedSince(
            Long userId,
            Instant since
    );
}
