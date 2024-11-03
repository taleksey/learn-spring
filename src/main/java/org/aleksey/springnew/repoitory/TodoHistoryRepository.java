package org.aleksey.springnew.repoitory;

import org.aleksey.springnew.model.TodoHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TodoHistoryRepository extends JpaRepository<TodoHistory, Long> {
    @Query("select td from TodoHistory td where td.todoId = ?1")
    List<TodoHistory> findByTodoId(Long id);
}
