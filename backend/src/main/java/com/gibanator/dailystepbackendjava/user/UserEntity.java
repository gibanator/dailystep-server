package com.gibanator.dailystepbackendjava.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Entity
@Table(name="users")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "firebase_uid", nullable = false, unique = true)
    private String firebaseUid;

    private Instant createdAt;

    @PrePersist
    public void prePersist(){
        createdAt = Instant.now();
    }
}
