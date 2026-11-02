package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectDto> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id:\\d+}")
    public ProjectDto getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PostMapping
    public ProjectDto createProject(@Valid @RequestBody Project project) {
        return projectService.createProject(project);
    }

    @PutMapping("/{id}")
    public ProjectDto updateProject(@PathVariable Long id, @Valid @RequestBody Project newProject) {
        return projectService.updateProject(id, newProject);
    }

    @DeleteMapping("/{id:\\d+}")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }

    @DeleteMapping("/all")
    public void deleteAllProjects() {
        projectService.deleteAllProjects();
    }

    @GetMapping("/count")
    public Long getProjectsCount() {
        return projectService.getProjectsCount();
    }

    @GetMapping("/search/{part}")
    public List<ProjectDto> getProjectsByNameContains(@PathVariable String part) {
        return projectService.getProjectByNameContains(part);
    }
}
