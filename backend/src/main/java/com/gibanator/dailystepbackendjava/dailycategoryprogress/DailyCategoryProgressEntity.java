package com.gibanator.dailystepbackendjava.dailycategoryprogress;

import com.gibanator.dailystepbackendjava.category.CategoryEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "daily_category_progress")
public class DailyCategoryProgressEntity {

    @EmbeddedId
    private DailyCategoryProgressId id;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(nullable = false)
    private boolean completed;

    private String comment;
}
