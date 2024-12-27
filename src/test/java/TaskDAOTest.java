import com.example.todolist.dao.TaskDAO;

import com.example.todolist.model.Task;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class TaskDAOTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("todo_db")
            .withUsername("postgres")
            .withPassword("12345");

    private TaskDAO taskDAO;

    @BeforeEach
    void setUp() throws SQLException {
        taskDAO = new TaskDAO() {
            @Override
            public Connection getConnection() throws SQLException {
                return DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
            }
        };

        try (Connection conn = taskDAO.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE tasks (id SERIAL PRIMARY KEY, title VARCHAR(255), description TEXT, completed BOOLEAN)");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection conn = taskDAO.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE tasks");
        }
    }

    @Test
    void testCreateAndGetAllTasks() {
        Task task = new Task(0, "Test Task", "Test Description", false);
        taskDAO.createTask(task);

        List<Task> tasks = taskDAO.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
    }

    @Test
    void testUpdateTask() {
        Task task = new Task(0, "Test Task", "Test Description", false);
        taskDAO.createTask(task);

        List<Task> tasks = taskDAO.getAllTasks();
        Task taskToUpdate = tasks.get(0);
        taskToUpdate.setTitle("Updated Task");
        taskDAO.updateTask(taskToUpdate);

        List<Task> updatedTasks = taskDAO.getAllTasks();
        assertEquals("Updated Task", updatedTasks.get(0).getTitle());
    }

    @Test
    void testDeleteTask() {
        Task task = new Task(0, "Test Task", "Test Description", false);
        taskDAO.createTask(task);

        List<Task> tasks = taskDAO.getAllTasks();
        assertEquals(1, tasks.size());

        taskDAO.deleteTask(tasks.get(0).getId());
        List<Task> updatedTasks = taskDAO.getAllTasks();
        assertEquals(0, updatedTasks.size());
    }
}