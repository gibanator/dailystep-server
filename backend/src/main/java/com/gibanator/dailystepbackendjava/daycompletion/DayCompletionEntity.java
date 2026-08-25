package com.gibanator.dailystepbackendjava.daycompletion;

import com.gibanator.dailystepbackendjava.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "day_completion")
public class DayCompletionEntity {
    @EmbeddedId
    private DayCompletionId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

}
