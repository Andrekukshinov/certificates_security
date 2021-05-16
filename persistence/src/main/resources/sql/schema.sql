CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    email    VARCHAR(100) UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(55)  NOT NULL
);

CREATE TABLE IF NOT EXISTS orders
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT           NOT NULL,
    create_date DATETIME                                         DEFAULT CURRENT_TIMESTAMP(),
    status      ENUM ('ORDERED', 'EXPIRED', 'PAYED', 'APPROVED') DEFAULT 'ORDERED',
    total_price DECIMAL UNSIGNED NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS order_certificates
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    certificate_id BIGINT NOT NULL,
    order_id       BIGINT NOT NULL,
    quantity       INT UNSIGNED DEFAULT (1),
    FOREIGN KEY (certificate_id) REFERENCES gift_certificates (id),
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

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
