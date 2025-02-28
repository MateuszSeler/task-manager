package app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "labels")
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Color color;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public enum Color {
        BLUE,
        CHARTREUSE,
        RED,
        CRIMSON,
        CYAN,
        LIME,
        GREEN;
    }
}
