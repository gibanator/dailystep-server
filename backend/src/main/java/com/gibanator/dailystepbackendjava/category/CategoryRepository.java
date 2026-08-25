package com.gibanator.dailystepbackendjava.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findByUserIdAndDeletedFalse(Long userId);
    List<CategoryEntity> findAllByUserIdOrderByIdAsc(Long userId);
    List<CategoryEntity> findAllByIdInAndUserIdAndDeletedFalse(Collection<Long> ids, Long userId);
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
}
