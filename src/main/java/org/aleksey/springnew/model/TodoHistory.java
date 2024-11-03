package org.aleksey.springnew.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "todo_history")
public class TodoHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todoId;

    @Column(name = "old_state", columnDefinition = "BLOB")
    private String oldState;

    @Column(name = "new_state", columnDefinition = "BLOB")
    private String newState;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
