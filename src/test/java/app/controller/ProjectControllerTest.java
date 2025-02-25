package app.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.dto.project.ProjectCreateRequestDto;
import app.dto.project.ProjectDto;
import app.dto.user.UserLoginRequestDto;
import app.dto.user.UserLoginResponseDto;
import app.dto.user.UserResponseDto;
import app.repository.TaskRepository;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:sqlqueries/projects-before-method-query-set.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sqlqueries/projects-after-method-query-set.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ProjectControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void createProject_validatedRequestDto_success() throws Exception {
        ProjectCreateRequestDto requestDto = getProjectCreateRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        post("/projects")
                                .header("Authorization", "Bearer " + loginUser().token())
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        ProjectDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), ProjectDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(getProjectDtoSavedByTestMethode(), actual, "id");
    }

    @Test
    void getUsersProjects_existingUserId_setOfTwo() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/projects")
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        Set<ProjectDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Set<ProjectDto>>() {}
        );

        Assertions.assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertTrue(actual.size() == 2);
    }

    @Test
    void getProjectById_existingProjectBelongingToUser_success() throws Exception {
        Long projectId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/projects/{projectId}", projectId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        ProjectDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), ProjectDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(getExpectedProjectDtoFromDb(), actual);
    }

    @Test
    void getProjectById_existingProjectNotBelongingToUser_forbidden() throws Exception {
        Long projectId = 3L;

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/projects/{projectId}", projectId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void updateProjectById_validatedRequestDto_byProjectManager_success() throws Exception {
        Long projectId = 1L;
        ProjectCreateRequestDto requestDto = getProjectCreateRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        put("/projects/" + projectId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        ProjectDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), ProjectDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(getProjectDtoSavedByTestMethode(), actual);
    }

    @Test
    void deleteProjectById_existingProject_byProjectManager_projectAndTasksDeleted()
            throws Exception {
        Long projectId = 2L;
        Long firstTaskId = 1L;
        Long secondTaskId = 2L;

        MvcResult mvcDeleteResult = mockMvc.perform(
                        delete("/projects/" + projectId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();

        assertTrue(taskRepository.findById(firstTaskId).isEmpty());
        assertTrue(taskRepository.findById(secondTaskId).isEmpty());
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

    private ProjectCreateRequestDto getProjectCreateRequestDto() {
        return new ProjectCreateRequestDto()
                .setName("new project")
                .setDescription("new project")
                .setStartDate(LocalDate.of(2025, 2, 26))
                .setEndDate(LocalDate.of(2025, 2, 26));
    }

    private ProjectDto getProjectDtoSavedByTestMethode() {
        return new ProjectDto()
                .setName("new project")
                .setDescription("new project")
                .setStartDate(LocalDate.of(2025, 2, 26))
                .setEndDate(LocalDate.of(2025, 3, 26))
                .setProjectManagers(Set.of(getUserDto()))
                .setProjectMembers(Set.of(getUserDto()));
    }

    private ProjectDto getExpectedProjectDtoFromDb() {
        return new ProjectDto()
                .setId(1L)
                .setName("past project")
                .setDescription("description of Jan past project")
                .setStartDate(LocalDate.of(2025, 1, 26))
                .setEndDate(LocalDate.of(2025, 2, 26))
                .setProjectManagers(Set.of(getUserDto()))
                .setProjectMembers(Set.of(getUserDto()));
    }

    private UserResponseDto getUserDto() {
        return new UserResponseDto()
                .setId(1L)
                .setEmail("jan@gmail.com")
                .setFirstName("Jan")
                .setLastName("Nowak");
    }
}
