package app.model.execution;

import app.model.task.TaskNode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("execution")
@Data
public class ExecutionNode {
    @Id
    @GeneratedValue
    private UUID id;
    private LocalDateTime start;
    private LocalDateTime end;
    @Relationship(type = "refers_to", direction = Relationship.Direction.OUTGOING)
    private List<TaskNode> taskToWhichRefers;
}
