package app.mapper;

import app.config.MapperConfig;
import app.dto.user.UserRegistrationRequestDto;
import app.dto.user.UserResponseDto;
import app.model.User;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto registrationRequestDto);

    User toModel(UserResponseDto userResponseDto);

    @Named("fromUserResponseDtosToUsers")
    default Set<User> usersSet(
            Set<UserResponseDto> userDtos) {
        if (userDtos == null || userDtos.isEmpty()) {
            return new HashSet<>();
        }
        return userDtos
                .stream()
                .map(this::toModel)
                .collect(Collectors.toSet());
    }

    @Named("fromUsersToUserResponseDtos")
    default Set<UserResponseDto> cartItemDtosSet(Set<User> users) {
        if (users == null || users.isEmpty()) {
            return new HashSet<>();
        }
        return users
                .stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}
