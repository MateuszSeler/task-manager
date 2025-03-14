package app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import app.exception.DataProcessingException;
import app.mapper.AttachmentMapper;
import app.model.Attachment;
import app.repository.AttachmentRepository;
import app.repository.TaskRepository;
import app.service.attachment.AttachmentServiceImpl;
import app.service.attachment.FileStorageProviderFactory;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class AttachmentServiceImplTest {
    @Mock
    private AttachmentRepository attachmentRepository;
    @Mock
    private AttachmentMapper attachmentMapper;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private FileStorageProviderFactory providerFactory;

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    @Test
    void uploadFile_duplicatedFileName_DataProcessingException() {
        Long taskId = 31L;
        String apiName = "DROPBOX";
        String fileName = "testFile.txt";
        Attachment attachment = new Attachment().setFileName(fileName);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getName()).thenReturn("testFile.txt");

        Mockito.when(attachmentRepository.findByFileName(fileName))
                .thenReturn(Optional.of(attachment));

        DataProcessingException exception =
                assertThrows(DataProcessingException.class,
                        () -> attachmentService.uploadFile(taskId, file, apiName));

        assertEquals("File " + file.getName() + " is already attached to the project",
                exception.getMessage());
    }
}
