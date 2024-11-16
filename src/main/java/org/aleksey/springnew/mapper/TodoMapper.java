package org.aleksey.springnew.mapper;

import org.aleksey.springnew.dto.TodoCreateDto;
import org.aleksey.springnew.dto.TodoResponseDto;
import org.aleksey.springnew.dto.TodoUpdateDto;
import org.aleksey.springnew.model.Todo;
import org.mapstruct.Mapper;
import org.aleksey.springnew.config.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface TodoMapper {
    Todo toEntity(TodoCreateDto dto);

    Todo toEntity(TodoUpdateDto todoUpdateDto);

    TodoResponseDto toResponseDto(Todo todo);
}
