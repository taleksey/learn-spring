package org.aleksey.springnew.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoHistoryResponseDto {
    private Long id;
    private Long todoId;
    private String oldState;
    private String newState;
    private LocalDateTime changeDate;
    private Long changeBy;
}
