package app.mapper;

import app.config.MapperConfig;
import app.dto.comment.CommentCreateRequestDto;
import app.dto.comment.CommentDto;
import app.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {UserMapper.class})
public interface CommentMapper {
    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "userId", source = "user.id")
    CommentDto toDto(Comment comment);

    @Mapping(target = "task", ignore = true)
    @Mapping(target = "user", ignore = true)
    Comment toModel(CommentCreateRequestDto requestDto);
}
