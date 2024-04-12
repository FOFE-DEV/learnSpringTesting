package com.fofe.learntesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fofe.learntesting.model.Task;
import com.fofe.learntesting.repositories.TaskRepo;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskControllerITest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private TaskRepo taskRepo;
  @Autowired
  private ObjectMapper objectMapper;
  @BeforeEach
    void setup(){
      taskRepo.deleteAll();
  }

    @Test
    public void givenTask_whenCreateTask_thenReturnSavedTask() throws Exception {
        // give -precondition
        Task task=Task.builder()
                .description("description")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();

        // when - action or behavior thant we want to test
        ResultActions response= mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)));
        // then verify the input
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(task.getDescription())));
    }

    @Test
    public void givenListOfTask_whenGetALL_thenReturnTaskList() throws Exception {
        // give -precondition
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
        List<Task> taskList=new ArrayList<>();
        taskList.add(task);
        taskList.add(task1);
        taskRepo.saveAll(taskList);
        // when - action or behavior thant we want to test
        ResultActions responce= mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks"));

        // then verify the input
        responce.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",CoreMatchers.is(taskList.size())));

    }

    @Test
    public void givenTaskId_whenGetTaskById_thenTaskObject() throws Exception {
        // give -precondition
        long taskId=1l;
        Task task=Task.builder()
                .description("description")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        taskRepo.save(task);

        // when - action or behavior thant we want to test
        ResultActions response= mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{id}",task.getId()));
        // then verify the input
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description",CoreMatchers.is(task.getDescription())));

    }

    @Test
    public void givenUpdatedTask_whenUpdateTask_thenUpdateTaskObject() throws Exception {
        // give -precondition
        Task SavedTask=Task.builder()
                .description("description")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        taskRepo.save(SavedTask);
        Task UpdatedTask=Task.builder()
                .description("description updated")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();

        // when - action or behavior thant we want to test
        ResultActions response= mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/{id}",SavedTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UpdatedTask)));

        // then verify the input
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void givenUpdatedTask_whenUpdateTask_thenReturn404() throws Exception {
        // give -precondition
        long taskId=1L;
        Task SavedTask=Task.builder()
                .description("description")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        taskRepo.save(SavedTask);
        Task UpdatedTask=Task.builder()
                .description("description updated")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();

        // when - action or behavior thant we want to test
        ResultActions response= mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/{id}",taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UpdatedTask)));

        // then verify the input
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void givenTaskId_whenDelete_thenReturn200() throws Exception {
        // give -precondition
        Task task=Task.builder()
                .description("description")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        taskRepo.save(task);
        // when - action or behavior thant we want to test
        ResultActions response= mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/{id}",task.getId()));

        // then verify the input
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

}
