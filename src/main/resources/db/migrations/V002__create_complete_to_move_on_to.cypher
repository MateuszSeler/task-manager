CREATE CONSTRAINT complete_to_move_on_to_realisationTime_exists IF NOT EXISTS
FOR ()-[r:complete_to_move_on_to]->()
REQUIRE r.realisationTime IS NOT NULL;