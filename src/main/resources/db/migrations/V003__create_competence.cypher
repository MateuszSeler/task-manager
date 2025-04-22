CREATE CONSTRAINT competence_project_name_unique IF NOT EXISTS
FOR (c:competence)
REQUIRE (c.projectId, c.name) IS UNIQUE;

CREATE CONSTRAINT competence_projectId_exists IF NOT EXISTS
FOR (c:competence)
REQUIRE c.projectId IS NOT NULL;

CREATE CONSTRAINT competence_name_exists IF NOT EXISTS
FOR (c:competence)
REQUIRE c.name IS NOT NULL;
