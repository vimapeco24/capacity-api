CREATE TABLE IF NOT EXISTS capability_technologies (
    capability_id BIGINT NOT NULL,
    technology_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (capability_id, technology_id),

    CONSTRAINT fk_capability
        FOREIGN KEY (capability_id)
        REFERENCES capabilities(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_technology
        FOREIGN KEY (technology_id)
        REFERENCES technologies(id)
        ON DELETE CASCADE
);
