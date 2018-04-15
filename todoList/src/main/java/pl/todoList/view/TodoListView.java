package pl.todoList.view;
import pl.todoList.model.TodoItem;
import pl.todoList.repositories.SQLiteConnectionManager;
import pl.todoList.repositories.TodoItemJDBC;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class TodoListView extends JFrame {
    private JPanel contentPane;
    private JTextField searchField;
    private JTable table;

    public TodoListView() {
        SQLiteConnectionManager connectionManager = new SQLiteConnectionManager();
        TodoItemJDBC todoItemDao = new TodoItemJDBC(connectionManager);
        addMainPanel();
        //add buttons
        searchButton(todoItemDao);
        showTasksButton(todoItemDao);
        addTaskButton(todoItemDao);
        deleteTaskButton(todoItemDao);
        editTaskButton(todoItemDao);
        selectUrgentTasksButton(todoItemDao);
        selectImportantTasksButton(todoItemDao);
    }

    void addMainPanel(){
        setTitle("TODOList App");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 484, 308);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel todoListLabel = new JLabel("TODO List:");
        todoListLabel.setFont(new Font("Sylfaen", Font.PLAIN, 14));
        todoListLabel.setBounds(122, 11, 73, 14);
        contentPane.add(todoListLabel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 31, 319, 191);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        JLabel enterTaskNameLabel = new JLabel("Enter Task Name:");
        enterTaskNameLabel.setBounds(10, 233, 116, 17);
        contentPane.add(enterTaskNameLabel);

        searchField = new JTextField();
        searchField.setBounds(135, 231, 109, 20);
        contentPane.add(searchField);
        searchField.setColumns(10);
    }

    void searchButton(TodoItemJDBC todoItemDao){
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            try {
                String taskName = searchField.getText();

                List<TodoItem> tasks = null;

                if (taskName != null && taskName.trim().length() > 0) {
                    tasks = todoItemDao.searchTodoItems(taskName);
                } else {
                    tasks = todoItemDao.getTodoItems();
                }

                TodoListTableModel model = new TodoListTableModel(tasks);
                table.setModel(model);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(TodoListView.this, "Error: " + exc, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        searchButton.setBounds(244, 230, 85, 23);
        contentPane.add(searchButton);
    }

    void showTasksButton(TodoItemJDBC todoItemDao){
        JButton displayTasksButton = new JButton("Display Tasks");
        displayTasksButton.addActionListener(e -> {
            try {
                List<TodoItem> tasks = null;
                tasks = todoItemDao.getTodoItems();

                TodoListTableModel model = new TodoListTableModel(tasks);
                table.setModel(model);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(TodoListView.this, "Error: " + exc, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        displayTasksButton.setBounds(339, 49, 135, 23);
        contentPane.add(displayTasksButton);
    }

    void addTaskButton(TodoItemJDBC todoItemDao){
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(e -> {
            try {
                TaskDialog dialog = new TaskDialog(TodoListView.this, todoItemDao);
                dialog.setVisible(true);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(TodoListView.this, "Error: " + exc, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        addTaskButton.setBounds(339, 93, 135, 23);
        contentPane.add(addTaskButton);
    }

    void deleteTaskButton(TodoItemJDBC todoItemDao){
        JButton deleteTaskButton = new JButton("Delete Task");
        deleteTaskButton.addActionListener(e -> {
            try {
                int row = table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(TodoListView.this,
                            "You must select a task", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirmation = JOptionPane.showConfirmDialog(TodoListView.this, "Delete this task?", "Confirm",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (confirmation != JOptionPane.YES_OPTION) {
                    return;
                }

                TodoItem task = (TodoItem) table.getValueAt(row, TodoListTableModel.TASK_ID_COLUMN);
                todoItemDao.deleteTodoItem(task.getTaskId());

                refreshTodoListView(todoItemDao);

                JOptionPane.showMessageDialog(TodoListView.this, "Task deleted successfully.", "Task Deleted", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception exc) {
                JOptionPane.showMessageDialog(TodoListView.this, "Error while deleting task: " + exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteTaskButton.setBounds(339, 137, 135, 23);
        contentPane.add(deleteTaskButton);
    }

    void editTaskButton(TodoItemJDBC todoItemDao){
        JButton editTaskButton = new JButton("Edit Task");
        editTaskButton.addActionListener(e -> {
            try {
                int row = table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(TodoListView.this,
                            "You must select a task", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                TodoItem task = (TodoItem) table.getValueAt(row, TodoListTableModel.TASK_ID_COLUMN);
                TaskDialog dialog = new TaskDialog(TodoListView.this, todoItemDao, task, true);
                dialog.setVisible(true);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(TodoListView.this, "Error while editing task: " + exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        editTaskButton.setBounds(339, 182, 135, 23);
        contentPane.add(editTaskButton);
    }

    void selectUrgentTasksButton(TodoItemJDBC todoItemDao){
        JButton urgentTasksButton = new JButton("Urgent Tasks");
        urgentTasksButton.addActionListener(e -> {
            try {
                List<TodoItem> tasks = null;
                tasks = todoItemDao.selectUrgentTodoItems();

                TodoListTableModel model = new TodoListTableModel(tasks);
                table.setModel(model);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(TodoListView.this, "Error: " + exc, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        urgentTasksButton.setBounds(339, 230, 135, 23);
        contentPane.add(urgentTasksButton);
    }

    void selectImportantTasksButton(TodoItemJDBC todoItemDao){
        JButton importantTasksButton = new JButton("Important Tasks");
        importantTasksButton.addActionListener(e -> {
            try {
                List<TodoItem> tasks = null;
                tasks = todoItemDao.selectImportantTodoItems();

                TodoListTableModel model = new TodoListTableModel(tasks);
                table.setModel(model);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(TodoListView.this, "Error: " + exc, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        importantTasksButton.setBounds(339, 260, 135, 23);
        contentPane.add(importantTasksButton);
    }

    public void refreshTodoListView(TodoItemJDBC todoItemDao) {
        try {
            List<TodoItem> tasks = todoItemDao.getTodoItems();

            TodoListTableModel model = new TodoListTableModel(tasks);
            table.setModel(model);
        } catch (Exception exc) {
            JOptionPane.showMessageDialog(this, "Error: " + exc, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
