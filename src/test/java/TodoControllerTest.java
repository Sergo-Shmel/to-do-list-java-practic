import com.example.todolist.TodoController;
import com.example.todolist.dao.TaskDAO;
import com.example.todolist.model.Task;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TodoControllerTest extends BaseJavaFXTest {

    private TodoController controller;
    private TaskDAO taskDAO;

    @BeforeEach
    void setUp() {
        taskDAO = mock(TaskDAO.class);
        controller = new TodoController();
        controller.taskDAO = taskDAO;

        // Initialize the taskListView and root
        controller.taskListView = new ListView<>();
        controller.root = new VBox();
        controller.filterField = new TextField();
        controller.root.getStylesheets().add("/com/example/todolist/darkmode.css");
    }

    @Test
    void testLoadTasks() {
        List<Task> tasks = Arrays.asList(
                new Task(1, "Task 1", "Description 1", false),
                new Task(2, "Task 2", "Description 2", true)
        );
        when(taskDAO.getAllTasks()).thenReturn(tasks);

        controller.loadTasks();

        ListView<Task> taskListView = controller.taskListView;
        assertEquals(2, taskListView.getItems().size());
        assertEquals("Task 1", taskListView.getItems().get(0).getTitle());
        assertEquals("Task 2", taskListView.getItems().get(1).getTitle());
    }

    @Test
    void testFilterTasks() {
        List<Task> tasks = Arrays.asList(
                new Task(1, "Task 1", "Description 1", false),
                new Task(2, "Task 2", "Description 2", true)
        );
        when(taskDAO.getAllTasks()).thenReturn(tasks);

        controller.loadTasks();

        TextField filterField = controller.filterField;
        filterField.setText("Task 1");

        controller.filterTasks(filterField.getText());

        ListView<Task> taskListView = controller.taskListView;
        assertEquals(1, taskListView.getItems().size());
        assertEquals("Task 1", taskListView.getItems().get(0).getTitle());
    }

    @Test
    void testToggleTheme() {
        Platform.runLater(() -> {
            assertTrue(controller.root.getStylesheets().contains("/com/example/todolist/darkmode.css"));

            controller.toggleTheme();

            assertTrue(controller.root.getStylesheets().contains("/com/example/todolist/lightmode.css"));

            controller.toggleTheme();

            assertTrue(controller.root.getStylesheets().contains("/com/example/todolist/darkmode.css"));
        });
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}