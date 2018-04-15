package pl.todoList.model;

public class TodoItem {
    private int taskId;
    private String taskName;
    private String status;
    private int priority;
    private String note;
    private boolean isUrgent;
    private boolean isImportant;

    public TodoItem() {}

    public TodoItem(int taskId, String taskName, String status, int priority, String note, boolean isUrgent, boolean isImportant) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.status = status;
        this.priority = priority;
        this.note = note;
        this.isUrgent = isUrgent;
        this.isImportant = isImportant;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        if (taskName.isEmpty())
            throw new IllegalArgumentException("Task Name field cannot be empty.");
        this.taskName = taskName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status.isEmpty())
            throw new IllegalArgumentException("Status field cannot be empty.");
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if (priority < 0)
            throw new IllegalArgumentException("Priority must be a natural number.");
        this.priority = priority;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean getUrgent() {
        return isUrgent;
    }

    public void setUrgent(boolean isUrgent) {
        this.isUrgent = isUrgent;
    }

    public boolean getImportant() {
        return isImportant;
    }

    public void setImportant(boolean isImportant) {
        this.isImportant = isImportant;
    }
}
