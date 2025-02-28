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

import app.dto.task.TaskCreateRequestDto;
import app.dto.task.TaskDto;
import app.dto.task.TaskUpdateRequestDto;
import app.dto.user.UserLoginRequestDto;
import app.dto.user.UserLoginResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
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
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:sqlqueries/tasks-before-method-query-set.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sqlqueries/tasks-after-method-query-set.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TaskControllerTest {
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
    void createTask_validatedRequestDto_byProjectManager_success() throws Exception {
        Long projectId = 2L;
        TaskCreateRequestDto requestDto = getTaskCreateRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        post("/projects/{projectId}/tasks", projectId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        TaskDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), TaskDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(getTaskDtoSavedByTestMethode(), actual, "id");
    }

    @Test
    void createTask_validatedRequestDto_byProjectMember_forbidden() throws Exception {
        Long projectId = 3L;
        TaskCreateRequestDto requestDto = getTaskCreateRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        post("/projects/{projectId}/tasks", projectId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void createTask_wrongPriorityFormat_byProjectManager_BadRequest() throws Exception {
        Long projectId = 2L;
        TaskCreateRequestDto requestDto = getTaskCreateRequestDto();
        requestDto.setPriority("wrongFormat");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        post("/projects/{projectId}/tasks", projectId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void getTasksFromProject_existingProject_setOfTwo() throws Exception {
        Long projectId = 2L;

        MvcResult mvcResult = mockMvc.perform(
                        get("/projects/{projectId}/tasks", projectId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        Set<TaskDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Set<TaskDto>>() {}
        );

        Assertions.assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertTrue(actual.size() == 2);
    }

    @Test
    void getTaskById_existingTask_success() throws Exception {
        Long projectId = 2L;
        Long taskId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        get("/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        TaskDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), TaskDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(getExpectedTaskDtoFromDb(), actual);
    }

    @Test
    void updateTaskById_validatedRequestDto_byProjectManager_success() throws Exception {
        Long projectId = 2L;
        Long taskId = 1L;
        TaskUpdateRequestDto requestDto = getTaskUpdateRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        put("/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        TaskDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), TaskDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(getTaskDtoSavedByTestMethode(), actual);
    }

    @Test
    void deleteTaskById_success() throws Exception {
        Long projectId = 2L;
        Long taskId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        delete("/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void addLabelToTask() throws Exception {
        Long projectId = 1L;
        Long taskId = 1L;
        Long labelId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        post("/projects/{projectId}/tasks/{taskId}/labels/{labelId}",
                                projectId, taskId, labelId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void removeLabelFromTask() throws Exception {
        Long projectId = 1L;
        Long taskId = 2L;
        Long labelId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        delete("/projects/{projectId}/tasks/{taskId}/labels/{labelId}",
                                projectId, taskId, labelId)
                                .header("Authorization", "Bearer " + loginUser().token())
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

    private TaskCreateRequestDto getTaskCreateRequestDto() {
        return new TaskCreateRequestDto()
                .setName("new task")
                .setDescription("new task")
                .setPriority("HIGH")
                .setDueDate(LocalDate.of(2025, 2, 26))
                .setProjectId(1L)
                .setAssigneeId(1L);
    }

    private TaskUpdateRequestDto getTaskUpdateRequestDto() {
        return new TaskUpdateRequestDto()
                .setName("new task")
                .setDescription("new task")
                .setPriority("HIGH")
                .setStatus("IN_PROGRESS")
                .setDueDate(LocalDate.of(2025, 2, 26))
                .setAssigneeId(1L);
    }

    private TaskDto getTaskDtoSavedByTestMethode() {
        return new TaskDto()
                .setName("new task")
                .setDescription("new task")
                .setPriority("HIGH")
                .setDueDate(LocalDate.of(2025, 2, 26))
                .setProjectId(1L)
                .setAssigneeId(1L);
    }

    private TaskDto getExpectedTaskDtoFromDb() {
        return new TaskDto()
                .setId(1L)
                .setName("first task")
                .setDescription("description of first task in new project")
                .setPriority("MEDIUM")
                .setStatus("IN_PROGRESS")
                .setDueDate(LocalDate.of(2025, 3, 26))
                .setProjectId(2L)
                .setAssigneeId(1L);
    }
}
