CREATE CONSTRAINT execution_id_unique IF NOT EXISTS
FOR (e:execution)
REQUIRE e.id IS UNIQUE;

CREATE CONSTRAINT execution_start_required IF NOT EXISTS
FOR (e:execution)
REQUIRE e.start IS NOT NULL;

CREATE CONSTRAINT execution_end_required IF NOT EXISTS
FOR (e:execution)
REQUIRE e.end IS NOT NULL;
