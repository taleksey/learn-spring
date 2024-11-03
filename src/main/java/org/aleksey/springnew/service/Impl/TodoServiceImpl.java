package org.aleksey.springnew.service.Impl;

import lombok.RequiredArgsConstructor;
import org.aleksey.springnew.dto.TodoCreateDto;
import org.aleksey.springnew.dto.TodoHistoryResponseDto;
import org.aleksey.springnew.dto.TodoResponseDto;
import org.aleksey.springnew.dto.TodoUpdateDto;
import org.aleksey.springnew.exception.EntityNotFoundException;
import org.aleksey.springnew.mapper.TodoHistoryMapper;
import org.aleksey.springnew.mapper.TodoMapper;
import org.aleksey.springnew.model.Todo;
import org.aleksey.springnew.model.TodoHistory;
import org.aleksey.springnew.repoitory.TodoHistoryRepository;
import org.aleksey.springnew.repoitory.TodoRepository;
import org.aleksey.springnew.service.TodoService;
import org.aleksey.springnew.types.StatusType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;
    private final TodoHistoryRepository todoHistoryRepository;
    private final TodoHistoryMapper todoHistoryMapper;


    @Override
    public TodoResponseDto createTodo(TodoCreateDto todoCreateDto) {
        Todo todo = todoMapper.toEntity(todoCreateDto);
        todo.setStatus(StatusType.PENDING);
        todo.setUserId(1L);
        return todoMapper.toResponseDto(todoRepository.save(todo));
    }

    @Override
    public TodoResponseDto updateTodo(Long id, TodoUpdateDto todoUpdateDto) {
        Todo savedTodo = todoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find todo by id " + id)
        );

        Todo updatedTodo = todoMapper.toEntity(todoUpdateDto);
        updatedTodo.setId(id);
        TodoHistory todoHistory = new TodoHistory();
        todoHistory.setTodoId(savedTodo);
        todoHistory.setOldState(savedTodo.toString());

        Todo newCreatedTodo = todoRepository.save(updatedTodo);

        todoHistory.setNewState(newCreatedTodo.toString());
        todoHistoryRepository.save(todoHistory);

        return todoMapper.toResponseDto(savedTodo);
    }

    @Override
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }

    @Override
    public List<TodoHistoryResponseDto> getTodoHistory(Long id) {
        List<TodoHistory> historyList = todoHistoryRepository.findByTodoId(id);

        return historyList.stream()
                .map(todoHistoryMapper::toResponseDto)
                .collect(Collectors.toList());

    }
}
