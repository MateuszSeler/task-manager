package app.model.task;

import app.model.Competence;
import app.model.execution.ExecutionEdge;
import app.model.user.UserProjectRepresentation;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("task")
@Data
public class TaskNode {
    @Id
    @GeneratedValue
    private UUID id;
    private Long taskId;
    private UserProjectRepresentation user;
    @Relationship(type = "complete_to_move_on_to", direction = Relationship.Direction.OUTGOING)
    private List<ExecutionEdge> completeToMoveOn;
    @Relationship(type = "will_required")
    private Set<Competence> competencesRequired;
}
