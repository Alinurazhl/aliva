package com.aliva.aliva.service;

import com.aliva.aliva.dto.RegistrationDto;
import com.aliva.aliva.entity.User;
import com.aliva.aliva.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegistrationDto dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Пользователь с таким username уже существует");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        User user = new User();

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        // Пароль сохраняем в зашифрованном виде
        user.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );

        // Обычный зарегистрированный пользователь
        user.setRole("USER");

        return userRepository.save(user);
    }
}