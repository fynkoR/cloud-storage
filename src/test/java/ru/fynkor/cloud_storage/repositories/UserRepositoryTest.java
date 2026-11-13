package ru.fynkor.cloud_storage.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.fynkor.cloud_storage.AbstractIntegrationTest;
import ru.fynkor.cloud_storage.entities.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser(){
        User user = new User("test", "test");

        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    public void testFindById(){
        User user = new User("Bob Marley", "test");
        User savedUser = userRepository.save(user);

        Optional<User> findUser = userRepository.findUserByUsername(savedUser.getUsername());

        assertThat(findUser).isNotNull();
        assertThat(findUser.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void testExistsUserByUsername(){
        User user = new User("Tony Soprano", "test");
        User savedUser = userRepository.save(user);

        boolean existsSavedUserInDatabase = userRepository.existsUserByUsername(savedUser.getUsername());

        assertThat(existsSavedUserInDatabase).isEqualTo(true);
    }

    @Test
    public void testIfUsernameIsNull(){
        User user = new User(null, "password");

        assertThatThrownBy(() -> {
            userRepository.saveAndFlush(user);
        });
    }

    @Test
    public void testIfPasswordIsNull(){
        User user = new User("username", null);

        assertThatThrownBy(() -> userRepository.saveAndFlush(user));
    }

    @Test
    public void testIfUsernameYetExists(){
        String username = "Killer";
        User user_1 = new User(username, "password_1");
        User user_2 = new User(username, "password_2");

        userRepository.save(user_1);
        assertThatThrownBy(() -> userRepository.saveAndFlush(user_2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testIfUserNotFoundByUsername(){
        Optional<User> user = userRepository.findUserByUsername("Bob Marley");
        assertThat(user.isEmpty());
    }
}
