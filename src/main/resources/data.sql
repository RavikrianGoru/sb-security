INSERT INTO users (username, password, enabled) VALUES
('admin','$2a$10$omAlkshiWkfwgqU4onytfe7.QsSZr.3QcYiFEwqcf0eS00UmYT4zC', true),
('ns','$2a$10$MfSbjvcq20FlHje8EX5cFeVH63dWYUhFlDbPxHoTkRvArJO0RWSIC', true),
('vs','$2a$10$bXCMlU9JVXKQYi2RoPX42.ZJb6xNvspWoXMwW6jzi8VWBssIyDe.W', true)
;

INSERT INTO authorities (username, authority) VALUES
('admin', 'ADMIN'),
('ns', 'EMPLOYEE'),
('vs', 'MANAGER')
;
