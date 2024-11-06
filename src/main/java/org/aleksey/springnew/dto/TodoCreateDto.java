package org.aleksey.springnew.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.aleksey.springnew.types.PriorityType;

import java.time.LocalDateTime;

@Data
public class TodoCreateDto {
    @Size(min = 1, max = 100)
    private String title;

    @Size(min = 1, max = 500)
    private String description;

    @NotNull
    private LocalDateTime dueDate;

    private PriorityType priority;

}
