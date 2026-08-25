package com.gibanator.dailystepbackendjava.target;

import com.gibanator.dailystepbackendjava.auth.exceptions.UserNotFoundException;
import com.gibanator.dailystepbackendjava.target.dto.CreateTargetRequest;
import com.gibanator.dailystepbackendjava.target.dto.TargetListResponse;
import com.gibanator.dailystepbackendjava.target.dto.TargetResponse;
import com.gibanator.dailystepbackendjava.target.dto.TargetSelectionHistoryResponse;
import com.gibanator.dailystepbackendjava.target.dto.TargetSelectionResponse;
import com.gibanator.dailystepbackendjava.target.dto.UpdateTargetRequest;
import com.gibanator.dailystepbackendjava.target.exception.TargetNotFoundException;
import com.gibanator.dailystepbackendjava.targetselection.TargetSelectionEntity;
import com.gibanator.dailystepbackendjava.targetselection.TargetSelectionId;
import com.gibanator.dailystepbackendjava.targetselection.TargetSelectionRepository;
import com.gibanator.dailystepbackendjava.user.UserEntity;
import com.gibanator.dailystepbackendjava.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TargetService {

    private final TargetRepository targetRepository;
    private final TargetSelectionRepository selectionRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public TargetListResponse findAllForDate(Long userId, LocalDate date) {
        List<TargetResponse> targets = targetRepository.findAllByUserIdOrderByIdAsc(userId)
                .stream()
                .map(this::toResponse)
                .toList();

        List<Long> selectedTargetIds = selectionRepository.findSelectedTargetIds(userId, date);

        return new TargetListResponse(date, targets, selectedTargetIds);
    }

    @Transactional
    public TargetResponse create(Long userId, CreateTargetRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        TargetEntity target = new TargetEntity();
        target.setUser(user);
        target.setName(request.name().trim());
        target.setDays(request.days());
        target.setDeadline(request.deadline());

        return toResponse(targetRepository.save(target));
    }

    @Transactional
    public TargetResponse update(Long userId, Long targetId, UpdateTargetRequest request) {
        TargetEntity target = findOwnedForUpdate(userId, targetId);

        target.setName(request.name().trim());
        target.setDays(request.days());
        target.setDeadline(request.deadline());
        target.setCompleted(request.completed());

        return toResponse(targetRepository.save(target));
    }

    @Transactional
    public void delete(Long userId, Long targetId) {
        TargetEntity target = findOwnedForUpdate(userId, targetId);
        targetRepository.delete(target);
    }

    @Transactional
    public TargetSelectionResponse select(Long userId, Long targetId, LocalDate date) {
        TargetEntity target = findOwnedForUpdate(userId, targetId);
        TargetSelectionId selectionId = new TargetSelectionId(targetId, date);

        if (!selectionRepository.existsById(selectionId)) {
            TargetSelectionEntity selection = new TargetSelectionEntity();
            selection.setId(selectionId);
            selection.setTarget(target);
            selectionRepository.save(selection);

            target.setDaysSelected(target.getDaysSelected() + 1);
            targetRepository.save(target);
        }

        return new TargetSelectionResponse(targetId, date, true, target.getDaysSelected());
    }

    @Transactional
    public TargetSelectionResponse deselect(Long userId, Long targetId, LocalDate date) {
        TargetEntity target = findOwnedForUpdate(userId, targetId);
        TargetSelectionId selectionId = new TargetSelectionId(targetId, date);

        if (selectionRepository.existsById(selectionId)) {
            selectionRepository.deleteById(selectionId);
            target.setDaysSelected(target.getDaysSelected() - 1);
            targetRepository.save(target);
        }

        return new TargetSelectionResponse(targetId, date, false, target.getDaysSelected());
    }

    @Transactional(readOnly = true)
    public TargetSelectionHistoryResponse getSelectionHistory(Long userId, Long targetId) {
        if (!targetRepository.existsByIdAndUserId(targetId, userId)) {
            throw new TargetNotFoundException(targetId);
        }

        return new TargetSelectionHistoryResponse(
                targetId,
                selectionRepository.findSelectionDates(targetId, userId)
        );
    }

    private TargetEntity findOwnedForUpdate(Long userId, Long targetId) {
        return targetRepository.findOwnedByIdForUpdate(targetId, userId)
                .orElseThrow(() -> new TargetNotFoundException(targetId));
    }

    private TargetResponse toResponse(TargetEntity target) {
        return new TargetResponse(
                target.getId(),
                target.getName(),
                target.getDays(),
                target.getDaysSelected(),
                target.isCompleted(),
                target.getDeadline(),
                target.getCreatedAt()
        );
    }
}
