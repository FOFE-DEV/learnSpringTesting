package com.fofe.learntesting.repositories;

import com.fofe.learntesting.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepo extends JpaRepository<Task,Long> {
    Optional<Task> findByDescription(String desc);
}
