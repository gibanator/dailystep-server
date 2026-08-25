CREATE TABLE daily_category_progress (
    date date NOT NULL,
    category_id bigint NOT NULL,
    completed boolean NOT NULL,
    comment text,
    PRIMARY KEY (date, category_id),
    CONSTRAINT fk_daily_category_progress_category
        FOREIGN KEY (category_id) REFERENCES categories(id)
);