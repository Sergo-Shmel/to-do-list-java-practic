import com.example.todolist.TaskCell;
import com.example.todolist.TodoController;
import com.example.todolist.dao.TaskDAO;
import com.example.todolist.model.Task;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskCellTest extends BaseJavaFXTest {

    private TaskDAO taskDAO;
    private TaskCell taskCell;
    private ListView<Task> listView;

    @BeforeEach
    void setUp() {
        taskDAO = mock(TaskDAO.class);
        TodoController controller = new TodoController();
        controller.taskDAO = taskDAO;
        taskCell = new TaskCell(taskDAO, controller);

        // Создаем реальный ListView
        listView = new ListView<>();
        ObservableList<Task> observableList = FXCollections.observableArrayList();
        listView.setItems(observableList);
        taskCell.updateListView(listView);
    }

    @Test
    void testUpdateItem() {
        Platform.runLater(() -> {
            Task task = new Task(1, "Test Task", "Test Description", false);
            taskCell.updateItem(task, false);

            assertNotNull(taskCell.getGraphic());
            assertEquals("Test Task", taskCell.titleLabel.getText());
        });
    }

    @Test
    void testEditButton() {
        Platform.runLater(() -> {
            Task task = new Task(1, "Test Task", "Test Description", false);
            taskCell.updateItem(task, false);

            Button editButton = taskCell.editButton;
            editButton.fire();

            verify(taskDAO, times(1)).updateTask(any(Task.class));
        });
    }

    @Test
    void testDeleteButton() {
        Platform.runLater(() -> {
            Task task = new Task(1, "Test Task", "Test Description", false);
            taskCell.updateItem(task, false);

            Button deleteButton = taskCell.deleteButton;
            deleteButton.fire();

            verify(taskDAO, times(1)).deleteTask(task.getId());
        });
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}