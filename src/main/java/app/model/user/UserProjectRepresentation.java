package app.model.user;

import app.model.execution.ExecutionNode;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("user")
@Data
public class UserProjectRepresentation {
    @Id
    @GeneratedValue
    private UUID id;
    private Long userId;
    private Long projectId;
    @Relationship(type = "work_in", direction = Relationship.Direction.OUTGOING)
    private List<ExecutionNode> executionBlock;
}
