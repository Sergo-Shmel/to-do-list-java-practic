package com.example.todolist.dao;

import com.example.todolist.model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/todo_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Создать задачу
    public void createTask(Task task) {
        String sql = "INSERT INTO tasks (title, description, completed) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setBoolean(3, task.isCompleted());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Получить все задачи
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getBoolean("completed")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // Обновить задачу
    public void updateTask(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, completed = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setBoolean(3, task.isCompleted());
            stmt.setInt(4, task.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Удалить задачу
    public void deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}