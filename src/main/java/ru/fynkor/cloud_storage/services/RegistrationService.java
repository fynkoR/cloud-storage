package ru.fynkor.cloud_storage.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fynkor.cloud_storage.dto.UserRequestDto;
import ru.fynkor.cloud_storage.entities.User;
import ru.fynkor.cloud_storage.exceptions.UserYetExistsException;
import ru.fynkor.cloud_storage.repositories.UserRepository;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String registration(UserRequestDto userRequestDto){
        String username = userRequestDto.username();

        if(userRepository.existsUserByUsername(username)){
            throw new UserYetExistsException(username);
        }

        String encodedPassword = passwordEncoder.encode(userRequestDto.password());
        User user = new User(username, encodedPassword);
        userRepository.save(user);

        return username;
    }
}
