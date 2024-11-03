package org.aleksey.springnew.dto;

import java.time.LocalDateTime;

public class TodoHistoryResponseDto {
    private Long id;
    private Long taskId;
    private String oldState;
    private String newState;
    private LocalDateTime changeDate;
    private Long changeBy;
}
