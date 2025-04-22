package app.model;

import java.util.UUID;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("competence")
@Data
public class Competence {
    @Id
    @GeneratedValue
    private UUID id;
    private long projectId;
    private String name;
}
