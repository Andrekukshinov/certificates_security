INSERT INTO gift_certificates(name, description, duration, create_date, last_update_date, price, status)
VALUES ('spa', 'family certificate', 3, '2021-03-25', '2021-03-25', 754, 'ACTIVE');

INSERT INTO gift_certificates(name, description, duration, create_date, last_update_date, price, status)
VALUES ('gym', 'for boss of the gym', 14, '2021-03-25', '2021-03-25', 300, 'ACTIVE');

INSERT INTO gift_certificates(name, description, duration, create_date, last_update_date, price, status)
VALUES ('pool', 'for better connection', 23, '2021-03-25', '2021-03-25', 354, DEFAULT);;

INSERT INTO gift_certificates(name, description, duration, create_date, last_update_date, price, status)
VALUES ('club', 'for leatherman', 9, '2021-03-25', '2021-03-25', 150, DEFAULT);

INSERT INTO gift_certificates(name, description, duration, create_date, last_update_date, price, status)
VALUES ('separate', 'test me', 9, '2021-03-25', '2021-03-25', 150, 'DELETED');

INSERT INTO gift_certificates(name, description, duration, create_date, last_update_date, price, status)
VALUES ('rich', 'for leatherman', 9, '2021-03-25', '2021-03-25', 10000, DEFAULT);

INSERT INTO gift_certificates(name, description, duration, create_date, last_update_date, price, status)
VALUES ('poor', 'for leatherman', 9, '2021-03-25', '2021-03-25', 1, DEFAULT);

INSERT INTO gift_certificates(name, description, duration, create_date, last_update_date, price, status)
VALUES ('pic kme', 'for leatherman', 9, '2021-03-25', '2021-03-25', 12, DEFAULT);

INSERT INTO tags(name)
VALUES ('leatherTime');
INSERT INTO tags(name)
VALUES ('people');

INSERT INTO tags_gift_certificates(gift_certificate_id, tag_id)
VALUES (2, 1);
INSERT INTO tags_gift_certificates(gift_certificate_id, tag_id)
VALUES (2, 2);
INSERT INTO tags_gift_certificates(gift_certificate_id, tag_id)
VALUES (1, 1);
INSERT INTO tags_gift_certificates(gift_certificate_id, tag_id)
VALUES (4, 1);
INSERT INTO tags_gift_certificates(gift_certificate_id, tag_id)
VALUES (4, 2);
INSERT INTO tags_gift_certificates(gift_certificate_id, tag_id)
VALUES (3, 2);
INSERT INTO tags_gift_certificates(gift_certificate_id, tag_id)
VALUES (5, 2);
INSERT INTO tags_gift_certificates(gift_certificate_id, tag_id)
VALUES (6, 2);
INSERT INTO tags_gift_certificates(gift_certificate_id, tag_id)
VALUES (8, 1);
INSERT INTO tags_gift_certificates(gift_certificate_id, tag_id)
VALUES (7, 1);
