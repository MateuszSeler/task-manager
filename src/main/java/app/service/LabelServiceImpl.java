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
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Override
    public LabelDto createLabel(Long projectId, LabelCreateRequestDto requestDto) {
        return labelMapper.toDto(labelRepository.save(toEntity(requestDto)));
    }

    @Override
    public Set<LabelDto> getLabelsFromProject(Long projectId) {
        return labelRepository.findLabelsFromTheProject(projectId)
                .stream()
                .map(labelMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public LabelDto updateLabel(Long labelId, LabelUpdateRequestDto requestDto) {
        Label updatedLabel = getLabelByIdOrThrowEntityNotFoundException(labelId)
                .setName(requestDto.getName())
                .setColor(Label.Color.valueOf(requestDto.getColor()));

        return labelMapper.toDto(labelRepository.save(updatedLabel));
    }

    @Override
    public void deleteLabelById(Long labelId) {
        Label labelToDelete = getLabelByIdOrThrowEntityNotFoundException(labelId);

        Set<Task> tasksMarkedByLabel = taskRepository.findTasksMarkedByLabel(labelId);
        for (Task task : tasksMarkedByLabel) {
            task.getLabels().remove(labelToDelete);
        }

        labelRepository.deleteById(labelId);
    }

    private Label toEntity(LabelCreateRequestDto requestDto) {
        Project project = getProjectByIdOrThrowEntityNotFoundException(requestDto.getProjectId());
        return labelMapper.toModel(requestDto).setProject(project);
    }

    private Project getProjectByIdOrThrowEntityNotFoundException(@NotNull Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Project with id: " + projectId + " not found"));
    }

    private Label getLabelByIdOrThrowEntityNotFoundException(@NotNull Long labelId) {
        return labelRepository.findById(labelId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Label with id: " + labelId + " not found"));
    }
}
