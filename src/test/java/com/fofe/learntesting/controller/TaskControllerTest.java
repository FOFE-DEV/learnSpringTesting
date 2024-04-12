package com.fofe.learntesting.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fofe.learntesting.model.Task;
import com.fofe.learntesting.service.TaskService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void givenTask_whenCreateTask_thenReturnSavedTask() throws Exception {
        // give -precondition
        Task task=Task.builder()
                .description("description")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        BDDMockito.given(taskService.save(ArgumentMatchers.any(Task.class)))
                .willAnswer((invocation ->invocation.getArgument(0)));

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
        BDDMockito.given(taskService.getAll()).willReturn(taskList);
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
        BDDMockito.given(taskService.getByid(taskId)).willReturn(Optional.of(task));
        // when - action or behavior thant we want to test
        ResultActions response= mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{id}",taskId));
        // then verify the input
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description",CoreMatchers.is(task.getDescription())));

    }

    @Test
    public void givenInvalidTaskId_whenGetTaskById_thenReturnEmpty() throws Exception {
        // give -precondition
        long taskId=1l;
        Task task=Task.builder()
                .description("description")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        BDDMockito.given(taskService.getByid(taskId)).willReturn(Optional.empty());
        // when - action or behavior thant we want to test
        ResultActions response= mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{id}",taskId));
        // then verify the input
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void givenUpdatedTask_whenUpdateTask_thenUpdateTaskObject() throws Exception {
        // give -precondition
        long taskId=1L;
        Task SavedTask=Task.builder()
                .description("description")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        Task UpdatedTask=Task.builder()
                .description("description updated")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();


        BDDMockito.given(taskService.getByid(taskId)).willReturn(Optional.of(SavedTask));
        BDDMockito.given(taskService.update(UpdatedTask)).willAnswer((invocation)->invocation.getArgument(0));

        // when - action or behavior thant we want to test
        ResultActions response= mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/{id}",taskId)
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
        Task UpdatedTask=Task.builder()
                .description("description updated")
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();


        BDDMockito.given(taskService.getByid(taskId)).willReturn(Optional.empty());
        //BDDMockito.given(taskService.update(UpdatedTask)).willAnswer((invocation)->invocation.getArgument(0));

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
        long taskId=1L;
        Task task=Task.builder()
                .description("description")
                .id(taskId)
                .create_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        BDDMockito.given(taskService.getByid(taskId)).willReturn(Optional.of(task));
        BDDMockito.willDoNothing().given(taskService).delete(taskId);
        // when - action or behavior thant we want to test
        ResultActions response= mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/{id}",taskId));

        // then verify the input
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


}
