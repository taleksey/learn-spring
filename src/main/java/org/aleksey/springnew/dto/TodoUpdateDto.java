package org.aleksey.springnew.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.aleksey.springnew.types.PriorityType;
import org.aleksey.springnew.types.StatusType;

import java.time.LocalDateTime;

public class TodoUpdateDto {
    @Size(min = 1, max = 100)
    private String title;

    @Size(min = 1, max = 500)
    private String description;

    @NotBlank
    private LocalDateTime dueDate;

    private PriorityType priority;

    private StatusType status;

}
