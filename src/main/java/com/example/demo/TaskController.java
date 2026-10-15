package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDto> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id:\\d+}")
    public TaskDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    public TaskDto createTask(@Valid @RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    public TaskDto updateTask(@PathVariable Long id, @Valid @RequestBody Task newTask) {
        return taskService.updateTask(id, newTask);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @DeleteMapping("/all")
    public void deleteAllTasks() {
        taskService.deleteAllTasks();
    }

    @GetMapping("/count")
    public Long getTasksCount() {
        return taskService.getTasksCount();
    }

    @GetMapping("/priority/{priority}")
    public List<TaskDto> getTasksByPriority(@PathVariable int priority) {
        return taskService.getTasksByPriority(priority);
    }

    @GetMapping("/completed")
    public List<TaskDto> getCompletedTasks() {
        return taskService.getTasksByCompleted(true);
    }

    @GetMapping("/incomplete")
    public List<TaskDto> getIncompleteTasks() {
        return taskService.getTasksByCompleted(false);
    }

    @GetMapping("/by-project/{projectId}")
    public List<TaskDto> getTasksByProject(@PathVariable Long projectId) {
        return taskService.getTasksByProject(projectId);
    }

    @GetMapping("/deadline-before/{deadline}")
    public List<TaskDto> getTasksByDeadlineBefore(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                      LocalDate deadline) {
        return taskService.getTasksByDeadlineBefore(deadline);
    }

    @GetMapping("/priority-greater/{priority}")
    public List<TaskDto> getTasksByPriorityGreaterThan(@PathVariable int priority) {
        return taskService.getTasksByPriorityGreaterThan(priority);
    }

    @GetMapping("/search/{part}")
    public List<TaskDto> getTasksByTitleContains(@PathVariable String part) {
        return taskService.getTasksByTitleContains(part);
    }
}
