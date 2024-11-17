package org.aleksey.springnew.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.aleksey.springnew.dto.TodoCreateDto;
import org.aleksey.springnew.dto.TodoHistoryResponseDto;
import org.aleksey.springnew.dto.TodoResponseDto;
import org.aleksey.springnew.dto.TodoUpdateDto;
import org.aleksey.springnew.service.TodoService;
import org.aleksey.springnew.types.PriorityType;
import org.aleksey.springnew.types.StatusType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(
        controllers = TodoController.class
)
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

    @Test
    void createTodo_ValidRequestDto_Success() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        TodoCreateDto todoCreateDto = new TodoCreateDto();
        todoCreateDto.setTitle("Test Todo");
        todoCreateDto.setDescription("Description Todo");
        todoCreateDto.setDueDate(localDateTime);
        todoCreateDto.setPriority(PriorityType.LOW.toString());

        TodoResponseDto todoResponseDto = new TodoResponseDto();
        todoResponseDto.setId(1L);
        todoResponseDto.setTitle(todoCreateDto.getTitle());
        todoResponseDto.setDescription(todoCreateDto.getDescription());
        todoResponseDto.setPriority(todoCreateDto.getPriority());
        todoResponseDto.setStatus(StatusType.IN_PROGRESS.toString());
        todoResponseDto.setDueDate(todoCreateDto.getDueDate());

        Mockito.when(todoService.create(todoCreateDto)).thenReturn(todoResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoResponseDto.getId()))
                .andExpect(jsonPath("$.title").value(todoResponseDto.getTitle()))
                .andExpect(jsonPath("$.description").value(todoResponseDto.getDescription()))
                .andExpect(jsonPath("$.priority").value(todoResponseDto.getPriority()))
                .andExpect(jsonPath("$.dueDate").value(localDateTime.toString()))
                .andExpect(jsonPath("$.status").value(StatusType.IN_PROGRESS.toString()));
    }

    @Test
    void createTodo_InvalidRequestDto_ShouldReturnBadRequest() throws Exception {
        TodoCreateDto invalidTodoCreateDto = new TodoCreateDto();
        invalidTodoCreateDto.setTitle("");
        invalidTodoCreateDto.setDescription("");

        mockMvc.perform(MockMvcRequestBuilders.post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTodoCreateDto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.dueDate", containsString("must not be null")))
            .andExpect(jsonPath("$.description", containsString("size must be between 1 and 500")))
            .andExpect(jsonPath("$.title", containsString("size must be between 1 and 100")));
    }

    @Test
    void updateTodo_ValidRequestDto_Success() throws Exception {
        Long id = 1L;
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        TodoUpdateDto todoUpdateDto = new TodoUpdateDto();
        todoUpdateDto.setId(id);
        todoUpdateDto.setTitle("Update Title");
        todoUpdateDto.setDescription("Update Description");
        todoUpdateDto.setPriority(PriorityType.HIGH.toString());
        todoUpdateDto.setStatus(StatusType.IN_PROGRESS);
        todoUpdateDto.setDueDate(localDateTime);

        TodoResponseDto todoResponseDto = new TodoResponseDto();
        todoResponseDto.setId(id);
        todoResponseDto.setTitle(todoUpdateDto.getTitle());
        todoResponseDto.setDescription(todoUpdateDto.getDescription());
        todoResponseDto.setPriority(todoUpdateDto.getPriority());
        todoResponseDto.setStatus(StatusType.IN_PROGRESS.toString());
        todoResponseDto.setDueDate(localDateTime);
        todoResponseDto.setUpdatedAt(null);
        todoResponseDto.setCreatedAt(null);

        Mockito.when(todoService.update(id, todoUpdateDto)).thenReturn(todoResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoResponseDto.getId()))
                .andExpect(jsonPath("$.title").value(todoResponseDto.getTitle()))
                .andExpect(jsonPath("$.description").value(todoResponseDto.getDescription()))
                .andExpect(jsonPath("$.dueDate").value(todoResponseDto.getDueDate().toString()))
                .andExpect(jsonPath("$.priority").value(todoResponseDto.getPriority()))
                .andExpect(jsonPath("$.status").value(todoResponseDto.getStatus()));
    }

    @Test
    void updateTodo_InvalidRequest_ShouldReturnBadRequest() throws Exception {
        TodoUpdateDto invalidTodoUpdateDto = new TodoUpdateDto();
        invalidTodoUpdateDto.setTitle("");
        invalidTodoUpdateDto.setDescription("");

        mockMvc.perform(MockMvcRequestBuilders.put("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTodoUpdateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dueDate", containsString("must not be null")))
                .andExpect(jsonPath("$.description", containsString("size must be between 1 and 500")))
                .andExpect(jsonPath("$.title", containsString("size must be between 1 and 100")));
    }

    @Test
    void deleteTodo_ValidRequest_Success() throws Exception {
        Mockito.doNothing().when(todoService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/todos/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }

    @Test
    void deleteTodo_InvalidRequest_ShouldReturnNotFound() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("Can't find todo by id 2")).when(todoService).delete(2L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/todos/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Can't find todo by id 2")));
    }

    @Test
    void getHistoryTodo_ValidRequest_Success() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        TodoHistoryResponseDto todoHistoryResponseDtoFirst = new TodoHistoryResponseDto();
        todoHistoryResponseDtoFirst.setId(1L);
        todoHistoryResponseDtoFirst.setTodoId(1L);
        todoHistoryResponseDtoFirst.setNewState("new state first");
        todoHistoryResponseDtoFirst.setOldState("old state first");
        todoHistoryResponseDtoFirst.setChangeBy(1L);
        todoHistoryResponseDtoFirst.setChangeDate(localDateTime);

        TodoHistoryResponseDto todoHistoryResponseDtoSecond = new TodoHistoryResponseDto();
        todoHistoryResponseDtoSecond.setId(2L);
        todoHistoryResponseDtoSecond.setTodoId(1L);
        todoHistoryResponseDtoSecond.setNewState("new state second");
        todoHistoryResponseDtoSecond.setOldState("old state second");
        todoHistoryResponseDtoSecond.setChangeBy(1L);
        todoHistoryResponseDtoSecond.setChangeDate(localDateTime);
        List<TodoHistoryResponseDto> histories = List.of(todoHistoryResponseDtoFirst, todoHistoryResponseDtoSecond);
        Mockito.when(todoService.getHistory(1L)).thenReturn(histories);

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/1/history")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(histories)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(todoHistoryResponseDtoFirst.getId()))
                .andExpect(jsonPath("$[0].todoId").value(todoHistoryResponseDtoFirst.getTodoId()))
                .andExpect(jsonPath("$[0].oldState").value(todoHistoryResponseDtoFirst.getOldState()))
                .andExpect(jsonPath("$[0].newState").value(todoHistoryResponseDtoFirst.getNewState()))
                .andExpect(jsonPath("$[0].changeDate").value(todoHistoryResponseDtoFirst.getChangeDate().toString()))
                .andExpect(jsonPath("$[0].changeBy").value(todoHistoryResponseDtoFirst.getChangeBy()))
                .andExpect(jsonPath("$[1].id").value(todoHistoryResponseDtoSecond.getId()))
                .andExpect(jsonPath("$[1].todoId").value(todoHistoryResponseDtoSecond.getTodoId()))
                .andExpect(jsonPath("$[1].oldState").value(todoHistoryResponseDtoSecond.getOldState()))
                .andExpect(jsonPath("$[1].newState").value(todoHistoryResponseDtoSecond.getNewState()))
                .andExpect(jsonPath("$[1].changeDate").value(todoHistoryResponseDtoSecond.getChangeDate().toString()))
                .andExpect(jsonPath("$[1].changeBy").value(todoHistoryResponseDtoSecond.getChangeBy()));
    }

    @Test
    void getHistoryTodo_InValidRequest_Success() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("Can't find todo by id 2")).when(todoService).getHistory(2L);

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/2/history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Can't find todo by id 2")));
    }
}
