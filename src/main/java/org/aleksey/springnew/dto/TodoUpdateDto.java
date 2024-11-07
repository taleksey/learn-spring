package org.aleksey.springnew.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aleksey.springnew.types.PriorityType;
import org.aleksey.springnew.types.StatusType;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class TodoUpdateDto {
    private Long id;
    @Size(min = 1, max = 100)
    private String title;

    @Size(min = 1, max = 500)
    private String description;

    @NotNull
    private LocalDateTime dueDate;

    private PriorityType priority;

    private StatusType status;
}
