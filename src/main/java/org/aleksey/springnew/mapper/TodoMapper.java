package org.aleksey.springnew.mapper;

import org.aleksey.springnew.dto.TodoCreateDto;
import org.aleksey.springnew.dto.TodoResponseDto;
import org.aleksey.springnew.dto.TodoUpdateDto;
import org.aleksey.springnew.model.Todo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoMapper {
   Todo toEntity(TodoCreateDto dto);

    Todo toEntity(TodoUpdateDto todoUpdateDto);

    TodoResponseDto toResponseDto(Todo todo);
}
