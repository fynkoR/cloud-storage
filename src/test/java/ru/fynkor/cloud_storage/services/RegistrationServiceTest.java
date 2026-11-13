package ru.fynkor.cloud_storage.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.fynkor.cloud_storage.AbstractIntegrationTest;
import ru.fynkor.cloud_storage.dto.UserRequestDto;
import ru.fynkor.cloud_storage.exceptions.UserYetExistsException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class RegistrationServiceTest extends AbstractIntegrationTest {

    @Autowired
    private RegistrationService registrationService;

    @Test
    public void registrationCorrectUser(){
        UserRequestDto userRequestDto = new UserRequestDto("username", "password");
        String username = registrationService.registration(userRequestDto);
        assertThat(username).isEqualTo(userRequestDto.username());
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
}
