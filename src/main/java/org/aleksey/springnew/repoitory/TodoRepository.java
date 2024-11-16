package org.aleksey.springnew.repoitory;

import org.aleksey.springnew.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
