package com.fofe.learntesting.repositories;

import com.fofe.learntesting.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryITest {
    @Autowired
    private TaskRepo taskRepo;
    //Junit test for save Task operation

    @Test
    public void  givenTaskObject_whenSave_thenReturnSavedTask(){
        // given - precondition or setup
        Task task= Task.builder()
                .description("description one ")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();

        // when - action or the  beahvior that we are going to test

        Task savedTask=taskRepo.save(task);

        // then - verify the output
        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getId()).isGreaterThan(0);
    }

    //Junit test for list Task operation
    @Test
    public  void givenTaskList_whenFindAll_thenReturnTaskList(){
        // given
        Task task= Task.builder()
                .description("desk1")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        Task task1= Task.builder()
                .description("desk 2")
                .updated_at(LocalDateTime.now())
                .create_at(LocalDateTime.now())
                .build();
        taskRepo.save(task);
        taskRepo.save(task1);
        // when
        List<Task> taskList=taskRepo.findAll();
        //then
        assertThat(taskList).isNotNull();
        assertThat(taskList.size()).isEqualTo(2);
    }

    //Junit test for get task by id operation
    @Test
    public void givenTaskObject_whenFindById_thenReturnTaskObject(){
        //given
        Task task= Task.builder()
                .description("desk1")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        taskRepo.save(task);
        // when
        Task taskFormBD= taskRepo.findById(task.getId()).get();
        // then
        assertThat(taskFormBD).isNotNull();
        assertThat(taskFormBD.getId()).isEqualTo(task.getId());
    }

    //Junit test for get task by description operation
    @Test
    public void givenTaskObject_whenFindBydescription_thenReturnTaskObject(){
        //given
        Task task= Task.builder()
                .description("description")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        taskRepo.save(task);
        // when
        Task taskFormBD= taskRepo.findByDescription(task.getDescription()).get();
        // then
        assertThat(taskFormBD).isNotNull();
        assertThat(taskFormBD.getDescription()).isEqualTo(task.getDescription());
    }

    //Junit test for delete task by description operation
    @Test
    public void givenTaskObject_whenDeleteTask_thenRemoveRask(){
        //given
        Task task= Task.builder()
                .description("description")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        taskRepo.save(task);
        // when
        taskRepo.delete(task);
        Optional<Task> taskOptional= taskRepo.findById(task.getId());
        // then
        assertThat(taskOptional).isEmpty();
    }

    @BeforeEach
    void setup(){
        taskRepo.deleteAll();
    }
}
