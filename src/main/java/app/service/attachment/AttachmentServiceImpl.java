package app.service.attachment;

import app.dto.attachment.AttachmentResponseDto;
import app.dto.attachment.ExternalAttachmentResponseDto;
import app.exception.EntityNotFoundException;
import app.mapper.AttachmentMapper;
import app.model.Attachment;
import app.model.Task;
import app.repository.AttachmentRepository;
import app.repository.TaskRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;
    private final TaskRepository taskRepository;
    private final FileStorageProviderFactory providerFactory;

    @Override
    public AttachmentResponseDto uploadFile(Long taskId, MultipartFile file, String apiName) {
        FileStorageProvider fileStorageProvider = providerFactory.getProvider(apiName);
        ExternalAttachmentResponseDto externalAttachmentResponseDto =
                fileStorageProvider.uploadFile(file);

        Attachment attachment = attachmentMapper.toModel(externalAttachmentResponseDto)
                .setTask(getTaskByIdOrThrowEntityNotFoundException(taskId))
                .setApiName(apiName);

        return attachmentMapper.toDto(attachmentRepository.save(attachment));
    }

    @Override
    public byte[] downloadFile(Long attachmentId) {
        FileStorageProvider fileStorageProvider = providerFactory.getProvider(
                getAttachmentByIdOrThrowEntityNotFoundException(attachmentId).getApiName());

        String filePath =
                getAttachmentByIdOrThrowEntityNotFoundException(attachmentId)
                        .getFilePath();

        return fileStorageProvider.downloadFile(String.valueOf(filePath));
    }

    @Override
    public void deleteLabelById(Long attachmentId) {
        FileStorageProvider fileStorageProvider = providerFactory.getProvider(
                getAttachmentByIdOrThrowEntityNotFoundException(attachmentId).getApiName());

        String filePath =
                getAttachmentByIdOrThrowEntityNotFoundException(attachmentId)
                        .getFilePath();

        fileStorageProvider.deleteFile(String.valueOf(filePath));
    }

    private Task getTaskByIdOrThrowEntityNotFoundException(@NotNull Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException("Task with id: " + taskId + " not found"));
    }

    private Attachment getAttachmentByIdOrThrowEntityNotFoundException(@NotNull Long attachmentId) {
        return attachmentRepository.findById(attachmentId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Attachment with id: " + attachmentId + " not found"));
    }
}
