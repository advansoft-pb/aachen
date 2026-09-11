truncate table orders cascade;
alter sequence order_id_seq restart with 100;
alter sequence order_item_id_seq restart with 100;

insert into orders (id, order_number, username,
                    customer_name, customer_phone,
                    delivery_address_zip_code, delivery_address_country,
                    status, comments)
values (1, 'order-123', 'user', 'Ala', '11111111', '75001', 'UK', 'NEW', null),
       (2, 'order-456', 'user', 'Ola', '22222222', '50072', 'India', 'NEW', null)
;

insert into order_items(order_id, code, name, price, quantity)
values (1, 'P00', 'The Hunger Games', 34.0, 2),
       (1, 'P01', 'To Kill a Mockingbird', 45.40, 1),
       (2, 'P02', 'The Chronicles of Narnia', 44.50, 1)
;
