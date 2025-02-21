package app.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.dto.user.UserLoginRequestDto;
import app.dto.user.UserLoginResponseDto;
import app.dto.user.UserRegistrationRequestDto;
import app.dto.user.UserResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:sqlqueries/users-before-method-query-set.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sqlqueries/users-after-method-query-set.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthenticationControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void register_validatedRequestDto_success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(getRegistrationRequestDto());

        MvcResult mvcLoginResult = mockMvc.perform(
                        post("/authentication/registration")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        UserResponseDto actual = objectMapper.readValue(
                mvcLoginResult.getResponse().getContentAsString(), UserResponseDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(getRegistrationResponseDto(), actual, "id");
    }

    @Test
    void register_wrongEmailFormat_failed() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(
                getRegistrationRequestDto()
                        .setEmail("emailWithWrongFormat"));

        MvcResult mvcLoginResult = mockMvc.perform(
                        post("/authentication/registration")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void register_userAlreadyExisted_failed() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(
                getRegistrationRequestDto()
                        .setEmail("jan@gmail.com"));

        MvcResult mvcLoginResult = mockMvc.perform(
                        post("/authentication/registration")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void login_validatedRequestDto_success() throws Exception {
        UserLoginRequestDto loginRequestDto = getLoginRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(getLoginRequestDto());

        MvcResult mvcLoginResult = mockMvc.perform(
                        post("/authentication/login")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserLoginResponseDto actual = objectMapper.readValue(
                mvcLoginResult.getResponse().getContentAsString(), UserLoginResponseDto.class);
    }

    @Test
    void login_userDoNotExist_failed() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(
                getLoginRequestDto()
                        .setEmail("nonExistingUser@gmail.com"));

        MvcResult mvcLoginResult = mockMvc.perform(
                        post("/authentication/login")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    void login_wrongPassword_failed() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(
                getLoginRequestDto()
                        .setPassword("wrongPassword"));

        MvcResult mvcLoginResult = mockMvc.perform(
                        post("/authentication/login")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    private UserRegistrationRequestDto getRegistrationRequestDto() {
        return new UserRegistrationRequestDto()
                .setEmail("tomasz@gmail.com")
                .setPassword("haslotomasza")
                .setRepeatPassword("haslotomasza")
                .setFirstName("Tomasz")
                .setLastName("Kowalski");
    }

    private UserResponseDto getRegistrationResponseDto() {
        return new UserResponseDto()
                .setEmail("tomasz@gmail.com")
                .setFirstName("Tomasz")
                .setLastName("Kowalski");
    }

    private UserLoginRequestDto getLoginRequestDto() {
        return new UserLoginRequestDto()
                .setEmail("jan@gmail.com")
                .setPassword("haslojana");
    }
}
