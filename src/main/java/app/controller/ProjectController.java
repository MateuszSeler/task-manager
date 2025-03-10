package app.controller;

import app.dto.project.ProjectCreateRequestDto;
import app.dto.project.ProjectDto;
import app.service.ProjectService;
import app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "project manger",
        description = "Endpoints for managing projects")
@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "creating project",
            description = "creating new project")
    public ProjectDto createProject(@RequestBody @Valid ProjectCreateRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return projectService.createProject(
                userService.getByEmail(authentication.getName()).getId(), requestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(summary = "getting projects",
            description = "getting all of user's projects")
    public Set<ProjectDto> getUsersProjects() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return projectService.getUsersProjects(
                userService.getByEmail(authentication.getName()).getId());
    }

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.whetherUserIsMember(#projectId, authentication.name)")
    @GetMapping("/{projectId}")
    @Operation(summary = "getting project",
            description = "getting project by id")
    public ProjectDto getProjectById(@PathVariable @Valid Long projectId) {
        return projectService.getProjectById(projectId);
    }

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.isUserManagingTheProject(#projectId, authentication.name)")
    @PutMapping("/{projectId}")
    @Operation(summary = "getting projects",
            description = "getting all of user's projects")
    public ProjectDto updateProjectById(
            @PathVariable @Valid Long projectId,
            @RequestBody @Valid ProjectCreateRequestDto requestDto) {
        return projectService.updateProjectById(projectId, requestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.isUserManagingTheProject(#projectId, authentication.name)")
    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "deleting projects",
            description = "deleting project by id")
    public void deleteProjectById(@PathVariable @Valid Long projectId) {
        projectService.deleteById(projectId);
    }
}
