package com.example.demo;

import java.time.LocalDate;

public class TaskDto {
    private Long id;
    private String projectName;
    private String title;
    private String description;
    private int priority;
    private boolean completed;
    private LocalDate deadline;

    public TaskDto(Long id, String projectName, String title, String description,
                   int priority, boolean completed, LocalDate deadline) {
        this.id = id;
        this.projectName = projectName;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.completed = completed;
        this.deadline = deadline;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
}
