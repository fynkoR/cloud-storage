package ru.fynkor.cloud_storage.services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import ru.fynkor.cloud_storage.AbstractIntegrationTest;
import ru.fynkor.cloud_storage.dto.UserRequestDto;
import ru.fynkor.cloud_storage.entities.User;
import ru.fynkor.cloud_storage.repositories.UserRepository;
import ru.fynkor.cloud_storage.security.CustomUserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class AuthServiceTest extends AbstractIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private RegistrationService registrationService;

    @BeforeEach
    public void createUser(){
        UserRequestDto user = new UserRequestDto("username", "password");
        registrationService.registration(user);
    }

    @Test
    public void authenticationCorrectUser(){
        UserRequestDto userRequestDto = new UserRequestDto("username", "password");
        String username = authService.performAuthentication(userRequestDto);
        assertThat(username).isEqualTo(userRequestDto.username());
    }

    @Test
    public void authenticationUserIfInUsernameError(){
        UserRequestDto userRequestDto = new UserRequestDto("username_no_correct", "password");
        assertThatThrownBy(() -> {
            authService.performAuthentication(userRequestDto);
        }).isInstanceOf(BadCredentialsException.class);
    }

    @Test
    public void authenticationUserIfInPasswordError(){
        UserRequestDto userRequestDto = new UserRequestDto("username", "password_no_correct");
        assertThatThrownBy(() -> {
            authService.performAuthentication(userRequestDto);
        }).isInstanceOf(BadCredentialsException.class);
    }

    @Test
    public void shouldBeFailTestIfDtoIsNull(){
        assertThatThrownBy(() -> {
            authService.performAuthentication(null);
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldAfterAuthInSecurityContextLocatedAuthentication(){
        UserRequestDto user = new UserRequestDto("username", "password");
        authService.performAuthentication(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        assertThat(user.username()).isEqualTo(customUserDetails.getUsername());
    }


    // TODO: доделать тест для logout

    /*@Test
    public void testLogout(){
        UserRequestDto user = new UserRequestDto("username", "password");
        authService.performAuthentication(user);
        authService.performLogout(MockHttpServletRequest , null, SecurityContextHolder.getContext().getAuthentication());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }*/
}
