package app.model.execution;

import app.model.task.TaskNode;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class ExecutionEdge {
    @TargetNode
    private TaskNode nextTask;
    private Long realisationTime;
}
