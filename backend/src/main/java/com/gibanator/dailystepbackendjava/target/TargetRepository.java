package com.gibanator.dailystepbackendjava.target;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface TargetRepository extends JpaRepository<TargetEntity, Long> {

    List<TargetEntity> findAllByUserIdOrderByIdAsc(Long userId);

    boolean existsByIdAndUserId(Long targetId, Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select t
            from TargetEntity t
            where t.id = :targetId and t.user.id = :userId
            """)
    Optional<TargetEntity> findOwnedByIdForUpdate(
            @Param("targetId") Long targetId,
            @Param("userId") Long userId
    );
}
