CREATE TABLE IF NOT EXISTS tags
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) UNIQUE
);

CREATE TABLE IF NOT EXISTS gift_certificates
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    name             VARCHAR(20) NOT NULL,
    description      VARCHAR(255),
    duration         SMALLINT,
    create_date      DATETIME,
    last_update_date DATETIME,
    status           ENUM ('ACTIVE', 'DELETED') DEFAULT 'ACTIVE',
    price            DECIMAL UNSIGNED
);

CREATE TABLE IF NOT EXISTS tags_gift_certificates
(
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    tag_id              BIGINT NOT NULL,
    gift_certificate_id BIGINT NOT NULL,
    FOREIGN KEY (tag_id) REFERENCES tags (id),
    FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificates (id)
);
