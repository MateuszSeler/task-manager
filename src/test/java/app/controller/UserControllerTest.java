package app.controller;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.security.test.context.support.WithMockUser;
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
class UserControllerTest {
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
    void getProfile_byOwner_userDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/users/me")
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), UserResponseDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(getExpectedUserDtoFromDb(), actual);
    }

    @Test
    void updateUserProfile() throws Exception {
        UserRegistrationRequestDto requestDto = getUserRegistrationRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        put("/users/me")
                                .header("Authorization", "Bearer " + loginUser().token())
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), UserResponseDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(getUserDtoSavedByTestMethode(), actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getUserById() throws Exception {
        Long userId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        get("/users/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), UserResponseDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(getExpectedUserDtoFromDb(), actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deletingUserById() throws Exception {
        Long userId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        delete("/users/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }

    private UserLoginResponseDto loginUser() throws Exception {
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto()
                .setEmail("jan@gmail.com")
                .setPassword("haslojana");

        String jsonRequest = objectMapper.writeValueAsString(userLoginRequestDto);

        MvcResult mvcLoginResult = mockMvc.perform(
                        post("/authentication/login")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                mvcLoginResult.getResponse().getContentAsString(), UserLoginResponseDto.class);
    }

    private UserResponseDto getExpectedUserDtoFromDb() {
        return new UserResponseDto()
                .setId(1L)
                .setEmail("jan@gmail.com")
                .setFirstName("Jan")
                .setLastName("Nowak");
    }

    private UserRegistrationRequestDto getUserRegistrationRequestDto() {
        return new UserRegistrationRequestDto()
                .setEmail("new@gmail.com")
                .setPassword("hasolJana")
                .setRepeatPassword("hasloJana")
                .setFirstName("Jan")
                .setLastName("Nowak");
    }

    private UserResponseDto getUserDtoSavedByTestMethode() {
        return new UserResponseDto()
                .setId(1L)
                .setEmail("new@gmail.com")
                .setFirstName("Jan")
                .setLastName("Nowak");
    }
}
