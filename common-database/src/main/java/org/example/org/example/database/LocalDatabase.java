package org.example.org.example.database;

import org.apache.kafka.common.protocol.types.Field;

import java.sql.*;
import java.util.UUID;

public class LocalDatabase {

    private final Connection connection;

    public LocalDatabase(String name) throws SQLException {
        String url = "jdbc:sqlite:target/" + name + ".db";
        connection = DriverManager.getConnection(url);

    }

    public void createIfNosExists(String sql) {
        try {
            connection.createStatement().execute(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean update(String statement, String... params) throws SQLException {

        return preparedStatement(statement, params).execute();
    }

    public ResultSet query(String statement, String... params) throws SQLException {

        return preparedStatement(statement, params).executeQuery();
    }

    private PreparedStatement preparedStatement(String statement, String[] params) throws SQLException {
        var prepareStatement = connection.prepareStatement(statement);
        for (int i = 0; i < params.length; i++) {
            prepareStatement.setString(i + 1, params[i]);
        }
        return prepareStatement;
    }
}
