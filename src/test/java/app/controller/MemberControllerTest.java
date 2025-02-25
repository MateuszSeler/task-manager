package app.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.dto.user.UserLoginRequestDto;
import app.dto.user.UserLoginResponseDto;
import app.dto.user.UserResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:sqlqueries/members-before-method-query-set.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sqlqueries/members-after-method-query-set.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class MemberControllerTest {
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
    void addUserToProject_notAMemberFromTheProject_byProjectManager_setOfThree() throws Exception {
        Long projectId = 1L;
        Long userId = 2L;

        MvcResult mvcResult = mockMvc.perform(
                        post("/projects/{projectId}/members/{userId}", projectId, userId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        Set<UserResponseDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Set<UserResponseDto>>() {}
        );

        Assertions.assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertTrue(actual.size() == 3);
    }

    @Test
    void addUserToProject_memberFromTheProject_byProjectManager_badRequest() throws Exception {
        Long projectId = 1L;
        Long userId = 3L;

        MvcResult mvcResult = mockMvc.perform(
                        post("/projects/{projectId}/members/{userId}", projectId, userId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void deleteMember_memberFromTheProject_byProjectManager_success() throws Exception {
        Long projectId = 1L;
        Long userId = 3L;

        MvcResult mvcResult = mockMvc.perform(
                        delete("/projects/{projectId}/members/{userId}", projectId, userId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void deleteMember_theOnlyManagerFromTheProject_byProjectManager_badRequest() throws Exception {
        Long projectId = 1L;
        Long userId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        delete("/projects/{projectId}/members/{userId}", projectId, userId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void deleteMember_notAMemberFromTheProject_byProjectManager_badRequest() throws Exception {
        Long projectId = 1L;
        Long userId = 2L;

        MvcResult mvcResult = mockMvc.perform(
                        delete("/projects/{projectId}/members/{userId}", projectId, userId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void makeUserManagerOfTheProject_memberFromTheProject_byProjectManager_setOfTwo()
            throws Exception {
        Long projectId = 1L;
        Long userId = 3L;

        MvcResult mvcResult = mockMvc.perform(
                        post("/projects/{projectId}/members/{userId}/managers", projectId, userId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        Set<UserResponseDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Set<UserResponseDto>>() {}
        );

        Assertions.assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertTrue(actual.size() == 2);
    }

    @Test
    void removeUserFromTheProjectManagerRole_managerFromTheProject_byProjectManager_setOfTwo()
            throws Exception {
        Long projectId = 2L;
        Long userId = 3L;

        MvcResult mvcResult = mockMvc.perform(
                        delete("/projects/{projectId}/members/{userId}/managers", projectId, userId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        Set<UserResponseDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Set<UserResponseDto>>() {}
        );

        Assertions.assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertTrue(actual.size() == 2);
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
}
