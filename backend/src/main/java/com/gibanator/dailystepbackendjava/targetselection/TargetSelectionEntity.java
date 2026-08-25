package com.gibanator.dailystepbackendjava.targetselection;

import com.gibanator.dailystepbackendjava.target.TargetEntity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
