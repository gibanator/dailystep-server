CREATE TABLE targets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    days INTEGER NOT NULL,
    days_selected INTEGER NOT NULL DEFAULT 0,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    deadline DATE,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_target_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_target_days_positive
        CHECK (days > 0),
    CONSTRAINT chk_target_days_selected_non_negative
        CHECK (days_selected >= 0)
);

CREATE TABLE target_selection (
    target_id BIGINT NOT NULL,
    date DATE NOT NULL,

    PRIMARY KEY (target_id, date),
    CONSTRAINT fk_target_selection_target
        FOREIGN KEY (target_id) REFERENCES targets(id) ON DELETE CASCADE
);

CREATE INDEX idx_targets_user_id ON targets(user_id);
CREATE INDEX idx_target_selection_date_target_id ON target_selection(date, target_id);
