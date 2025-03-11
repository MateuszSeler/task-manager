package app.service.notification;

import static app.service.notification.ProjectEvent.Action.ADDED;
import static app.service.notification.ProjectEvent.Action.CHANGED;
import static app.service.notification.ProjectEvent.Action.DELETED;
import static app.service.notification.ProjectEvent.Type.COMMENT;
import static app.service.notification.ProjectEvent.Type.LABEL;
import static app.service.notification.ProjectEvent.Type.TASK;

import app.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeManager {
    private final ApplicationEventPublisher eventPublisher;
    private final ProjectService projectService;

    //TASKS
    public void notedTaskCreated(Long projectId, Long taskId) {
        eventPublisher.publishEvent(ProjectEvent
                .builder()
                .source(this)
                .projectId(projectId)
                .taskId(taskId)
                .typOfEvent(TASK)
                .typeOfAction(ADDED)
                .build());
    }

    public void notedTaskEdited(Long projectId, Long taskId) {
        eventPublisher.publishEvent(ProjectEvent
                .builder()
                .source(this)
                .projectId(projectId)
                .taskId(taskId)
                .typOfEvent(TASK)
                .typeOfAction(CHANGED)
                .build());
    }

    public void notedLabelWasAdded(Long projectId, String taskName, String labelName) {
        String customMsg = new StringBuilder(
                labelName)
                .append(" label was added to task: ")
                .append(taskName)
                .append(".")
                .toString();

        eventPublisher.publishEvent(ProjectEvent
                .builder()
                .source(this)
                .projectId(projectId)
                .typOfEvent(LABEL)
                .typeOfAction(ADDED)
                .customMassage(customMsg)
                .build());
    }

    public void notedLabelWasRemoved(Long projectId, String taskName, String labelName) {
        String customMsg = new StringBuilder(
                labelName)
                .append(" label was removed from task: ")
                .append(taskName)
                .append(".")
                .toString();

        eventPublisher.publishEvent(ProjectEvent
                .builder()
                .source(this)
                .projectId(projectId)
                .typOfEvent(LABEL)
                .typeOfAction(DELETED)
                .customMassage(customMsg)
                .build());
    }

    public void notedTaskDeleted(Long projectId, Long taskId) {
        eventPublisher.publishEvent(ProjectEvent
                .builder()
                .source(this)
                .projectId(projectId)
                .taskId(taskId)
                .typOfEvent(TASK)
                .typeOfAction(DELETED)
                .build());
    }

    //COMMENTS
    public void notedCommentCreated(Long projectId, Long commentId) {
        eventPublisher.publishEvent(ProjectEvent
                .builder()
                .source(this)
                .projectId(projectId)
                .taskId(commentId)
                .typOfEvent(COMMENT)
                .typeOfAction(ADDED)
                .build());
    }

    public void notedCommentDeleted(Long projectId, Long commentId) {
        eventPublisher.publishEvent(ProjectEvent
                .builder()
                .source(this)
                .projectId(projectId)
                .taskId(commentId)
                .typOfEvent(COMMENT)
                .typeOfAction(DELETED)
                .build());
    }

    public void notedCommentEdited(Long projectId, Long commentId) {
        eventPublisher.publishEvent(ProjectEvent
                .builder()
                .source(this)
                .projectId(projectId)
                .taskId(commentId)
                .typOfEvent(COMMENT)
                .typeOfAction(CHANGED)
                .build());
    }
}
