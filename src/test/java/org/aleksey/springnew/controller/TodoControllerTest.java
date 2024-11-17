package org.aleksey.springnew.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aleksey.springnew.dto.TodoCreateDto;
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
        todoResponseDto.setTitle("Test Todo");
        todoResponseDto.setDescription("Description Todo");
        todoResponseDto.setPriority(PriorityType.LOW.toString());
        todoResponseDto.setStatus(StatusType.IN_PROGRESS.toString());
        todoResponseDto.setDueDate(localDateTime);

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
        TodoCreateDto invalidRequest = new TodoCreateDto();
        invalidRequest.setTitle("");
        invalidRequest.setDescription("");

        mockMvc.perform(MockMvcRequestBuilders.post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andDo(print()) // Print the response for debugging
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
        todoResponseDto.setTitle("Update Title");
        todoResponseDto.setDescription("Update Description");
        todoResponseDto.setPriority(PriorityType.HIGH.toString());
        todoResponseDto.setStatus(StatusType.IN_PROGRESS.toString());
        todoResponseDto.setDueDate(localDateTime);
        todoResponseDto.setUpdatedAt(localDateTime);
        todoResponseDto.setCreatedAt(localDateTime);

        Mockito.when(todoService.update(id, todoUpdateDto)).thenReturn(todoResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoUpdateDto)))
                .andDo(print()) // Print the response to the console
                .andExpect(status().isOk());
                //.andExpect(status().isOk())

                //.andExpect(jsonPath("$.id").value(todoResponseDto.getId()))
                //.andExpect(jsonPath("$.title").value(todoResponseDto.getTitle()));
                /*.andExpect(jsonPath("$.description").value(todoResponseDto.getDescription()))
                .andExpect(jsonPath("$.dueDate").value(todoResponseDto.getDueDate().toString()))
                .andExpect(jsonPath("$.priority").value(todoResponseDto.getPriority().toString()))
                .andExpect(jsonPath("$.status").value(todoResponseDto.getStatus().toString()))
                .andExpect(jsonPath("$.createdDate").value(todoResponseDto.getCreatedDate()))
                .andExpect(jsonPath("$.updatedDate").value(todoResponseDto.getUpdatedDate()))
                .andExpect(jsonPath("$.userId").value(todoResponseDto.getUserId()));*/
    }
}
