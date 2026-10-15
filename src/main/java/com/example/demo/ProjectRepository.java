package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :part, '%'))")
    List<Project> findProjectsByNameContains(@Param("part") String part);
}
