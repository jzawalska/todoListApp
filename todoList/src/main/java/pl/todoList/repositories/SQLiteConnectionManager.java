package pl.todoList.repositories;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SQLiteConnectionManager {

    private Connection connection;

    public SQLiteConnectionManager() {
        try {
            Properties properties = new Properties();
            FileInputStream in = new FileInputStream("todoList.properties");
            properties.load(in);
            in.close();

            String dburl = properties.getProperty("dburl");
            connection = DriverManager.getConnection(dburl);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
        } catch (SQLException | IOException e) {
            System.err.println(e.getMessage());
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}