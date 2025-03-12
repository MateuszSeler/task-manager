package app.service;

import app.dto.label.LabelCreateRequestDto;
import app.dto.label.LabelDto;
import app.dto.label.LabelUpdateRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public interface LabelService {

    LabelDto createLabel(@Valid LabelCreateRequestDto requestDto);

    Set<LabelDto> getLabelsFromProject();

    LabelDto updateLabel(@NotNull Long labelId, @Valid LabelUpdateRequestDto requestDto);

    void deleteLabelById(@NotNull Long labelId);

    LabelDto getLabelById(@NotNull Long labelId);

    Set<LabelDto> getLabels();
}
