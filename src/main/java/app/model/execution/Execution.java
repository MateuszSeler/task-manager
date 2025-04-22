package app.model.execution;

import app.model.CalendarBlock;
import app.model.Competence;
import app.model.task.Task;
import java.util.Set;

public class Execution {
    private Task nextTask;
    private Set<Competence> competencesRequired;
    private Long realisationTime;
    private Set<CalendarBlock> executionBlocks;
}
