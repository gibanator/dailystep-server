package com.gibanator.dailystepbackendjava.daycompletion;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DayCompletionId implements Serializable {

    private Long userId;
    private LocalDate date;

}
