package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private Long wrongId, taskId, projectId;

    private int taskPriority;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();

        Project project = projectRepository.save(new Project("Постройка дома", "строим дом"));

        Task task1 = taskRepository.save(new Task(project,"Купить цемент", "дорогой цементик надо закупить",
                3, false, LocalDate.of(2026, 12, 24)));

        Task task2 = taskRepository.save(new Task(project, "купить доски", "доски", 2,
                true, LocalDate.of(2027, 1, 2)));

        wrongId = taskRepository.findAll().stream()
                .mapToLong(Task::getId)
                .max()
                .orElse(0L) + 1;

        taskId = task1.getId();
        projectId = project.getId();
        taskPriority = task1.getPriority();
    }

    @Test
    void getAllTasks_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/tasks")
                .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getTaskById_shouldReturnTask() throws Exception {
        mockMvc.perform(get("/api/tasks/{id}", taskId)
                .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Купить цемент"))
                .andExpect(jsonPath("$.description").value("дорогой цементик надо закупить"));
    }

    @Test
    void getTaskById_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/tasks/{id}", wrongId)
                        .with(user("user").roles("USER")))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTask_shouldReturnTask() throws Exception {
        String json = """
                {
                        "project": { "id": %d },
                        "title": "постройка ракеты",
                        "description": "строим ракету",
                        "priority": 5,
                        "completed": true,
                        "deadline": "2028-12-02"
                }
                """.formatted(projectId);
        mockMvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content(json)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("постройка ракеты"))
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.deadline").value("2028-12-02"));
    }

    @Test
    void updateTask_shouldReturnUpdatedTask() throws Exception {
        String json = """
                {
                        "project": { "id": %d },
                        "title": "постройка ракеты",
                        "description": "строим ракету",
                        "priority": 5,
                        "completed": true,
                        "deadline": "2028-12-02"
                }""".formatted(projectId);
        mockMvc.perform(put("/api/tasks/{id}", taskId).contentType(MediaType.APPLICATION_JSON).content(json)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("постройка ракеты"));
    }

    @Test
    void deleteTask_shouldDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/{id}", taskId).with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/tasks/{id}", taskId).with(user("user").roles("USER")))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTasksCount_shouldReturnTasksCount() throws Exception {
        mockMvc.perform(get("/api/tasks/count").with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(2));
    }

    @Test
    void getCompletedTasks_shouldReturnOne() throws Exception {
        mockMvc.perform(get("/api/tasks/completed").with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getIncompleteTasks_shouldReturnOne() throws Exception {
        mockMvc.perform(get("/api/tasks/incomplete").with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getTasksByPriority_shouldReturnOne() throws Exception {
        mockMvc.perform(get("/api/tasks/priority/{priority}", taskPriority)
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Купить цемент"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deleteAllTasks_shouldDeleteAllTasks() throws Exception {
        mockMvc.perform(delete("/api/tasks/all").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/tasks").with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void createTask_shouldReturn403ForUser() throws Exception {
        String json = """
                {
                        "project": { "id": %d },
                        "title": "постройка ракеты",
                        "description": "строим ракету",
                        "priority": 5,
                        "completed": true,
                        "deadline": "2028-12-02"
                }""".formatted(projectId);
        mockMvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content(json)
                .with(user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllTasks_shouldReturn401WithoutAuth() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isUnauthorized());
    }
}
