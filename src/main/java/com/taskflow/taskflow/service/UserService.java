package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dto.RegisterRequest;
import com.taskflow.taskflow.exception.ResourceAlreadyExistsException;
import com.taskflow.taskflow.model.User;
import com.taskflow.taskflow.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {

        String email = request.email()
                .strip()
                .toLowerCase(Locale.ROOT); // Locale representa las convenciones lingüísticas y regionales de un lugar.
                // Locale.ROOT: configuración regional neutral, independiente de un idioma o país

        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException(
            "User",
            "email"
            );
        }

        String passwordHash = passwordEncoder.encode(request.password()); // encode genera hash. Este hash es generaod con un salt aleatorio, el cual es guardado en el hash generado, con esto, puede 
        // dado la contraseña a validar, hashearla con mismo salt y ver si son iguales 

        User user = new User(email, passwordHash);

        return userRepository.save(user);
    }
}