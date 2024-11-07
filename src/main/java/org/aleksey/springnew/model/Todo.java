package org.aleksey.springnew.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import lombok.Data;
import org.aleksey.springnew.types.PriorityType;
import org.aleksey.springnew.types.StatusType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "todo")
@SQLDelete(sql = "UPDATE todo SET is_deleted=true where id = ?")
@SQLRestriction("is_deleted=false")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", length = 100, columnDefinition = "VARBINARY")
    private String title;
    @Column(name = "description", length = 500, columnDefinition = "VARBINARY")
    private String description;
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private PriorityType priority;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Long userId;

    private boolean isDeleted = false;
}
