package app.mapper;

import app.config.MapperConfig;
import app.dto.label.LabelCreateRequestDto;
import app.dto.label.LabelDto;
import app.model.Label;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface LabelMapper {
    @Mapping(target = "projectId", source = "project.id")
    LabelDto toDto(Label label);

    @Mapping(target = "project", ignore = true)
    Label toModel(LabelCreateRequestDto requestDto);
}
