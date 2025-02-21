package app.service;

import app.dto.user.UserRegistrationRequestDto;
import app.dto.user.UserResponseDto;
import app.exception.EntityNotFoundException;
import app.exception.RegistrationException;
import app.mapper.UserMapper;
import app.model.User;
import app.repository.user.UserRepository;
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

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto) {
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

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findByEmail(String email) {
        return userMapper.toDto(userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User with email: " + email + " not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + id + " not found")));
    }
}
