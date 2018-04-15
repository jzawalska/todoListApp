package pl.todoList.view;

import pl.todoList.model.TodoItem;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TodoListTableModel extends AbstractTableModel {
    private static final int TASK_NAME_COLUMN = 0;
    private static final int STATUS_COLUMN = 1;
    private static final int PRIORITY_COLUMN = 2;
    private static final int NOTES_COLUMN = 3;
    public static final int TASK_ID_COLUMN = -1;
    private static final int URGENT_COLUMN = -2;
    private static final int IMPORTANT_COLUMN = -3;

    private String[] columnNames = {"Task Name", "Status", "Priority", "Notes"};
    private List<TodoItem> tasks;

    public TodoListTableModel(List<TodoItem> tasks) {
        this.tasks = tasks;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        TodoItem tmpTask = tasks.get(row);

        switch (column) {
            case TASK_NAME_COLUMN:
                return tmpTask.getTaskName();
            case STATUS_COLUMN:
                return tmpTask.getStatus();
            case PRIORITY_COLUMN:
                return tmpTask.getPriority();
            case NOTES_COLUMN:
                return tmpTask.getNote();
            case URGENT_COLUMN:
                return tmpTask.getUrgent();
            case IMPORTANT_COLUMN:
                return tmpTask.getImportant();
            case TASK_ID_COLUMN:
                return tmpTask;
            default:
                return tmpTask.getTaskName();
        }
    }

    @Override
    public Class getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }
}
