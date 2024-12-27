import com.example.todolist.MainApp;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MainAppTest extends BaseJavaFXTest {

    @Override
    public void start(Stage stage) throws Exception {
        new MainApp().start(stage);
    }

    @Test
    void testApplicationStarts() {
        Platform.runLater(() -> {
            Scene scene = Stage.getWindows().stream().findFirst().orElseThrow().getScene();
            assertNotNull(scene);
        });
    }
}