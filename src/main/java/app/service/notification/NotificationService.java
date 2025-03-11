package app.service.notification;

import app.dto.project.ProjectDto;
import app.dto.user.UserResponseDto;
import app.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final ProjectService projectService;
    private final EmailService emailService;

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

        for (UserResponseDto user : project.getProjectMembers()) {
            emailService.sendEmail(user.getEmail(), subject, massage);
        }
    }
}
