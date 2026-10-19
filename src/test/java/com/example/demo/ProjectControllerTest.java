package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    private String name;
    private String description;

    private Long wrongId, projectId;

    @BeforeEach
    void setUp() {
        projectRepository.deleteAll();
        taskRepository.deleteAll();

        Project project = projectRepository.save(new Project("Постройка дома", "строим дом"));

        Task task1 = taskRepository.save(new Task(project,"Купить цемент", "дорогой цементик надо закупить",
                3, false, LocalDate.of(2026, 12, 24)));

        Task task2 = taskRepository.save(new Task(project, "купить доски", "доски", 2,
                true, LocalDate.of(2027, 1, 2)));

        wrongId = projectRepository.findAll().stream()
                .mapToLong(Project::getId)
                .max()
                .orElse(0L) + 1;

        projectId = project.getId();
    }

    @Test
    void getAllProject_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/projects")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Постройка дома"));
    }

    @Test
    void getProjectById_shouldReturnProject() throws Exception {
        mockMvc.perform(get("/api/projects/{id}", projectId)
                .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Постройка дома"))
                .andExpect(jsonPath("$.description").value("строим дом"));
    }

    @Test
    void getProjectById_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/projects/{id}", wrongId)
                        .with(user("user").roles("USER")))
                .andExpect(status().is(404));
    }

    @Test
    void createProject_shouldReturnProject() throws Exception {
        String json =
                """
                {
                "name": "мдауш",
                "description": "мдау"
                }
                """;
        mockMvc.perform(post("/api/projects").contentType(MediaType.APPLICATION_JSON).content(json)
                .with(user("admin").roles("ADMIN")))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value("мдауш"));
    }

    @Test
    void updateProject_shouldReturnUpdatedProject() throws Exception {
        String json =
                """
                {
                "name": "мдауш",
                "description": "мдау"
                }
                """;
        mockMvc.perform(put("/api/projects/{id}", projectId)
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                .with(user("admin").roles("ADMIN")))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value("мдауш"));
    }

    @Test
    void deleteProject_shouldDeleteProject() throws Exception {
        mockMvc.perform(delete("/api/tasks/{id}", projectId).with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/tasks/{id}", projectId).with(user("user").roles("USER")))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProjectsCount_shouldReturnCount() throws Exception {
        mockMvc.perform(get("/api/projects/count")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    void getProjectsByNameContains_shouldReturnOne() throws Exception {
        mockMvc.perform(get("/api/projects/search/дом")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Постройка дома"));
    }

    @Test
    void createProject_shouldReturn403ForUser() throws Exception {
        String json = """
            {
            "name": "мдауш",
            "description": "мдау"
            }
            """;

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllProjects_shouldReturn401WithoutAuth() throws Exception {
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteAllProjects_shouldDeleteAll() throws Exception {
        mockMvc.perform(delete("/api/projects/all")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
