package app.mapper;

import app.config.MapperConfig;
import app.dto.user.UserRegistrationRequestDto;
import app.dto.user.UserResponseDto;
import app.exception.EntityNotFoundException;
import app.model.User;
import app.repository.user.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto registrationRequestDto);

    @Named("fromUserIdToUser")
    default User fromUserIdToUser(Long userId, @Context UserRepository userRepository) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + userId + " not found")
        );
    }

}
