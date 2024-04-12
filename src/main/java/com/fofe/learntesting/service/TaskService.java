package com.fofe.learntesting.service;

import com.fofe.learntesting.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface TaskService {
    public Task save(Task task);
    public Task update(Task task);
    public void delete(long id);
    public Optional<Task> getByid(long id);
    public List<Task> getAll();
}
