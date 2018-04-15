package pl.todoList.view;

import pl.todoList.model.TodoItem;
import pl.todoList.repositories.TodoItemJDBC;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TaskDialog extends JDialog {

    private TodoItemJDBC todoItemDao;
    private TodoListView todoListApp;
    private TodoItem previousTask = null;
    private boolean updateMode = false;

    private JTextField taskNameField;
    private JTextField statusField;
    private JTextField priorityField;
    private JCheckBox importantCheckbox;
    private JCheckBox urgentCheckbox;
    private JTextField noteField;

    public TaskDialog(TodoListView todoListApp,
                      TodoItemJDBC todoItemDao, TodoItem previousTask, boolean updateMode) {
        this();
        this.todoItemDao = todoItemDao;
        this.todoListApp = todoListApp;
        this.previousTask = previousTask;
        this.updateMode = updateMode;

        if (updateMode) {
            setTitle("Update TodoList");
            fillGui(previousTask);
        }
    }


    public TaskDialog(TodoListView todoListApp,
                      TodoItemJDBC todoItemDao) {
        this(todoListApp, todoItemDao, null, false);
    }

    public TaskDialog() {
        setResizable(false);
        setBounds(100, 100, 322, 240);
        getContentPane().setLayout(new BorderLayout());
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel taskNameLabel = new JLabel("Task Name");
        taskNameLabel.setBounds(10, 11, 75, 14);
        contentPanel.add(taskNameLabel);

        JLabel statusLabel = new JLabel("Status");
        statusLabel.setBounds(10, 36, 46, 14);
        contentPanel.add(statusLabel);

        JLabel priorityLabel = new JLabel("Priority");
        priorityLabel.setBounds(172, 36, 46, 14);
        contentPanel.add(priorityLabel);

        JLabel notesLabel = new JLabel("Notes");
        notesLabel.setBounds(10, 61, 46, 14);
        contentPanel.add(notesLabel);

        taskNameField = new JTextField();
        taskNameField.setBounds(84, 8, 220, 20);
        contentPanel.add(taskNameField);
        taskNameField.setColumns(10);

        statusField = new JTextField();
        statusField.setBounds(52, 36, 86, 20);
        contentPanel.add(statusField);
        statusField.setColumns(10);

        priorityField = new JTextField();
        priorityField.setBounds(218, 36, 86, 20);
        contentPanel.add(priorityField);
        priorityField.setColumns(10);

        noteField = new JTextField();
        noteField.setBounds(10, 77, 296, 29);
        contentPanel.add(noteField);
        noteField.setColumns(10);

        importantCheckbox = new JCheckBox("Important");
        importantCheckbox.setBounds(120, 113, 120, 23);
        contentPanel.add(importantCheckbox);

        urgentCheckbox = new JCheckBox("Urgent");
        urgentCheckbox.setBounds(0, 113, 120, 23);
        contentPanel.add(urgentCheckbox);

        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton saveButton = new JButton("Save");
                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            saveTask();
                        } catch (Exception exc) {
                            JOptionPane.showMessageDialog(TaskDialog.this,
                                    "Error while saving task: " + exc.getMessage(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                buttonPane.add(saveButton);
                getRootPane().setDefaultButton(saveButton);
            }
            {
                JButton exitButton = new JButton("Exit");
                buttonPane.add(exitButton);
                exitButton.addActionListener(e -> {
                    setVisible(false);
                    dispose();
                });
            }
        }
    }

    private void fillGui(TodoItem task) {
        taskNameField.setText(task.getTaskName());
        statusField.setText(task.getStatus());
        priorityField.setText(Integer.toString(task.getPriority()));
        noteField.setText(task.getNote());

        if ((task.getUrgent())) {
            urgentCheckbox.setSelected(true);
        } else {
            urgentCheckbox.setSelected(false);
        }
        if ((task.getImportant())) {
            importantCheckbox.setSelected(true);
        } else {
            importantCheckbox.setSelected(false);
        }
    }

    private void saveTask() {
        String taskName = taskNameField.getText();
        String status = statusField.getText();
        int priority = Integer.parseInt(priorityField.getText());
        String note = noteField.getText();
        boolean isUrgent = urgentCheckbox.isSelected();
        boolean isImportant = importantCheckbox.isSelected();
        TodoItem todoItem;

        if (updateMode) {
            todoItem = previousTask;
        } else {
            todoItem = new TodoItem();
        }
        todoItem.setTaskName(taskName);
        todoItem.setStatus(status);
        todoItem.setPriority(priority);
        todoItem.setNote(note);
        todoItem.setUrgent(isUrgent);
        todoItem.setImportant(isImportant);
        try {
            if (updateMode) {
                todoItemDao.updateTodoItem(todoItem);
            } else {
                todoItemDao.addTodoItem(todoItem);
            }
            setVisible(false);
            dispose();

            todoListApp.refreshTodoListView(todoItemDao);

            JOptionPane.showMessageDialog(todoListApp,
                    "Task saved successfully.", "Task Saved",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception exc) {
            JOptionPane.showMessageDialog(todoListApp,
                    "Error while saving task: " + exc.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

