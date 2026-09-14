CREATE
SEQUENCE IF NOT EXISTS platform_role_sequence START
WITH 1 INCREMENT BY 20;

CREATE
SEQUENCE IF NOT EXISTS revinfo_seq START
WITH 1 INCREMENT BY 50;

CREATE
SEQUENCE IF NOT EXISTS user_sequence START
WITH 1 INCREMENT BY 20;

CREATE TABLE platform_roles
(
    id          BIGINT      NOT NULL,
    name        VARCHAR(50) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_platform_roles PRIMARY KEY (id)
);

CREATE TABLE revchanges
(
    rev        BIGINT NOT NULL,
    entityname VARCHAR(255)
);

CREATE TABLE revinfo
(
    rev      BIGINT NOT NULL,
    revtstmp BIGINT,
    CONSTRAINT pk_revinfo PRIMARY KEY (rev)
);

CREATE TABLE users
(
    id               BIGINT       NOT NULL,
    first_name       VARCHAR(120) NOT NULL,
    last_name        VARCHAR(120) NOT NULL,
    email            VARCHAR(320) NOT NULL,
    password         VARCHAR(60),
    avatar_key       VARCHAR(255),
    phone            VARCHAR(30),
    platform_role_id BIGINT       NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE platform_roles
    ADD CONSTRAINT uc_platform_roles_name UNIQUE (name);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_PLATFORM_ROLE FOREIGN KEY (platform_role_id) REFERENCES platform_roles (id);

ALTER TABLE revchanges
    ADD CONSTRAINT fk_revchanges_on_default_tracking_modified_entities_changelog FOREIGN KEY (rev) REFERENCES revinfo (rev);