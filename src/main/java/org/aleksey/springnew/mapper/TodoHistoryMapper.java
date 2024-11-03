package org.aleksey.springnew.mapper;

import org.aleksey.springnew.dto.TodoHistoryResponseDto;
import org.aleksey.springnew.model.TodoHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TodoHistoryMapper {
    @Mapping(target = "todoId", source = "todo.id")
    TodoHistoryResponseDto toResponseDto(TodoHistory taskHistory);
}