package org.aleksey.springnew.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aleksey.springnew.dto.TodoCreateDto;
import org.aleksey.springnew.dto.TodoHistoryResponseDto;
import org.aleksey.springnew.dto.TodoResponseDto;
import org.aleksey.springnew.dto.TodoUpdateDto;
import org.aleksey.springnew.service.TodoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @PostMapping
    public TodoResponseDto create(@RequestBody @Valid TodoCreateDto todo) {
        return todoService.createTodo(todo);
    }

    @PutMapping("/{id}")
    public TodoResponseDto update(@PathVariable Long id, @Valid @RequestBody TodoUpdateDto todo) {
        todo.setId(id);
        return todoService.updateTodo(id, todo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        todoService.deleteTodo(id);
    }

    @GetMapping("/{id}/history")
    public List<TodoHistoryResponseDto> getHistory(@PathVariable Long id) {
        return todoService.getTodoHistory(id);
    }
}
