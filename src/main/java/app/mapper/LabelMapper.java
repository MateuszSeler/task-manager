package app.mapper;

import app.config.MapperConfig;
import app.dto.label.LabelCreateRequestDto;
import app.dto.label.LabelDto;
import app.model.Label;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface LabelMapper {
    LabelDto toDto(Label label);

    Label toModel(LabelCreateRequestDto requestDto);
}
