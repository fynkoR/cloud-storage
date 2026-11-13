package ru.fynkor.cloud_storage.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = ValidationAutoConfiguration.class)
public class UserRequestDtoTest {

    @Autowired
    private Validator validator;

    public void createDtoAndCheckAsserts(String username, String password, String checkField){
        UserRequestDto userRequestDto = new UserRequestDto(username, password);
        Errors errors = new BeanPropertyBindingResult(userRequestDto, "userRequestDto");

        validator.validate(userRequestDto, errors);

        assertThat(errors.hasErrors()).isTrue();
        assertThat(errors.getFieldError(checkField)).isNotNull();
        assertThatExceptionOfType(MethodArgumentNotValidException.class);
    }
    @Test
    public void shouldFailValidationForUserIfAllFieldsNull(){
        UserRequestDto userRequestDto = new UserRequestDto(null, null);
        Errors errors = new BeanPropertyBindingResult(userRequestDto, "userRequestDto");

        validator.validate(userRequestDto, errors);

        assertThat(errors.hasErrors()).isTrue();
        assertThatExceptionOfType(MethodArgumentNotValidException.class);
    }
    @Test
    public void shouldFailValidationForUserIfUsernameIsNull(){
        createDtoAndCheckAsserts(null, "password", "username");
    }
    @Test
    public void shouldFailValidationForUserIfPasswordIsNull(){
        createDtoAndCheckAsserts("username", null, "password");
    }
    @Test
    public void shouldFailValidationForUserIfUsernameNot3AndNot20Chars(){
        createDtoAndCheckAsserts("BobMarleyIsCoolManAndSmileFace", "password", "username");
    }
    @Test
    public void shouldFailValidationForUserIfUsernameStartWithPunctuationMark(){
        createDtoAndCheckAsserts("_W_O_L_F_", "password", "username");
    }
}
