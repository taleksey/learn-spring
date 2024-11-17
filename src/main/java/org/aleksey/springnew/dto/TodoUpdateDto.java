package org.aleksey.springnew.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aleksey.springnew.types.PriorityType;
import org.aleksey.springnew.types.StatusType;
import org.aleksey.springnew.validation.ValidEnum;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @ValidEnum(enumClass = PriorityType.class, message = "Priority must be one of: HIGH, LOW, MEDIUM")
    private String priority;

    private StatusType status;

    public boolean equals(Object object) {
        if (this == object) return true;
        TodoUpdateDto that = (TodoUpdateDto) object;
        if (!Objects.equals(this.getId(), that.getId())) {
            return false;
        }

        if (!Objects.equals(this.getTitle(), that.getTitle())) {
            return false;
        }

        if (!Objects.equals(this.getDescription(), that.getDescription())) {
            return false;
        }

        if (!Objects.equals(this.getDueDate(), that.getDueDate())) {
            return false;
        }

        if (!Objects.equals(this.getPriority(), that.getPriority())) {
            return false;
        }

        return Objects.equals(this.getStatus(), that.getStatus());
    }
}
