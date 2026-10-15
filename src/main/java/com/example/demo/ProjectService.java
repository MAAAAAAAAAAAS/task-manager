package com.example.demo;

import org.springframework.stereotype.Service;

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

    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Проект не найден"));
        return toDto(project);
    }

    public ProjectDto createProject(Project project) {
        Project saved = projectRepository.save(project);
        return toDto(saved);
    }

    public ProjectDto updateProject(Long id, Project newProject) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Проект не найден"));
        project.setName(newProject.getName());
        project.setDescription(newProject.getDescription());
        Project saved = projectRepository.save(project);
        return toDto(saved);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public List<ProjectDto> getProjectByNameContains(String part) {
        return projectRepository.findProjectsByNameContains(part).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Long getProjectsCount() {
        return projectRepository.count();
    }

    public void deleteAllProjects() {
        projectRepository.deleteAll();
    }
}
