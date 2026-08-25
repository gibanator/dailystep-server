package com.gibanator.dailystepbackendjava.targetselection;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TargetSelectionId implements Serializable {

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "date")
    private LocalDate date;
}
