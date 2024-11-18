insert into todo (id, title, description, due_date, priority, status, created_date, updated_date, user_id, is_deleted)
values (1, "Todo first title", "Todo first descriptiom", "2024-11-17 22:33:16", "LOW", "PENDING", "2024-11-17 22:33:50", "2024-11-17 22:34:10", 1, 0);

insert into todo_history (id, todo_id, old_state, new_state, updated_at, changed_by, is_deleted)
values
    (1, 1, "old state first", "new state first", "2024-11-17 22:35:26", 1, 0),
    (2, 1, "old state second", "new state second", "2024-11-17 22:37:36", 1, 0);