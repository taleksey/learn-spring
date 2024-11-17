package org.aleksey.springnew.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.aleksey.springnew.dto.TodoCreateDto;
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

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

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
    void createTodo_ValidRequestDto_Success() throws Exception {
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

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/todos")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        TodoResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), TodoResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(todoResponseDto.getTitle(), actual.getTitle());
        Assertions.assertEquals(todoResponseDto.getDescription(), actual.getDescription());
        // or
        EqualsBuilder.reflectionEquals(todoResponseDto, actual, "id");
    }
}
