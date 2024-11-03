package org.aleksey.springnew.service;

import org.aleksey.springnew.dto.TodoCreateDto;
import org.aleksey.springnew.dto.TodoHistoryResponseDto;
import org.aleksey.springnew.dto.TodoResponseDto;
import org.aleksey.springnew.dto.TodoUpdateDto;
import org.aleksey.springnew.model.TodoHistory;

import java.util.List;

public interface TodoService {
    public TodoResponseDto createTodo(TodoCreateDto todoCreateDto);

    public TodoResponseDto updateTodo(Long id, TodoUpdateDto todoUpdateDto);

    public void deleteTodo(Long id);

    public List<TodoHistoryResponseDto> getTodoHistory(Long id);
}
