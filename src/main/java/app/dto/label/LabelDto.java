package app.dto.label;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LabelDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @NotNull
    private Long projectId;
}
