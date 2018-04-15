package pl.todoList.main;
import pl.todoList.view.TodoListView;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                TodoListView app = new TodoListView();
                app.setVisible(true);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        });
    }
}

