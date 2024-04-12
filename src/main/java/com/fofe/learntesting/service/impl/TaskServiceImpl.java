package com.fofe.learntesting.service.impl;

import com.fofe.learntesting.exeptions.ResourceNotFoundException;
import com.fofe.learntesting.model.Task;
import com.fofe.learntesting.repositories.TaskRepo;
import com.fofe.learntesting.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class TaskServiceImpl implements TaskService {
    private TaskRepo taskRepo;

    public TaskServiceImpl(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    @Override
    public Task save(Task task) {
        Optional<Task> savedTask=taskRepo.findByDescription(task.getDescription());
        if (savedTask.isPresent()){
            throw new ResourceNotFoundException("Task already exist with given description: "+ task.getDescription());
        }
        return taskRepo.save(task);
    }

    @Override
    public Task update(Task task) {
        return taskRepo.save(task);
    }

    @Override
    public void delete(long id) {
        taskRepo.deleteById(id);
    }

    @Override
    public Optional<Task> getByid(long id) {
        return taskRepo.findById(id);
    }

    @Override
    public List<Task> getAll() {
        return taskRepo.findAll();
    }
}
