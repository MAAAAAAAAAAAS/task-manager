package com.example.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Tasks", description = "Task management APIs")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get all Tasks", description = "Returns a list of all tasks")
    @ApiResponse(responseCode = "200", description = "Successfully returned all tasks")
    @GetMapping
    public List<TaskDto> getAllTasks() {
        return taskService.getAllTasks();
    }

    @Operation(summary = "Get Task by id")
    @ApiResponse(responseCode = "200", description = "Task found")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @GetMapping("/{id:\\d+}")
    public TaskDto getTaskById(@Parameter(description = "ID of task to retrieve", required = true)
                                   @PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @Operation(summary = "Create task")
    @ApiResponse(responseCode = "200", description = "Task created")
    @ApiResponse(responseCode = "400", description = "Invalid data")
    @PostMapping
    public TaskDto createTask(@Valid @RequestBody Task task) {
        return taskService.createTask(task);
    }

    @Operation(summary = "Update task")
    @ApiResponse(responseCode = "200", description = "Task updated")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @PutMapping("/{id}")
    public TaskDto updateTask(@PathVariable Long id, @Valid @RequestBody Task newTask) {
        return taskService.updateTask(id, newTask);
    }

    @Operation(summary = "Delete task")
    @ApiResponse(responseCode = "200", description = "Task deleted")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @Operation(summary = "Delete all tasks")
    @DeleteMapping("/all")
    public void deleteAllTasks() {
        taskService.deleteAllTasks();
    }

    @Operation(summary = "Get tasks count")
    @GetMapping("/count")
    public Long getTasksCount() {
        return taskService.getTasksCount();
    }

    @Operation(summary = "Get tasks by priority")
    @ApiResponse(responseCode = "200", description = "Tasks got")
    @ApiResponse(responseCode = "400", description = "Invalid priority")
    @GetMapping("/priority/{priority}")
    public List<TaskDto> getTasksByPriority(@PathVariable int priority) {
        return taskService.getTasksByPriority(priority);
    }

    @Operation(summary = "Get completed tasks")
    @GetMapping("/completed")
    public List<TaskDto> getCompletedTasks() {
        return taskService.getTasksByCompleted(true);
    }

    @Operation(summary = "Get incomplete tasks")
    @GetMapping("/incomplete")
    public List<TaskDto> getIncompleteTasks() {
        return taskService.getTasksByCompleted(false);
    }

    @Operation(summary = "Get tasks by project")
    @GetMapping("/by-project/{projectId}")
    public List<TaskDto> getTasksByProject(@Parameter(description = "ID of project to retrieve", required = true)
                                               @PathVariable Long projectId) {
        return taskService.getTasksByProject(projectId);
    }

    @Operation(summary = "Get tasks by deadline before date")
    @GetMapping("/deadline-before/{deadline}")
    public List<TaskDto> getTasksByDeadlineBefore(@Parameter(description = "date before which we take tasks", required = true)
                                                      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                      LocalDate deadline) {
        return taskService.getTasksByDeadlineBefore(deadline);
    }

    @Operation(summary = "Get tasks by priority greater than")
    @GetMapping("/priority-greater/{priority}")
    public List<TaskDto> getTasksByPriorityGreaterThan(@Parameter(description = "priority grated which we take tasks", required = true)
                                                           @PathVariable int priority) {
        return taskService.getTasksByPriorityGreaterThan(priority);
    }

    @Operation(summary = "Get tasks with name which contains part")
    @GetMapping("/search/{part}")
    public List<TaskDto> getTasksByTitleContains(@Parameter(description = "part name with which we take tasks", required = true)
                                                     @PathVariable String part) {
        return taskService.getTasksByTitleContains(part);
    }
}
