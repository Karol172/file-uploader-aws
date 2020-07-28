-- liquibase formatted sql
-- changeset author:kcymerys

CREATE TABLE FILE
(
    ID              BIGINT DEFAULT NEXTVAL('HIBERNATE_SEQUENCE'),
    FILENAME        VARCHAR(255) UNIQUE NOT NULL,
    SIZE            BIGINT              NOT NULL,
    TIMESTAMP       TIMESTAMP           NOT NULL,
    PRIMARY KEY (ID)
);
