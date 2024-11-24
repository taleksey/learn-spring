UPDATE todo SET
                title= "Todo first title",
                description = "Todo first descriptiom",
                due_date = "2024-11-17 22:33:16",
                priority = "LOW",
                status = "PENDING",
                created_at = "2024-11-17 22:33:50",
                updated_at = "2024-11-17 22:34:10",
                user_id = 1,
                is_deleted = 0
where id = 1;

DELETE FROM todo_history WHERE id > 2;