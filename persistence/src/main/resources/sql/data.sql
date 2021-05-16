INSERT INTO gift_certificates(name, description, duration, create_date, last_update_date, price, status)
VALUES ('spa', 'family certificate', 3, '2021-03-25', '2021-03-25', 754, 'DELETED'),
       ('gym', 'for boss of the gym', 14, '2021-03-25', '2021-03-25', 300, 'ACTIVE'),
       ('pool', 'for better connection', 23, '2021-03-25', '2021-03-25', 354, DEFAULT),
       ('club', 'for leatherman', 9, '2021-03-25', '2021-03-25', 150, DEFAULT),
       ('separate', 'test me', 9, '2021-03-25', '2021-03-25', 150, DEFAULT),
       ('rich', 'for leatherman', 9, '2021-03-25', '2021-03-25', 10000, DEFAULT),
       ('poor', 'for leatherman', 9, '2021-03-25', '2021-03-25', 1, DEFAULT),
       ('pic kme', 'for leatherman', 9, '2021-03-25', '2021-03-25', 12, DEFAULT);

INSERT INTO tags(name)
    VALUE
    ('activity'),
    ('sports'),
    ('workout'),
    ('leatherTime'),
    ('people'),
    ('rich'),
    ('poor'),
    ('to be shown');

INSERT INTO tags_gift_certificates(gift_certificate_id, tag_id)
VALUES (2, 1),
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (1, 4),
       (1, 5),
       (4, 1),
       (4, 4),
       (3, 4),
       (4, 5),
       (5, 5),
       (5, 4),
       (5, 3),
       (6, 6),
       (8, 8),
       (7, 7);


INSERT INTO users(email, password, nickname)
    VALUE
    ('billyHarington@gmail.com', SHA1('password'), 'BillYHar'),
    ('vanVandarkholme@gmail.com', SHA1('password2'), 'Vaaaan'),
    ('markVolf@gmail.com', SHA1('password'), 'mVolf'),
    ('johnySins@gmail.com', SHA1('password'), 'saintSinner');

INSERT INTO orders(user_id, create_date, status, total_price)
    VALUE
    (1, '2021-04-19', 'PAYED', 300),
    (3, '2021-04-20', 'PAYED', 1108),
    (2, '2021-04-21', 'PAYED', 150);


INSERT INTO order_certificates(certificate_id, order_id)
    VALUE
    (2, 1),
    (3, 2),
    (1, 2),
    (5, 3);
