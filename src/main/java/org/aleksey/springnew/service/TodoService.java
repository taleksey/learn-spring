package org.aleksey.springnew.service;

import org.aleksey.springnew.dto.TodoCreateDto;
import org.aleksey.springnew.dto.TodoHistoryResponseDto;
import org.aleksey.springnew.dto.TodoResponseDto;
import org.aleksey.springnew.dto.TodoUpdateDto;

import java.util.List;

public interface TodoService {
    TodoResponseDto create(TodoCreateDto todoCreateDto);

    TodoResponseDto update(Long id, TodoUpdateDto todoUpdateDto);

    void delete(Long id);

    List<TodoHistoryResponseDto> getHistory(Long id);
}
