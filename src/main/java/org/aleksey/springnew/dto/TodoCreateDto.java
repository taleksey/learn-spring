package org.aleksey.springnew.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoCreateDto {
    @Size(min = 1, max = 100)
    private String title;

    @Size(min = 1, max = 500)
    private String description;

    @NotBlank
    private LocalDateTime dueDate;

    private String priority;

}
