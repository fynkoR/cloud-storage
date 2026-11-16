package ru.fynkor.cloud_storage.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.fynkor.cloud_storage.CloudStorageApplicationTests;
import ru.fynkor.cloud_storage.dto.UserRequestDto;
import ru.fynkor.cloud_storage.entities.User;
import ru.fynkor.cloud_storage.exceptions.UserYetExistsException;
import ru.fynkor.cloud_storage.repositories.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class RegistrationServiceTest extends CloudStorageApplicationTests {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void registrationCorrectUser(){
        UserRequestDto userRequestDto = new UserRequestDto("username", "password");
        String username = registrationService.registration(userRequestDto);

        boolean existsUserInDatabase = userRepository.existsUserByUsername(username);

        assertThat(username).isEqualTo(userRequestDto.username());
        assertThat(existsUserInDatabase).isTrue();
    }

    @Test
    public void registrationExistsUser(){
        String username = "username";
        UserRequestDto user1 = new UserRequestDto(username, "password");
        UserRequestDto user2 = new UserRequestDto(username, "password");

        registrationService.registration(user1);

        assertThat(user1.username()).isEqualTo(user2.username());
        assertThatThrownBy(() -> {
            registrationService.registration(user2);
        }).isInstanceOf(UserYetExistsException.class);
    }

    @Test
    public void shouldBeFailTestIfDtoIsNull(){
        assertThatThrownBy(() -> {
            registrationService.registration(null);
        }).isInstanceOf(NullPointerException.class);
    }
}
