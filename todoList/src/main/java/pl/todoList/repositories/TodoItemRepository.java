package pl.todoList.repositories;

import pl.todoList.model.TodoItem;

import java.util.List;

public interface TodoItemRepository {
    void addTodoItem(TodoItem task);

    void deleteTodoItem(int taskId);

    void updateTodoItem(TodoItem task);

    List<TodoItem> getTodoItems();

    List<TodoItem> searchTodoItems(String taskName);

    List<TodoItem> selectUrgentTodoItems();

    List<TodoItem> selectImportantTodoItems();
}
