package com.gibanator.dailystepbackendjava.dailycategoryprogress;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DailyCategoryProgressId implements Serializable {

    private LocalDate date;
    private UUID categoryId;

}
