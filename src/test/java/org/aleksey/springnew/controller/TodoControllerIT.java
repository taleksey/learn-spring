package org.aleksey.springnew.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.aleksey.springnew.dto.TodoCreateDto;
import org.aleksey.springnew.dto.TodoHistoryResponseDto;
import org.aleksey.springnew.dto.TodoResponseDto;
import org.aleksey.springnew.types.PriorityType;
import org.aleksey.springnew.types.StatusType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.apache.commons.lang3.builder.EqualsBuilder;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoControllerIT {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/todo/add-todos-and-histories.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/todo/remove-all-todos-and-histories.sql")
            );
        }
    }

    @Test
    @DisplayName("Create a new todo")
    @Sql(
        scripts = "classpath:database/todo/delete-insert-new-todo.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void createTodo_ValidRequestDto_Success() throws Exception {
        Long id = 2L;
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        TodoCreateDto todoCreateDto = new TodoCreateDto();
        todoCreateDto.setTitle("Test Todo");
        todoCreateDto.setDescription("Description Todo");
        todoCreateDto.setDueDate(localDateTime);
        todoCreateDto.setPriority(PriorityType.LOW.toString());

        TodoResponseDto todoResponseDto = new TodoResponseDto();
        todoResponseDto.setId(id);
        todoResponseDto.setTitle(todoCreateDto.getTitle());
        todoResponseDto.setDescription(todoCreateDto.getDescription());
        todoResponseDto.setPriority(todoCreateDto.getPriority());
        todoResponseDto.setStatus(StatusType.IN_PROGRESS.toString());
        todoResponseDto.setDueDate(localDateTime);
        todoResponseDto.setUpdatedAt(null);
        todoResponseDto.setCreatedAt(null);

        String jsonRequest = objectMapper.writeValueAsString(todoCreateDto);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/todos")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        TodoResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), TodoResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(todoResponseDto.getTitle(), actual.getTitle());
        Assertions.assertEquals(todoResponseDto.getDescription(), actual.getDescription());
        //Or
        EqualsBuilder.reflectionEquals(todoResponseDto, actual, "id");
    }

    @Test
    @DisplayName("Update created task")
    @Sql(
            scripts = "classpath:database/todo/return-original-todo.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void updateTodo_ValidRequestDto_Success() throws Exception {
        Long id = 1L;
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        TodoCreateDto todoCreateDto = new TodoCreateDto();
        todoCreateDto.setTitle("Test Todo");
        todoCreateDto.setDescription("Description Todo");
        todoCreateDto.setDueDate(localDateTime);
        todoCreateDto.setPriority(PriorityType.LOW.toString());

        TodoResponseDto todoResponseDto = new TodoResponseDto();
        todoResponseDto.setId(id);
        todoResponseDto.setTitle(todoCreateDto.getTitle());
        todoResponseDto.setDescription(todoCreateDto.getDescription());
        todoResponseDto.setPriority(todoCreateDto.getPriority());
        todoResponseDto.setStatus(StatusType.IN_PROGRESS.toString());
        todoResponseDto.setDueDate(localDateTime);
        todoResponseDto.setUpdatedAt(null);
        todoResponseDto.setCreatedAt(null);

        String jsonRequest = objectMapper.writeValueAsString(todoCreateDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/todos/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        TodoResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), TodoResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(todoResponseDto.getId(), actual.getId());
        Assertions.assertEquals(todoResponseDto.getTitle(), actual.getTitle());
        Assertions.assertEquals(todoResponseDto.getDescription(), actual.getDescription());
        Assertions.assertEquals(todoResponseDto.getDueDate(), actual.getDueDate());
        Assertions.assertEquals(todoResponseDto.getPriority(), actual.getPriority());
        Assertions.assertEquals(todoResponseDto.getStatus(), actual.getStatus());
    }

    @Test
    @DisplayName("Add update task")
    void getHistoryTodo_ValidRequest_Success() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/todos/1/history")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        // Given
        List<TodoHistoryResponseDto> expected = new ArrayList<>();
        TodoHistoryResponseDto todoHistoryResponseFirstDto = new TodoHistoryResponseDto();
        todoHistoryResponseFirstDto.setId(1L);
        todoHistoryResponseFirstDto.setTodoId(1L);
        todoHistoryResponseFirstDto.setOldState("old state first");
        todoHistoryResponseFirstDto.setNewState("new state first");
        expected.add(todoHistoryResponseFirstDto);

        TodoHistoryResponseDto todoHistoryResponseSecondDto = new TodoHistoryResponseDto();
        todoHistoryResponseSecondDto.setId(2L);
        todoHistoryResponseSecondDto.setTodoId(1L);
        todoHistoryResponseSecondDto.setOldState("old state second");
        todoHistoryResponseSecondDto.setNewState("new state second");
        expected.add(todoHistoryResponseSecondDto);

        // Then
        //TodoHistoryResponseDto[] actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(), TodoHistoryResponseDto[].class);
        List<TodoHistoryResponseDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                new TypeReference<>() {
                }
        );

        Assertions.assertEquals(2, (long) actual.size());
        Assertions.assertEquals(expected, actual);
    }
}
