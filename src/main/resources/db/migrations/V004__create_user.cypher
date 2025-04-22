CREATE CONSTRAINT user_id_unique IF NOT EXISTS
FOR (u:user)
REQUIRE u.id IS UNIQUE;

CREATE CONSTRAINT user_project_unique IF NOT EXISTS
FOR (u:user)
REQUIRE (u.userId, u.projectId) IS UNIQUE;
