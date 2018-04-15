package pl.todoList.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class JdbcRepository {
    static void close(Statement statement, ResultSet resultSet) {
        closeStatement(statement);
        closeResultSet(resultSet);
    }

    static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

}
