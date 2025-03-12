package app.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.dto.comment.CommentCreateRequestDto;
import app.dto.comment.CommentDto;
import app.dto.user.UserLoginRequestDto;
import app.dto.user.UserLoginResponseDto;
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
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:sqlqueries/comments-before-method-query-set.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sqlqueries/comments-after-method-query-set.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CommentControllerTest {
    protected static MockMvc mockMvc;
    private final WebClient webClient = WebClient.create("http://localhost:8025");

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
    void createComment_validatedRequestDto_byProjectMember_success() throws Exception {
        Long projectId = 1L;
        Long taskId = 1L;
        CommentCreateRequestDto requestDto = getCommentCreateRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        post("/projects/{projectId}/tasks/{taskId}/comments/", projectId, taskId)
                                .header("Authorization", "Bearer " + loginMember().token())
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CommentDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CommentDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(
                getCommentDtoSavedByTestMethode(), actual, "id", "timestamp");

        //NOTIFICATION
        String notificationMsg = "NEW PROJECT: comment has been added";
        String emailResponse = webClient.get()
                .uri("/api/v2/messages")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        assertTrue(emailResponse.contains(notificationMsg));
    }

    @Test
    void getCommentsFromTask_byProjectManager_setOfOne() throws Exception {
        Long projectId = 1L;
        Long taskId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        get("/projects/{projectId}/tasks/{taskId}/comments/", projectId, taskId)
                                .header("Authorization", "Bearer " + loginManager().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        Set<CommentDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Set<CommentDto>>() {}
        );

        Assertions.assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertTrue(actual.size() == 1);
    }

    @Test
    void deleteCommentById_byProjectMember_success() throws Exception {
        Long projectId = 1L;
        Long taskId = 1L;
        Long commentId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        delete("/projects/{projectId}/tasks/{taskId}/comments/{commentId}",
                                projectId, taskId, commentId)
                                .header("Authorization", "Bearer " + loginManager().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();

        //NOTIFICATION
        String notificationMsg = "NEW PROJECT: comment has been deleted";
        String emailResponse = webClient.get()
                .uri("/api/v2/messages")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        assertTrue(emailResponse.contains(notificationMsg));
    }

    @Test
    void deleteCommentById_byProjectMember_forbidden() throws Exception {
        Long projectId = 1L;
        Long taskId = 1L;
        Long commentId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        delete("/projects/{projectId}/tasks/{taskId}/comments/{commentId}",
                                projectId, taskId, commentId)
                                .header("Authorization", "Bearer " + loginMember().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void updateComment_byAuthor_success() throws Exception {
        Long projectId = 1L;
        Long taskId = 1L;
        Long commentId = 1L;
        CommentCreateRequestDto requestDto = getCommentCreateRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        put("/projects/{projectId}/tasks/{taskId}/comments/{commentId}",
                                projectId, taskId, commentId)
                                .header("Authorization", "Bearer " + loginManager().token())
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CommentDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CommentDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(getCommentDtoSavedByTestMethode(), actual);

        //NOTIFICATION
        String notificationMsg = "NEW PROJECT: comment has been changed";
        String emailResponse = webClient.get()
                .uri("/api/v2/messages")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        assertTrue(emailResponse.contains(notificationMsg));
    }

    @Test
    void updateComment_byNotAuthor_forbidden() throws Exception {
        Long projectId = 1L;
        Long taskId = 1L;
        Long commentId = 1L;
        CommentCreateRequestDto requestDto = getCommentCreateRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        put("/projects/{projectId}/tasks/{taskId}/comments/{commentId}",
                                projectId, taskId, commentId)
                                .header("Authorization", "Bearer " + loginMember().token())
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andReturn();
    }

    private UserLoginResponseDto loginManager() throws Exception {
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

    private UserLoginResponseDto loginMember() throws Exception {
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto()
                .setEmail("piotr@gmail.com")
                .setPassword("haslopiotra");

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

    private CommentCreateRequestDto getCommentCreateRequestDto() {
        return new CommentCreateRequestDto()
                .setText("new comment")
                .setUserId(1L)
                .setTaskId(1L);

    }

    private CommentDto getCommentDtoSavedByTestMethode() {
        return new CommentDto()
                .setText("new comment")
                .setUserId(1L)
                .setTaskId(1L);
    }
}
