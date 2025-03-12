package app.service;

import app.dto.user.UserResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public interface MemberService {
    boolean whetherUserIsMember(@NotNull Long projectId, @NotNull String userEmail);

    boolean isUserManagingTheProject(@NotNull Long projectId, String userEmail);

    Set<UserResponseDto> addUserToProject(@NotNull Long projectId, @NotNull Long userId);

    Set<UserResponseDto> deleteUserFromProject(@Valid Long projectId, @Valid Long userId);

    Set<UserResponseDto> makeUserManagerOfTheProject(@Valid Long projectId, @Valid Long userId);

    Set<UserResponseDto> removeUserFromTheProjectManagerRole(
            @Valid Long projectId, @Valid Long userId);
}
