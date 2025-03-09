INSERT INTO users (id, email, password, first_name, last_name, is_deleted)
VALUES (1, 'jan@gmail.com', '$2a$10$TVPKgP9EZZstDAmcxzrhnukVlvnolcVGMfZMlzh1ilSLtx6X3MbRu', 'Jan', 'Nowak', 0);

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 2)

