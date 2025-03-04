--USERS
INSERT INTO users (id, email, password, first_name, last_name, is_deleted)
VALUES (1, 'jan@gmail.com', '$2a$10$TVPKgP9EZZstDAmcxzrhnukVlvnolcVGMfZMlzh1ilSLtx6X3MbRu', 'Jan', 'Nowak', 0);

--JAN'S PROJECT WITH ONE TASKS
INSERT INTO projects (id, name, description, start_date, end_date, status)
VALUES (1, 'new project', 'description of Jan new project', '2025-02-26', '2025-04-26', 'IN_PROGRESS');

INSERT INTO project_members (project_id, user_id)
VALUES (1, 1);

INSERT INTO project_managers (project_id, user_id)
VALUES (1, 1);

INSERT INTO tasks (id, name, description, priority, status, due_date, project_id, user_id)
VALUES (1, 'first task', 'description of first task in new project', 'MEDIUM', 'IN_PROGRESS', '2025-03-26', 1, 1);
