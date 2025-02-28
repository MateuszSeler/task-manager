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

import app.dto.label.LabelCreateRequestDto;
import app.dto.label.LabelDto;
import app.dto.label.LabelUpdateRequestDto;
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
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:sqlqueries/labels-before-method-query-set.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sqlqueries/labels-after-method-query-set.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LabelControllerTest {
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
    void createLabel_validatedRequestDto_byProjectManager_success() throws Exception {
        Long projectId = 1L;
        LabelCreateRequestDto requestDto = getLabelCreateRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        post("/projects/{projectId}/labels/", projectId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        LabelDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), LabelDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(getLabelDtoSavedByTestMethode(), actual, "id");
    }

    @Test
    void getLabelsFromProject_existingProject_setOfOne() throws Exception {
        Long projectId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        get("/projects/{projectId}/labels/", projectId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        Set<LabelDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Set<LabelDto>>() {}
        );

        Assertions.assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertTrue(actual.size() == 1);
    }

    @Test
    void updateLabels_validatedRequestDto_byProjectManager_success() throws Exception {
        Long projectId = 1L;
        Long labelId = 1L;
        LabelUpdateRequestDto requestDto = getLabelUpdateRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        put("/projects/{projectId}/labels/{labelId}", projectId, labelId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        LabelDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), LabelDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(getLabelDtoSavedByTestMethode(), actual);
    }

    @Test
    void deleteLabelById_success() throws Exception {
        Long projectId = 1L;
        Long labelId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        delete("/projects/{projectId}/labels/{labelId}", projectId, labelId)
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

    private LabelCreateRequestDto getLabelCreateRequestDto() {
        return new LabelCreateRequestDto()
                .setName("new label")
                .setColor("BLUE")
                .setProjectId(1L);
    }

    private LabelDto getLabelDtoSavedByTestMethode() {
        return new LabelDto()
                .setName("new label")
                .setColor("BLUE")
                .setProjectId(1L);
    }

    private LabelUpdateRequestDto getLabelUpdateRequestDto() {
        return new LabelUpdateRequestDto()
                .setName("new label")
                .setColor("BLUE");
    }
}
