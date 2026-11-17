package ru.fynkor.cloud_storage.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.fynkor.cloud_storage.CloudStorageApplicationTests;
import ru.fynkor.cloud_storage.dto.UserRequestDto;
import ru.fynkor.cloud_storage.entities.User;
import ru.fynkor.cloud_storage.repositories.UserRepository;
import ru.fynkor.cloud_storage.services.AuthService;
import ru.fynkor.cloud_storage.services.RegistrationService;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest extends CloudStorageApplicationTests {

    private static final String AUTH_URI = "/api/auth/sign-in";
    private static final String REGISTRATION_URI = "/api/auth/sign-up";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrationService registrationService;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void registrationWithCorrectUser() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto(
                "username",
                "password"
        );
        Mockito.when(registrationService.registration(userRequestDto)).thenReturn(userRequestDto.username());

        mockMvc.perform(
                post(REGISTRATION_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.username").value(userRequestDto.username()));
    }
    @Test
    public void registrationWithUserAllNull() throws Exception {
        UserRequestDto userRequestDto = doWithUserParametersWith(null, null, REGISTRATION_URI);

        verify(registrationService, never()).registration(userRequestDto);
    }

    @Test
    public void registrationWithUserIfUsernameIsNull() throws Exception {
        UserRequestDto userRequestDto = doWithUserParametersWith(null, "password", REGISTRATION_URI);

        verify(registrationService, never()).registration(userRequestDto);
    }

    @Test
    public void registrationWithUserIfPasswordIsNull() throws Exception {
        UserRequestDto userRequestDto = doWithUserParametersWith("username", null, REGISTRATION_URI);

        verify(registrationService, never()).registration(userRequestDto);
    }

    @Test
    public void registrationWithUserIfUsernameNotValid() throws Exception {
        UserRequestDto userRequestDto = doWithUserParametersWith("12", "password", REGISTRATION_URI);

        verify(registrationService, never()).registration(userRequestDto);
    }

    @Test
    public void authCorrectUser() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto(
                "username",
                "password"
        );

        String username = registrationService.registration(userRequestDto);

        Mockito.when(authService.performAuthentication(userRequestDto)).thenReturn(username);

        mockMvc.perform(
                post(AUTH_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    public void authWithUserAllNull() throws Exception {
        UserRequestDto userRequestDto = doWithUserParametersWith(null, null, AUTH_URI);

        verify(authService, never()).performAuthentication(userRequestDto);
    }

    @Test
    public void authWithUserIfUsernameIsNull() throws Exception {
        UserRequestDto userRequestDto = doWithUserParametersWith(null, "password", AUTH_URI);

        verify(authService, never()).performAuthentication(userRequestDto);
    }

    @Test
    public void authWithUserIfPasswordIsNull() throws Exception {
        UserRequestDto userRequestDto = doWithUserParametersWith("username", null, AUTH_URI);

        verify(authService, never()).performAuthentication(userRequestDto);
    }

    @Test
    public void authWithUserIfUsernameNotValid() throws Exception {
        UserRequestDto userRequestDto = doWithUserParametersWith("12", "password", AUTH_URI);

        verify(authService, never()).performAuthentication(userRequestDto);
    }

    public UserRequestDto doWithUserParametersWith(String username, String password, String uri) throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto(
                username,
                password
        );

        mockMvc.perform(
                        post(uri)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequestDto))
                )
                .andExpect(status().is4xxClientError());

        return userRequestDto;
    }
}
