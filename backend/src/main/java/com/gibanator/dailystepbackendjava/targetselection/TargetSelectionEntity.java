package com.gibanator.dailystepbackendjava.targetselection;

import com.gibanator.dailystepbackendjava.target.TargetEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "target_selection")
@NoArgsConstructor
@Getter
@Setter
public class TargetSelectionEntity {

    @EmbeddedId
    private TargetSelectionId id;

    @ManyToOne
    @MapsId("targetId")
    @JoinColumn(name = "target_id", nullable = false)
    private TargetEntity target;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        updatedAt = Instant.now();
    }
}
