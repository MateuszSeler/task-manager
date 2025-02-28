--USERS
INSERT INTO users (id, email, password, first_name, last_name, is_deleted)
VALUES (1, 'jan@gmail.com', '$2a$10$TVPKgP9EZZstDAmcxzrhnukVlvnolcVGMfZMlzh1ilSLtx6X3MbRu', 'Jan', 'Nowak', 0);

INSERT INTO users (id, email, password, first_name, last_name, is_deleted)
VALUES (2, 'piotr@gmail.com', '$2a$10$bHUPuNHbuvggx5j1x128x.s9LP/WLzyuCiwduvNIuzMbq6JlmJVzS', 'Piotr', 'Piotr', 0);

--JAN'S PAST PROJECT WITH NO TASKS
INSERT INTO projects (id, name, description, start_date, end_date, status)
VALUES (1, 'past project', 'description of Jan past project', '2025-01-26', '2025-02-26', 'COMPLETED');

INSERT INTO project_members (project_id, user_id)
VALUES (1, 2);

INSERT INTO project_members (project_id, user_id)
VALUES (1, 1);

INSERT INTO project_managers (project_id, user_id)
VALUES (1, 1);

--JAN'S NEW PROJECT WITH TWO TASKS, ONE LABEL AND PIOTR AS A MEMBER
INSERT INTO projects (id, name, description, start_date, end_date, status)
VALUES (2, 'new project', 'description of Jan new project', '2025-02-26', '2025-04-26', 'IN_PROGRESS');

INSERT INTO project_members (project_id, user_id)
VALUES (2, 1);

INSERT INTO project_members (project_id, user_id)
VALUES (2, 2);

INSERT INTO project_managers (project_id, user_id)
VALUES (2, 1);

INSERT INTO tasks (id, name, description, priority, status, due_date, project_id, user_id)
VALUES (1, 'first task', 'description of first task in new project', 'MEDIUM', 'IN_PROGRESS', '2025-03-26', 2, 1);

INSERT INTO tasks (id, name, description, priority, status, due_date, project_id, user_id)
VALUES (2, 'second task', 'description of second task in new project', 'MEDIUM', 'IN_PROGRESS', '2025-03-26', 2, 2);

INSERT INTO labels (id, name, color, project_id)
VALUES (1, 'Milestone', 'LIME', '2');

INSERT INTO tasks_labels (label_id, task_id)
VALUES (1, 2);

--CONTROL OBJECT
INSERT INTO projects (id, name, description, start_date, end_date, status)
VALUES (3, 'Piotr project', 'description of Piotr project', '2025-01-26', '2025-02-26', 'COMPLETED');
