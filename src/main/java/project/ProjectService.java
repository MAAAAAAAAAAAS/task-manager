package project;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectDto toDto(Project project) {
        return new ProjectDto(project.getId(), project.getName(), project.getDescription());
    }

    @Transactional(readOnly = true)
    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Проект не найден"));
        return toDto(project);
    }

    @Transactional
    public ProjectDto createProject(Project project) {
        Project saved = projectRepository.save(project);
        return toDto(saved);
    }

    @Transactional
    public ProjectDto updateProject(Long id, Project newProject) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Проект не найден"));
        project.setName(newProject.getName());
        project.setDescription(newProject.getDescription());
        Project saved = projectRepository.save(project);
        return toDto(saved);
    }

    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectByNameContains(String part) {
        return projectRepository.findProjectsByNameContains(part).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long getProjectsCount() {
        return projectRepository.count();
    }

    @Transactional
    public void deleteAllProjects() {
        projectRepository.deleteAll();
    }
}
