package app.controller;

import app.dto.user.UserResponseDto;
import app.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "members manger",
        description = "Endpoints for managing members in projects")
@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class MemberController {
    private final MemberService memberService;

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.isUserManagingTheProject(#projectId, authentication.name)")
    @PostMapping("/{projectId}/members/{userId}")
    @Operation(summary = "adding member",
            description = "adding user to the project")
    public Set<UserResponseDto> addUserToProject(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long userId) {
        return memberService.addUserToProject(projectId, userId);
    }

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.isUserManagingTheProject(#projectId, authentication.name)")
    @DeleteMapping("/{projectId}/members/{userId}")
    @Operation(summary = "deleting member",
            description = "removing user from the project")
    public Set<UserResponseDto> deleteMember(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long userId) {
        return memberService.deleteUserFromProject(projectId, userId);
    }

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.isUserManagingTheProject(#projectId, authentication.name)")
    @PostMapping("/{projectId}/members/{userId}/managers")
    @Operation(summary = "changing user role",
            description = "changing user role in the project")
    public Set<UserResponseDto> makeUserManagerOfTheProject(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long userId) {
        return memberService.makeUserManagerOfTheProject(projectId, userId);
    }

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.isUserManagingTheProject(#projectId, authentication.name)")
    @DeleteMapping("/{projectId}/members/{userId}/managers")
    @Operation(summary = "changing user role",
            description = "changing user role in the project")
    public Set<UserResponseDto> removeUserFromTheProjectManagerRole(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long userId) {
        return memberService.removeUserFromTheProjectManagerRole(projectId, userId);
    }
}
