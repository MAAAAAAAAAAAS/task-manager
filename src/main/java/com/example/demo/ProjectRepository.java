package com.example.demo;

import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :part, '%'))")
    List<Project> findProjectsByNameContains(@Param("part") String part);

    @EntityGraph(attributePaths = {"tasks"})
    @Query("SELECT p FROM Project p")
    List<Project> findAllWithTasks();
}
