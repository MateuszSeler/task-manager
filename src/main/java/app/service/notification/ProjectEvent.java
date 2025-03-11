package app.service.notification;

import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProjectEvent extends ApplicationEvent {
    private final Long projectId;
    private final Long taskId;
    private final Long attachmentId;
    private final Long commentId;
    private final Type typOfEvent;
    private final Action typeOfAction;
    private final String customMassage;

    @Builder
    public ProjectEvent(
            Object source, Long taskId, Long projectId, Long attachmentId,
            Long commentId, Type typOfEvent, Action typeOfAction, String customMassage) {
        super(source);
        this.projectId = projectId;
        this.taskId = taskId;
        this.attachmentId = attachmentId;
        this.commentId = commentId;
        this.typOfEvent = typOfEvent;
        this.typeOfAction = typeOfAction;
        this.customMassage = customMassage;
    }

    public enum Type {
        PROJECT,
        TASK,
        ATTACHMENT,
        COMMENT,
        LABEL;
    }

    public enum Action {
        ADDED,
        CHANGED,
        DELETED,
        COMPLETED;
    }
}
