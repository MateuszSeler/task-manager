package app.service;

import app.dto.user.UserRegistrationRequestDto;
import app.dto.user.UserResponseDto;
import app.exception.EntityNotFoundException;
import app.exception.RegistrationException;
import app.mapper.UserMapper;
import app.model.Role;
import app.model.User;
import app.repository.RoleRepository;
import app.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserResponseDto register(@Valid UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.findByEmail(userRegistrationRequestDto.getEmail()).isPresent()) {
            throw new RegistrationException("User with email: "
                    + userRegistrationRequestDto.getEmail() + " not found");
        }
        if (!userRegistrationRequestDto.getRepeatPassword()
                .equals(userRegistrationRequestDto.getPassword())) {
            throw new RegistrationException("Passwords do not match");
        }

        User user = userMapper.toModel(userRegistrationRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(getDefaultUserRole());

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getByEmail(@NotNull String email) {
        return userMapper.toDto(userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User with email: " + email + " not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findById(@NotNull Long id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + id + " not found")));
    }

    @Override
    @Transactional
    public UserResponseDto updateProfile(Long userId, UserRegistrationRequestDto requestDto) {
        User user = getUserByIdOrThrowEntityNotFoundException(userId)
                .setEmail(requestDto.getEmail())
                .setFirstName(requestDto.getFirstName())
                .setLastName(requestDto.getLastName())
                .setPassword(requestDto.getPassword());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deletingById(@NotNull Long userId) {
        getUserByIdOrThrowEntityNotFoundException(userId);
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getById(@NotNull Long userId) {
        return userMapper.toDto(getUserByIdOrThrowEntityNotFoundException(userId));
    }

    @Transactional(readOnly = true)
    private User getUserByIdOrThrowEntityNotFoundException(@NotNull Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + userId + " not found"));
    }

    @Transactional(readOnly = true)
    private Role getDefaultUserRole() {
        return roleRepository.findByRoleType(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException("User role not found"));
    }
}
