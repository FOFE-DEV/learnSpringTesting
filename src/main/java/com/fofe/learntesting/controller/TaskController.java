package com.fofe.learntesting.controller;

import com.fofe.learntesting.model.Task;
import com.fofe.learntesting.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private TaskService service;

    public TaskController(TaskService service, TaskService taskService) {
        this.service = service;
        this.taskService = taskService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody Task task){
        return  taskService.save(task);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Task> getAllTasks(){
        return  taskService.getAll();
    }
    @GetMapping("{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") long id){
        return taskService.getByid(id)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }
    @PutMapping("{id}")
    public ResponseEntity<Task> updateTAsk(@PathVariable("id") Long taskId,@RequestBody Task task){

        return taskService.getByid(taskId).map(
                savedTask->{
                    savedTask.setDescription(task.getDescription());
                    savedTask.setUpdated_at(LocalDateTime.now());
                   Task updatedTAsk= taskService.update(savedTask);
                   return new ResponseEntity<>(updatedTAsk,HttpStatus.OK);
                }
        ).orElseGet(()->ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") long taskId){
//        taskService.delete(taskId);
//        return  new ResponseEntity<String>("Task deleted successfully!!",HttpStatus.OK);
        return taskService.getByid(taskId).map(
                savedTask->{
                    taskService.delete(savedTask.getId());
                    return  new ResponseEntity<String>("Task deleted successfully!!",HttpStatus.OK);
                }
        ).orElseGet(()->new ResponseEntity<>("Task with provided id not found ",HttpStatus.NOT_FOUND));
    }

    @DeleteMapping()
    public ResponseEntity<String> delete(@RequestBody Task task){


        return new ResponseEntity<>("Task with provided id not found ",HttpStatus.NOT_FOUND);
    }
}
