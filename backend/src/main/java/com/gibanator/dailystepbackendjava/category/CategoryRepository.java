package com.gibanator.dailystepbackendjava.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    List<CategoryEntity> findAllByUserId(Long userId);
    List<CategoryEntity> findByUserIdAndDeletedFalse(Long userId);
    List<CategoryEntity> findAllByUserIdOrderBySortOrderAscCreatedAtAsc(Long userId);
    List<CategoryEntity> findAllByIdInAndUserIdAndDeletedFalse(Collection<UUID> ids, Long userId);
    boolean existsByUserIdAndNameAndDeletedFalse(Long userId, String name);

    @Query("""
    select c
    from CategoryEntity c
    where c.user.id = :userId
      and c.isActive = true
      and c.deleted = false
    order by c.sortOrder
    """)
    List<CategoryEntity> findAllActiveByUser(Long userId);

    @Query("""
    SELECT c
    FROM CategoryEntity c
    WHERE c.user.id = :userId
      AND c.updatedAt > :since
    """)
    List<CategoryEntity> findChangedSince(
            Long userId,
            Instant since
    );

    Optional<CategoryEntity> findByIdAndUserId(UUID id, Long userId);
}
