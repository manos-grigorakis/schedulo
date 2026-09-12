CREATE
SEQUENCE IF NOT EXISTS oauth_account_sequence START
WITH 1 INCREMENT BY 20;

CREATE TABLE oauth_accounts
(
    id               BIGINT       NOT NULL,
    provider         VARCHAR(255) NOT NULL,
    external_subject VARCHAR(255) NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    user_id          BIGINT       NOT NULL,
    CONSTRAINT pk_oauth_accounts PRIMARY KEY (id)
);

ALTER TABLE oauth_accounts
    ADD CONSTRAINT uk_oauth_account_provider_external_subject UNIQUE (provider, external_subject);

ALTER TABLE oauth_accounts
    ADD CONSTRAINT FK_OAUTH_ACCOUNTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);