CREATE TABLE day_completion (
    user_id bigint NOT NULL,
    date date NOT NULL,
    PRIMARY KEY (user_id, date),
    CONSTRAINT fk_day_completion_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);