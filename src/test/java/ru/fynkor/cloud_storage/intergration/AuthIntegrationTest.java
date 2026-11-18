package ru.fynkor.cloud_storage.intergration;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import ru.fynkor.cloud_storage.dto.UserRequestDto;
import ru.fynkor.cloud_storage.entities.User;
import ru.fynkor.cloud_storage.security.CustomUserDetails;
import ru.fynkor.cloud_storage.services.AuthService;
import ru.fynkor.cloud_storage.services.RegistrationService;
import tools.jackson.databind.ObjectMapper;
import ru.fynkor.cloud_storage.CloudStorageApplicationTests;
import ru.fynkor.cloud_storage.repositories.UserRepository;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthIntegrationTest extends CloudStorageApplicationTests {

    private static final String AUTH_URI = "/api/auth/sign-in";
    private static final String REGISTRATION_URI = "/api/auth/sign-up";
    private static final String LOGOUT_URI = "/api/auth/sign-out";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private AuthService authService;


    @Test
    public void registrationYetExistsUser() throws Exception {
        String username = "username";
        String password = "password";

        User user = new User(username, password);
        userRepository.saveAndFlush(user);

        UserRequestDto userRequestDto = new UserRequestDto(
                username,
                password
        );

        mockMvc.perform(
                        post("/api/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequestDto))
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void registryAndLogin() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto(
                "username",
                "password"
        );

        mockMvc.perform(
                        post(REGISTRATION_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequestDto))
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.username").value(userRequestDto.username()));

        mockMvc.perform(
                        post(AUTH_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequestDto))
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.username").value(userRequestDto.username()));
    }
}
