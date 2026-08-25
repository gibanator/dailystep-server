package com.gibanator.dailystepbackendjava.target;

import com.gibanator.dailystepbackendjava.target.dto.CreateTargetRequest;
import com.gibanator.dailystepbackendjava.target.dto.TargetListResponse;
import com.gibanator.dailystepbackendjava.target.dto.TargetResponse;
import com.gibanator.dailystepbackendjava.target.dto.TargetSelectionHistoryResponse;
import com.gibanator.dailystepbackendjava.target.dto.TargetSelectionResponse;
import com.gibanator.dailystepbackendjava.target.dto.UpdateTargetRequest;
import com.gibanator.dailystepbackendjava.target.exception.TargetNotFoundException;
import com.gibanator.dailystepbackendjava.targetselection.TargetSelectionId;
import com.gibanator.dailystepbackendjava.targetselection.TargetSelectionRepository;
import com.gibanator.dailystepbackendjava.user.UserEntity;
import com.gibanator.dailystepbackendjava.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TargetServiceIntegrationTests {

    @Autowired
    private TargetService service;

    @Autowired
    private TargetRepository targetRepository;

    @Autowired
    private TargetSelectionRepository selectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setEmail("target-user@example.com");
        user.setFirebaseUid("target-user-firebase-id");
        user = userRepository.save(user);
    }

    @Test
    void createsWithNullableDeadlineAndSupportsFullUpdate() {
        TargetResponse created = service.create(
                user.getId(),
                new CreateTargetRequest("  Read books  ", 3, null)
        );

        assertThat(created.name()).isEqualTo("Read books");
        assertThat(created.deadline()).isNull();
        assertThat(created.daysSelected()).isZero();
        assertThat(created.completed()).isFalse();

        TargetResponse updated = service.update(
                user.getId(),
                created.id(),
                new UpdateTargetRequest("Read one book", 1, LocalDate.of(2026, 7, 1), true)
        );

        assertThat(updated.name()).isEqualTo("Read one book");
        assertThat(updated.days()).isOne();
        assertThat(updated.deadline()).isEqualTo(LocalDate.of(2026, 7, 1));
        assertThat(updated.completed()).isTrue();

        TargetResponse reopened = service.update(
                user.getId(),
                created.id(),
                new UpdateTargetRequest("Read one book", 1, null, false)
        );

        assertThat(reopened.deadline()).isNull();
        assertThat(reopened.completed()).isFalse();
    }

    @Test
    void selectionOperationsAreIdempotentAndMayExceedTargetDays() {
        TargetResponse target = service.create(
                user.getId(),
                new CreateTargetRequest("Walk", 1, null)
        );
        LocalDate firstDate = LocalDate.of(2026, 6, 20);
        LocalDate secondDate = LocalDate.of(2026, 6, 21);

        TargetSelectionResponse firstSelect = service.select(user.getId(), target.id(), firstDate);
        TargetSelectionResponse repeatedSelect = service.select(user.getId(), target.id(), firstDate);
        TargetSelectionResponse beyondGoal = service.select(user.getId(), target.id(), secondDate);

        assertThat(firstSelect.daysSelected()).isOne();
        assertThat(repeatedSelect.daysSelected()).isOne();
        assertThat(beyondGoal.daysSelected()).isEqualTo(2);

        TargetSelectionResponse firstDelete = service.deselect(user.getId(), target.id(), firstDate);
        TargetSelectionResponse repeatedDelete = service.deselect(user.getId(), target.id(), firstDate);

        assertThat(firstDelete.daysSelected()).isOne();
        assertThat(repeatedDelete.daysSelected()).isOne();
        assertThat(selectionRepository.existsById(new TargetSelectionId(target.id(), firstDate))).isFalse();
    }

    @Test
    void returnsDailySelectionsAndChronologicalHistory() {
        TargetResponse first = service.create(
                user.getId(),
                new CreateTargetRequest("First", 2, null)
        );
        TargetResponse second = service.create(
                user.getId(),
                new CreateTargetRequest("Second", 2, null)
        );
        LocalDate earlier = LocalDate.of(2026, 6, 20);
        LocalDate later = LocalDate.of(2026, 6, 22);

        service.select(user.getId(), first.id(), later);
        service.select(user.getId(), first.id(), earlier);
        service.select(user.getId(), second.id(), later);

        TargetListResponse daily = service.findAllForDate(user.getId(), later);
        TargetSelectionHistoryResponse history = service.getSelectionHistory(user.getId(), first.id());

        assertThat(daily.targets()).extracting(TargetResponse::id)
                .containsExactly(first.id(), second.id());
        assertThat(daily.selectedTargetIds()).containsExactly(first.id(), second.id());
        assertThat(history.dates()).containsExactly(earlier, later);
    }

    @Test
    void hidesOtherUsersTargetsAndCascadesSelectionsOnDelete() {
        TargetResponse target = service.create(
                user.getId(),
                new CreateTargetRequest("Private", 2, null)
        );
        LocalDate date = LocalDate.of(2026, 6, 22);
        service.select(user.getId(), target.id(), date);
        entityManager.flush();
        entityManager.clear();

        UserEntity otherUser = new UserEntity();
        otherUser.setEmail("other-target-user@example.com");
        otherUser.setFirebaseUid("other-target-user-firebase-id");
        otherUser = userRepository.save(otherUser);

        Long otherUserId = otherUser.getId();
        assertThatThrownBy(() -> service.update(
                otherUserId,
                target.id(),
                new UpdateTargetRequest("Changed", 1, null, false)
        )).isInstanceOf(TargetNotFoundException.class);

        assertThatThrownBy(() -> service.getSelectionHistory(otherUserId, target.id()))
                .isInstanceOf(TargetNotFoundException.class);

        service.delete(user.getId(), target.id());
        targetRepository.flush();

        assertThat(selectionRepository.existsById(new TargetSelectionId(target.id(), date))).isFalse();
    }
}
