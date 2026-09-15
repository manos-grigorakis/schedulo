CREATE
SEQUENCE IF NOT EXISTS business_role_sequence START
WITH 1 INCREMENT BY 20;

CREATE
SEQUENCE IF NOT EXISTS business_sequence START
WITH 1 INCREMENT BY 20;

CREATE TABLE business_members
(
    business_role_id BIGINT NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    user_id          BIGINT NOT NULL,
    business_id      BIGINT NOT NULL,
    CONSTRAINT pk_business_members PRIMARY KEY (user_id, business_id)
);

CREATE TABLE business_roles
(
    id          BIGINT      NOT NULL,
    name        VARCHAR(50) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_business_roles PRIMARY KEY (id)
);

CREATE TABLE businesses
(
    id            BIGINT       NOT NULL,
    name          VARCHAR(80)  NOT NULL,
    slug          VARCHAR(160) NOT NULL,
    email         VARCHAR(320) NOT NULL,
    logo_key      VARCHAR(255),
    phone         VARCHAR(30)  NOT NULL,
    street        VARCHAR(30)  NOT NULL,
    street_number VARCHAR(10)  NOT NULL,
    postal_code   VARCHAR(20)  NOT NULL,
    city          VARCHAR(30)  NOT NULL,
    country       VARCHAR(40)  NOT NULL,
    created_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_businesses PRIMARY KEY (id)
);

ALTER TABLE business_roles
    ADD CONSTRAINT uc_business_roles_name UNIQUE (name);

ALTER TABLE businesses
    ADD CONSTRAINT uc_businesses_slug UNIQUE (slug);

ALTER TABLE business_members
    ADD CONSTRAINT FK_BUSINESS_MEMBERS_ON_BUSINESS FOREIGN KEY (business_id) REFERENCES businesses (id);

ALTER TABLE business_members
    ADD CONSTRAINT FK_BUSINESS_MEMBERS_ON_BUSINESS_ROLE FOREIGN KEY (business_role_id) REFERENCES business_roles (id);

ALTER TABLE business_members
    ADD CONSTRAINT FK_BUSINESS_MEMBERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);