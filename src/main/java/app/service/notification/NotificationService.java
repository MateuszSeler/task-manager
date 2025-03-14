package app.service.notification;

import app.dto.project.ProjectDto;
import app.service.ProjectService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Logger errorLogger = LogManager.getRootLogger();
    private final ProjectService projectService;
    private final EmailService emailService;
    private final Executor executor;

    @EventListener
    public void notedChangeInTheProject(ProjectEvent event) {
        ProjectDto project = projectService.getProjectById(event.getProjectId());
        String subject = new StringBuilder(
                project.getName().toUpperCase())
                .append(": ")
                .append(event.getTypOfEvent().toString().toLowerCase())
                .append(" has been ")
                .append(event.getTypeOfAction().toString().toLowerCase())
                .append(".")
                .toString();

        String massage;
        if (event.getCustomMassage() == null) {
            massage = subject;
        } else {
            massage = event.getCustomMassage();
        }

        project.getProjectMembers()
                .stream()
                .map(user -> CompletableFuture.runAsync(
                        () -> emailService.sendEmail(user.getEmail(), subject, massage), executor)
                        .exceptionally(ex -> {
                            errorLogger.error("message about "
                                    + subject
                                    + " wasn't send to "
                                    + user.getEmail());
                            return null;
                        }))
                .toList();
    }
}
