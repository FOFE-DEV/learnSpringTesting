package com.fofe.learntesting.service;

import com.fofe.learntesting.exeptions.ResourceNotFoundException;
import com.fofe.learntesting.model.Task;
import com.fofe.learntesting.repositories.TaskRepo;
import com.fofe.learntesting.service.impl.TaskServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepo taskRepo;
    @InjectMocks
    private TaskServiceImpl taskService;
    @Test
    //@DisplayName("")
    public void givenTaskObject_whenSave_thenReturnSavedTask(){
        // give -precondition
        Task task=Task.builder()
                .description("description")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        BDDMockito.given(taskRepo.findByDescription(task.getDescription()))
                .willReturn(Optional.empty());
        BDDMockito.given(taskRepo.save(task))
                .willReturn(task);
        // when - action or behavior thant we want to test
        Task task1=taskService.save(task);
        // then verify the input
        Assertions.assertThat(task1).isNotNull();
        Assertions.assertThat(task1.getDescription()).isEqualTo(task.getDescription());
    }


    @Test
    public void givenTaskObject_whenSave_thenThrowException(){
        // give -precondition
        Task task=Task.builder()
                .description("description")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        BDDMockito.given(taskRepo.findByDescription(task.getDescription()))
                .willReturn(Optional.of(task));
        // when - action or behavior thant we want to test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,()->{
            taskService.save(task);
        });
        // then verify the input
        Mockito.verify(taskRepo,Mockito.never()).save(Mockito.any(Task.class));
    }
    @Test
    public void givenListOfTask_whenGetAll_thenReturnTaskList(){
        // give -prevondition
        Task task=Task.builder()
                .description("description")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        Task task1=Task.builder()
                .description("description1")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        BDDMockito.given(taskRepo.findAll()).willReturn(List.of(task,task1));

        // when - action or behavior thant we want to test
        List<Task> taskList=taskService.getAll();
        // then verify the input
        Assertions.assertThat(taskList).isNotNull();
        Assertions.assertThat(taskList.size()).isEqualTo(2);

    }

    @Test
    public void givenListOfTask_whenGetAll_thenReturnEmptyTaskList(){
        // give -prevondition
        BDDMockito.given(taskRepo.findAll()).willReturn(Collections.emptyList());

        // when - action or behavior thant we want to test
        List<Task> taskList=taskService.getAll();
        // then verify the input
        Assertions.assertThat(taskList).isEmpty();
        Assertions.assertThat(taskList.size()).isEqualTo(0);

    }

    @Test
    public void givenTaskId_whenGetByID_thenReturnTask(){
        // give -prevondition
        Task task=Task.builder()
                .description("description")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        BDDMockito.given(taskRepo.findById(1L)).willReturn(Optional.of(task));

        // when - action or behavior thant we want to test
        Task savedTask=taskService.getByid(1L).get();

        // then verify the input
        Assertions.assertThat(savedTask).isNotNull();
    }
    @Test
    public void givenTaskObject_whenUpdateTAsk_thenReturnUpdatedTask(){
        // give -prevondition
        Task task=Task.builder()
                .description("description")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        BDDMockito.given(taskRepo.save(task)).willReturn(task);
        task.setDescription("updated");
        task.setUpdated_at(LocalDateTime.now());
        // when - action or behavior thant we want to test
        Task updatedTask=taskService.update(task);
        // then verify the input
        Assertions.assertThat(updatedTask.getDescription()).isEqualTo(task.getDescription());


    }

    @Test
    public void givenTaskId_whenDeleteTAsk_thenNothing(){
        // give -prevondition
        long taskId=1L;
        BDDMockito.willDoNothing().given(taskRepo).deleteById(taskId);

        // when - action or behavior thant we want to test
        taskService.delete(taskId);
        // then verify the input
        BDDMockito.verify(taskRepo,Mockito.times(1)).deleteById(taskId);

    }

}
