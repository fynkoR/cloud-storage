package ru.fynkor.cloud_storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fynkor.cloud_storage.dto.CreateUserDto;
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
    public String registration(CreateUserDto createUserDto){
        String username = createUserDto.username();

        if(userRepository.existsUserByUsername(username)){
            throw new UserYetExistsException(username);
        }

        String encodedPassword = passwordEncoder.encode(createUserDto.password());
        User user = new User(username, encodedPassword);
        userRepository.save(user);

        return username;
    }
}
