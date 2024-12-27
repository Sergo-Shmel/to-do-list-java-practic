import com.example.todolist.model.Task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    void testTaskCreation() {
        Task task = new Task(1, "Test Task", "Test Description", false);
        assertEquals(1, task.getId());
        assertEquals("Test Task", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertFalse(task.isCompleted());
    }

    @Test
    void testTaskSetters() {
        Task task = new Task(1, "Test Task", "Test Description", false);
        task.setId(2);
        task.setTitle("Updated Task");
        task.setDescription("Updated Description");
        task.setCompleted(true);

        assertEquals(2, task.getId());
        assertEquals("Updated Task", task.getTitle());
        assertEquals("Updated Description", task.getDescription());
        assertTrue(task.isCompleted());
    }
}