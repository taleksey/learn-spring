package org.aleksey.springnew.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aleksey.springnew.types.PriorityType;
import org.aleksey.springnew.types.StatusType;
import org.aleksey.springnew.validation.ValidEnum;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class TodoUpdateDto {
    private Long id;
    @Size(min = 1, max = 100)
    private String title;

    @Size(min = 1, max = 500)
    private String description;

    @NotNull
    private LocalDateTime dueDate;

    @ValidEnum(enumClass = PriorityType.class, message = "Priority must be one of: HIGH, LOW, MEDIUM")
    private String priority;

    private StatusType status;
}
