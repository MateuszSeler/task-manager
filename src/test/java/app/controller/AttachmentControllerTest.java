package app.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.dto.attachment.AttachmentResponseDto;
import app.dto.user.UserLoginRequestDto;
import app.dto.user.UserLoginResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:sqlqueries/attachments-before-method-query-set.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sqlqueries/attachments-after-method-query-set.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AttachmentControllerTest {
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
    void adding_downloading_deleting_testFile_success() throws Exception {
        Long projectId = 1L;
        Long taskId = 1L;
        String apiName = "DropBox";

        //UPLOADING
        MockMultipartFile file = new MockMultipartFile(
                "file", "testFile.txt", MediaType.TEXT_PLAIN_VALUE, "Test".getBytes()
        );

        MvcResult uploadingResult = mockMvc.perform(
                        multipart("/projects/{projectId}/tasks/{taskId}/attachments/{apiName}",
                                projectId, taskId, apiName)
                                .file(file)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        AttachmentResponseDto responseDto = objectMapper.readValue(
                uploadingResult.getResponse().getContentAsString(), AttachmentResponseDto.class);

        Long attachmentId = responseDto.getId();
        assertNotNull(attachmentId);

        //DOWNLOADING
        MvcResult downloadResult = mockMvc.perform(
                get("/projects/{projectId}/tasks/{taskId}/attachments/{attachmentId}",
                        projectId, taskId, attachmentId)
                        .header("Authorization", "Bearer " + loginUser().token())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                )
                .andExpect(status().isOk())
                .andReturn();

        byte[] downloadedBytes = downloadResult.getResponse().getContentAsByteArray();
        assertArrayEquals(file.getBytes(), downloadedBytes);

        //DELETING
        MvcResult deletingResult = mockMvc.perform(
                        delete("/projects/{projectId}/tasks/{taskId}/attachments/{attachmentId}",
                                projectId, taskId, attachmentId)
                                .header("Authorization", "Bearer " + loginUser().token())
                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
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
}
