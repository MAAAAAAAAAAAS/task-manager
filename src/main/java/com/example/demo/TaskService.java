package com.example.demo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public TaskDto toDto(Task task) {
        return new TaskDto(task.getId(),
                task.getProject() != null ? task.getProject().getName() : null,
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.isCompleted(),
                task.getDeadline());
    }

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));
        return toDto(task);
    }

    public TaskDto createTask(Task task) {
        if (task.getProject() != null && task.getProject().getId() != null) {
            Project project = projectRepository.findById(task.getProject().getId())
                    .orElseThrow(() -> new RuntimeException("Проект не найден"));
            task.setProject(project);
        }
        Task saved = taskRepository.save(task);
        return toDto(saved);
    }

    public TaskDto updateTask(Long id, Task newTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        if (newTask.getProject() != null && newTask.getProject().getId() != null) {
            Project project = projectRepository.findById(newTask.getProject().getId())
                    .orElseThrow(() -> new RuntimeException("Проект не найден"));
            task.setProject(project);
        }

        task.setTitle(newTask.getTitle());
        task.setDescription(newTask.getDescription());
        task.setPriority(newTask.getPriority());
        task.setCompleted(newTask.isCompleted());
        task.setDeadline(newTask.getDeadline());

        Task saved = taskRepository.save(task);
        return toDto(saved);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<TaskDto> getTasksByPriority(int priority) {
        return taskRepository.findByPriority(priority).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByCompleted(boolean completed) {
        return taskRepository.findByCompleted(completed).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByDeadlineBefore(LocalDate deadline) {
        return taskRepository.findByDeadlineBefore(deadline).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByPriorityGreaterThan(int priority) {
        return taskRepository.findByPriorityGreaterThan(priority).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByTitleContains(String part) {
        return taskRepository.findTaskByTitleContains(part).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Long getTasksCount() {
        return taskRepository.count();
    }

    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }
}
