package app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "attachments")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String fileName;
    @NotNull
    private String fileId;
    @NotNull
    private String fileUrl;
    @NotNull
    private String filePath;
    @NotNull
    private Instant uploadDate;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
    @NotBlank
    private String apiName;
}
