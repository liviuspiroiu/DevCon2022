INSERT INTO orders(id, created_date, last_modified_date, address_1, address_2, city, country, postcode, shipped, status, total_price, payment_id, user_user_id)
VALUES (1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'Dionisie Lupu 66', 'Sector 1', 'Bucuresti', 'RO', '030167', null, 'SENT', 600, null, 1);

INSERT INTO order_items(id, created_date, last_modified_date, quantity, order_id, product_id)
VALUES (1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1.0, 1, 1);

INSERT INTO orders(id, created_date, last_modified_date, address_1, address_2, city, country, postcode, shipped, status, total_price, payment_id, user_user_id)
VALUES (2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'Dionisie Lupu 66', 'Sector 1', 'Bucuresti', 'RO', '030167', null, 'NEW', 1800, null, 1);

INSERT INTO order_items(id, created_date, last_modified_date, quantity, order_id, product_id)
VALUES (2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3.0, 2, 1);