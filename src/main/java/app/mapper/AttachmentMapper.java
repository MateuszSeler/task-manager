package app.mapper;

import app.config.MapperConfig;
import app.dto.attachment.AttachmentResponseDto;
import app.dto.attachment.ExternalAttachmentResponseDto;
import app.model.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface AttachmentMapper {
    @Mapping(target = "taskId", source = "task.id")
    AttachmentResponseDto toDto(Attachment attachment);

    @Mapping(target = "task", ignore = true)
    Attachment toModel(ExternalAttachmentResponseDto requestDto);
}
