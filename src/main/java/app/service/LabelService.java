package app.service;

import app.dto.label.LabelCreateRequestDto;
import app.dto.label.LabelDto;
import app.dto.label.LabelUpdateRequestDto;
import jakarta.validation.Valid;
import java.util.Set;

public interface LabelService {

    LabelDto createLabel(@Valid Long projectId, @Valid LabelCreateRequestDto requestDto);

    Set<LabelDto> getLabelsFromProject(@Valid Long projectId);

    LabelDto updateLabel(@Valid Long labelId, @Valid LabelUpdateRequestDto requestDto);

    void deleteLabelById(@Valid Long labelId);

}
