package ru.fynkor.cloud_storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fynkor.cloud_storage.dto.CreateUserDto;
import ru.fynkor.cloud_storage.exceptions.UserYetExistsException;
import ru.fynkor.cloud_storage.repositories.UserRepository;

@Service
public class RegistrationService {

    private final UserRepository userRepository;

    @Autowired
    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // TODO: необходимо реализовать метод регистрации пользователя
    @Transactional
    public String registration(CreateUserDto createUserDto){
        // TODO: добавить сам Spring Security
        String username = createUserDto.username();
        if(userRepository.existsUserByUsername(username)){
            throw new UserYetExistsException(username);
        }

    }
}
