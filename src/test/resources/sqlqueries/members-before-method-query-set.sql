--USERS
INSERT INTO users (id, email, password, first_name, last_name, is_deleted)
VALUES (1, 'jan@gmail.com', '$2a$10$TVPKgP9EZZstDAmcxzrhnukVlvnolcVGMfZMlzh1ilSLtx6X3MbRu', 'Jan', 'Nowak', 0);

INSERT INTO users (id, email, password, first_name, last_name, is_deleted)
VALUES (2, 'piotr@gmail.com', '$2a$10$bHUPuNHbuvggx5j1x128x.s9LP/WLzyuCiwduvNIuzMbq6JlmJVzS', 'Piotr', 'Piotr', 0);

INSERT INTO users (id, email, password, first_name, last_name, is_deleted)
VALUES (3, 'tomasz@gmail.com', '$2a$10$HSEwSm7eE0EQxiZDGltSYeDU5VmUMYYwbOPjCUJ2XGj4J1LUET.0C', 'Tomasz', 'Kowalski', 0);

--JAN'S PAST PROJECT WITH TOMASZ AS A MEMBER
INSERT INTO projects (id, name, description, start_date, end_date, status)
VALUES (1, 'past project', 'description of Jan past project', '2025-01-26', '2025-02-26', 'COMPLETED');

INSERT INTO project_members (project_id, user_id)
VALUES (1, 1);

INSERT INTO project_members (project_id, user_id)
VALUES (1, 3);

INSERT INTO project_managers (project_id, user_id)
VALUES (1, 1);

--COMMON PROJECT WITH 3 USERS AND 3 MANAGERS
INSERT INTO projects (id, name, description, start_date, end_date, status)
VALUES (2, 'past project', 'description of Jan past project', '2025-01-26', '2025-02-26', 'COMPLETED');

INSERT INTO project_members (project_id, user_id)
VALUES (2, 1);

INSERT INTO project_members (project_id, user_id)
VALUES (2, 2);

INSERT INTO project_members (project_id, user_id)
VALUES (2, 3);

INSERT INTO project_managers (project_id, user_id)
VALUES (2, 1);

INSERT INTO project_managers (project_id, user_id)
VALUES (2, 2);

INSERT INTO project_managers (project_id, user_id)
VALUES (2, 3);
