CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(254) NOT NULL UNIQUE,
    firebase_uid VARCHAR(128) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE categories (
    id UUID PRIMARY KEY,
    user_id BIGINT NOT NULL,

    name VARCHAR(255) NOT NULL,
    name_key VARCHAR(255),

    sort_order INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL,
    is_system BOOLEAN NOT NULL,
    is_visible BOOLEAN NOT NULL,
    deleted BOOLEAN NOT NULL,

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_category_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE daily_category_progress (
    date DATE NOT NULL,
    category_id UUID NOT NULL,

    completed BOOLEAN NOT NULL,
    comment TEXT,

    updated_at TIMESTAMPTZ NOT NULL,

    PRIMARY KEY (date, category_id),

    CONSTRAINT fk_daily_category_progress_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id)
        ON DELETE CASCADE
);

CREATE TABLE day_completion (
    user_id BIGINT NOT NULL,
    date DATE NOT NULL,

    deleted BOOLEAN NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL,

    PRIMARY KEY (user_id, date),

    CONSTRAINT fk_day_completion_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE targets (
    id UUID PRIMARY KEY,
    user_id BIGINT NOT NULL,

    deleted BOOLEAN NOT NULL,

    name VARCHAR(255) NOT NULL,
    days INTEGER NOT NULL,
    days_selected INTEGER NOT NULL,
    is_completed BOOLEAN NOT NULL,
    deadline DATE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_target_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_target_days_positive
        CHECK (days > 0),

    CONSTRAINT chk_target_days_selected_non_negative
        CHECK (days_selected >= 0)
);

CREATE TABLE target_selection (
    target_id UUID NOT NULL,
    date DATE NOT NULL,

    deleted BOOLEAN NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL,

    PRIMARY KEY (target_id, date),

    CONSTRAINT fk_target_selection_target
        FOREIGN KEY (target_id)
        REFERENCES targets(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_targets_user_id
    ON targets(user_id);

CREATE INDEX idx_target_selection_date_target_id
    ON target_selection(date, target_id);
