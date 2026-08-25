package com.gibanator.dailystepbackendjava.targetselection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TargetSelectionRepository
        extends JpaRepository<TargetSelectionEntity, TargetSelectionId> {

    @Query("""
            select ts.id.targetId
            from TargetSelectionEntity ts
            where ts.id.date = :date and ts.target.user.id = :userId
            order by ts.id.targetId
            """)
    List<Long> findSelectedTargetIds(
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
            @Param("targetId") Long targetId,
            @Param("userId") Long userId
    );

    @Query("""
            select ts
            from TargetSelectionEntity ts
            where ts.target.user.id = :userId
            order by ts.id.date, ts.id.targetId
            """)
    List<TargetSelectionEntity> findAllByUserId(@Param("userId") Long userId);
}
