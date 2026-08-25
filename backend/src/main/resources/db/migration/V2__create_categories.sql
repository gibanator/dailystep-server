CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(255) NOT NULL,
    name_key VARCHAR(255),

    sort_order INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    is_system BOOLEAN DEFAULT FALSE,
    is_visible BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    user_id BIGINT NOT NULL,

    CONSTRAINT fk_category_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);