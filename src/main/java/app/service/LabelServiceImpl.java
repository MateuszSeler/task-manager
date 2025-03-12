package app.service;

import app.dto.label.LabelCreateRequestDto;
import app.dto.label.LabelDto;
import app.dto.label.LabelUpdateRequestDto;
import app.exception.EntityNotFoundException;
import app.mapper.LabelMapper;
import app.model.Label;
import app.model.Project;
import app.model.Task;
import app.repository.LabelRepository;
import app.repository.ProjectRepository;
import app.repository.TaskRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public LabelDto createLabel(@Valid LabelCreateRequestDto requestDto) {
        return labelMapper.toDto(
                labelRepository.save(
                        labelMapper.toModel(requestDto)));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<LabelDto> getLabelsFromProject() {
        return labelRepository.findAll()
                .stream()
                .map(labelMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public LabelDto updateLabel(@NotNull Long labelId, @Valid LabelUpdateRequestDto requestDto) {
        Label updatedLabel = getLabelByIdOrThrowEntityNotFoundException(labelId)
                .setName(requestDto.getName())
                .setColor(Label.Color.valueOf(requestDto.getColor()));

        return labelMapper.toDto(labelRepository.save(updatedLabel));
    }

    @Override
    public void deleteLabelById(@NotNull Long labelId) {
        Label labelToDelete = getLabelByIdOrThrowEntityNotFoundException(labelId);

        Set<Task> tasksMarkedByLabel = taskRepository.findTasksMarkedByLabel(labelId);
        for (Task task : tasksMarkedByLabel) {
            task.getLabels().remove(labelToDelete);
        }

        labelRepository.deleteById(labelId);
    }

    @Override
    @Transactional(readOnly = true)
    public LabelDto getLabelById(@NotNull Long labelId) {
        return labelMapper.toDto(getLabelByIdOrThrowEntityNotFoundException(labelId));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<LabelDto> getLabels() {
        return labelRepository.getAll()
                .stream()
                .map(labelMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    private Project getProjectByIdOrThrowEntityNotFoundException(@NotNull Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Project with id: " + projectId + " not found"));
    }

    @Transactional(readOnly = true)
    private Label getLabelByIdOrThrowEntityNotFoundException(@NotNull Long labelId) {
        return labelRepository.findById(labelId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Label with id: " + labelId + " not found"));
    }
}
