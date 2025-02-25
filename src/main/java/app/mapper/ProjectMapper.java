package app.mapper;

import app.config.MapperConfig;
import app.dto.project.ProjectCreateRequestDto;
import app.dto.project.ProjectDto;
import app.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {UserMapper.class})
public interface ProjectMapper {
    @Mapping(source = "projectMembers", target = "projectMembers",
            qualifiedByName = "fromUsersToUserResponseDtos")
    @Mapping(source = "projectManagers", target = "projectManagers",
            qualifiedByName = "fromUsersToUserResponseDtos")
    ProjectDto toDto(Project project);

    Project toModel(ProjectCreateRequestDto projectCreateRequestDto);

    @Mapping(source = "projectMembers", target = "projectMembers",
            qualifiedByName = "fromUserResponseDtosToUsers")
    @Mapping(source = "projectManagers", target = "projectManagers",
            qualifiedByName = "fromUserResponseDtosToUsers")
    Project toModel(ProjectDto projectDto);
}
