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

import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.fynkor.cloud_storage.dto.UserRequestDto;
import ru.fynkor.cloud_storage.services.AuthService;
import ru.fynkor.cloud_storage.services.RegistrationService;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrationService registrationService;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegistrationCorrectUser() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto(
                "username",
                "password"
        );
        Mockito.when(registrationService.registration(userRequestDto)).thenReturn(userRequestDto.username());

        mockMvc.perform(
                post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.username").value(userRequestDto.username()));
    }
    @Test
    public void registrationWithUserAllNull() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto(
                null,
                null
        );

        mockMvc.perform(
                post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                )
                .andExpect(status().is4xxClientError());

        verify(registrationService, never()).registration(userRequestDto);
    }

    @Test
    public void registrationWithUserIfUsernameIsNull() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto(
                null,
                "password"
        );

        mockMvc.perform(
                post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                )
                .andExpect(status().is4xxClientError());

        verify(registrationService, never()).registration(userRequestDto);
    }

    @Test
    public void registrationWithUserIfPasswordIsNull() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto(
                "username",
                null
        );

        mockMvc.perform(
                        post("/api/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequestDto))
                )
                .andExpect(status().is4xxClientError());

        verify(registrationService, never()).registration(userRequestDto);
    }

    @Test
    public void registrationWithUserIfUsernameNotValid() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto(
                "12",
                "password"
        );

        mockMvc.perform(
                        post("/api/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequestDto))
                )
                .andExpect(status().is4xxClientError());

        verify(registrationService, never()).registration(userRequestDto);
    }


}
