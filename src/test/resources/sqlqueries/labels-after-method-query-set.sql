TRUNCATE TABLE projects;
ALTER TABLE projects
AUTO_INCREMENT = 1;

TRUNCATE TABLE users;
ALTER TABLE users
AUTO_INCREMENT = 1;

TRUNCATE TABLE project_members;
ALTER TABLE project_members
AUTO_INCREMENT = 1;

TRUNCATE TABLE project_managers;
ALTER TABLE project_managers
AUTO_INCREMENT = 1;

TRUNCATE TABLE tasks;
ALTER TABLE tasks
AUTO_INCREMENT = 1;

TRUNCATE TABLE labels;
ALTER TABLE labels
AUTO_INCREMENT = 1;

TRUNCATE TABLE tasks_labels;
ALTER TABLE tasks_labels
AUTO_INCREMENT = 1;
