package org.aleksey.springnew.repoitory;

import jakarta.transaction.Transactional;
import org.aleksey.springnew.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    /*@Transactional
    @Modifying
    void deleteByNameContaining(String name);*/

}
