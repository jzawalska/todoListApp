package pl.todoList.repositories;

import pl.todoList.model.TodoItem;

import java.util.*;
import java.sql.*;

public class TodoItemJDBC extends JdbcRepository implements TodoItemRepository {
    private SQLiteConnectionManager SQLiteConnectionManager;
    public TodoItemJDBC(SQLiteConnectionManager SQLiteConnectionManager) {
        this.SQLiteConnectionManager = SQLiteConnectionManager;
    }

    @Override
    public void addTodoItem(TodoItem task) {
        PreparedStatement addTaskStmt = null;
        try {
            addTaskStmt = SQLiteConnectionManager.getConnection().prepareStatement("INSERT  INTO Tasks (TaskName, Status, Priority, Note, Urgent, Important) VALUES (?, ?, ?, ?, ?, ?)");

            addTaskStmt.setString(1, task.getTaskName());
            addTaskStmt.setString(2, task.getStatus());
            addTaskStmt.setInt(3, task.getPriority());
            addTaskStmt.setString(4, task.getNote());
            addTaskStmt.setBoolean(5, task.getUrgent());
            addTaskStmt.setBoolean(6, task.getImportant());

            addTaskStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            closeStatement(addTaskStmt);
        }
    }

    @Override
    public void deleteTodoItem(int taskId) {
        PreparedStatement deleteTaskStmt = null;
        try {
            deleteTaskStmt = SQLiteConnectionManager.getConnection().prepareStatement("DELETE FROM Tasks WHERE TaskId = ?");
            deleteTaskStmt.setInt(1, taskId);

            deleteTaskStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            closeStatement(deleteTaskStmt);
        }
    }

    @Override
    public void updateTodoItem(TodoItem task) {
        PreparedStatement updateTaskStmt = null;
        try {
            updateTaskStmt = SQLiteConnectionManager.getConnection().prepareStatement("UPDATE Tasks SET TaskName = ?, Status = ?, Priority = ?, Note = ?, Urgent = ?, Important = ? WHERE  TaskId = ?");
            updateTaskStmt.setString(1, task.getTaskName());
            updateTaskStmt.setString(2, task.getStatus());
            updateTaskStmt.setInt(3, task.getPriority());
            updateTaskStmt.setString(4, task.getNote());
            updateTaskStmt.setInt(5, task.getUrgent() ? 1 : 0);
            updateTaskStmt.setInt(6, task.getImportant() ? 1 : 0);
            updateTaskStmt.setInt(7, task.getTaskId());

            updateTaskStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            closeStatement(updateTaskStmt);
        }
    }

    @Override
    public List<TodoItem> getTodoItems() {
        List<TodoItem> tasks = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = SQLiteConnectionManager.getConnection().createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Tasks");

            while (resultSet.next()) {
                TodoItem task = convertRowToTodoItem(resultSet);
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            close(statement, resultSet);
        }
        return tasks;
    }

    public List<TodoItem> search(String sqlQuery) {
        List<TodoItem> tasks = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = SQLiteConnectionManager.getConnection().prepareStatement(sqlQuery);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                TodoItem task = convertRowToTodoItem(resultSet);
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            close(statement, resultSet);
        }
        return tasks;
    }

    @Override
    public List<TodoItem> searchTodoItems(String taskName) {
        String sqlStmt = new StringBuilder("SELECT * FROM Tasks WHERE TaskName LIKE '%").append(taskName).append("%'").toString();
        return search(sqlStmt);
    }

    @Override
    public List<TodoItem> selectUrgentTodoItems() {
        String sqlStmt = "SELECT * FROM Tasks WHERE Urgent = 1";
        return search(sqlStmt);
    }

    @Override
    public List<TodoItem> selectImportantTodoItems() {
        String sqlStmt = "SELECT * FROM Tasks WHERE Important = 1";
        return search(sqlStmt);
    }

    private TodoItem convertRowToTodoItem(ResultSet resultSet) throws SQLException {
        String taskName = resultSet.getString("TaskName");
        String status = resultSet.getString("Status");
        int priority = resultSet.getInt("Priority");
        String note = resultSet.getString("Note");
        int taskId = resultSet.getInt("TaskId");
        boolean isUrgent = resultSet.getBoolean("Urgent");
        boolean isImportant = resultSet.getBoolean("Important");
        TodoItem task = new TodoItem(taskId, taskName, status, priority, note, isUrgent, isImportant);
        return task;
    }
}

