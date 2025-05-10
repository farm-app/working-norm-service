--liquibase formatted sql

CREATE SCHEMA IF NOT EXISTS "working_norm";
SET search_path TO "working_norm";

--changeset Andrey Antonov:1
--comment create table working_norm
CREATE TABLE IF NOT EXISTS working_norm
(
    id            BIGSERIAL PRIMARY KEY,
    product_id    BIGINT,
    user_id       BIGINT,
    email         VARCHAR(64),
    working_count INTEGER     NOT NULL,
    current_count INTEGER     NOT NULL,
    deadline      TIMESTAMP   NOT NULL,
    status        VARCHAR(16) NOT NULL,
    updated_at    TIMESTAMP   NOT NULL,
    created_at    TIMESTAMP   NOT NULL
);
