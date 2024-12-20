package org.aleksey.springnew.service.Impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aleksey.springnew.dto.TodoCreateDto;
import org.aleksey.springnew.dto.TodoHistoryResponseDto;
import org.aleksey.springnew.dto.TodoResponseDto;
import org.aleksey.springnew.dto.TodoUpdateDto;
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

@RequiredArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final TodoHistoryRepository todoHistoryRepository;
    private final TodoMapper todoMapper;
    private final TodoHistoryMapper todoHistoryMapper;

    @Override
    public TodoResponseDto create(TodoCreateDto todoCreateDto) {
        Todo todo = todoMapper.toEntity(todoCreateDto);
        todo.setStatus(StatusType.PENDING);
        todo.setUserId(1L);
        return todoMapper.toResponseDto(todoRepository.save(todo));
    }

    @Override
    @Transactional
    public TodoResponseDto update(Long id, TodoUpdateDto todoUpdateDto) {
        Todo savedTodo = todoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find todo by id " + id)
        );

        Todo updatedTodo = todoMapper.toEntity(todoUpdateDto);
        updatedTodo.setUserId(savedTodo.getUserId());
        updatedTodo.setStatus(StatusType.IN_PROGRESS);

        TodoHistory todoHistory = new TodoHistory();
        todoHistory.setTodo(savedTodo);
        todoHistory.setOldState(savedTodo.toString());
        todoHistory.setChangedBy(1L);

        Todo newCreatedTodo = todoRepository.save(updatedTodo);
        todoHistory.setNewState(newCreatedTodo.toString());

        todoHistoryRepository.save(todoHistory);

        return todoMapper.toResponseDto(newCreatedTodo);
    }

    @Override
    public void delete(Long id) {
        todoRepository.deleteById(id);
    }

    @Override
    public List<TodoHistoryResponseDto> getHistory(Long id) {
        Todo savedTodo = todoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find todo by id " + id)
        );
        List<TodoHistory> historyList = todoHistoryRepository.findByTodoId(savedTodo.getId());

        return historyList.stream()
                .map(todoHistoryMapper::toResponseDto)
                .toList();

    }
}
