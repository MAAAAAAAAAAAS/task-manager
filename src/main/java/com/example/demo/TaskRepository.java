package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByPriority(int priority);
    List<Task> findByCompleted(boolean completed);
    List<Task> findByProjectId(Long projectId);
    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :part, '%'))")
    List<Task> findTaskByTitleContains(@Param("part") String part);
    List<Task> findByDeadlineBefore(LocalDate deadline);
    List<Task> findByPriorityGreaterThan(int priority);
}
