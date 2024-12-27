import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseJavaFXTest {
    private static boolean jfxInitialized = false;

    @BeforeAll
    public static void initJavaFX() {
        if (!jfxInitialized) {
            Platform.startup(() -> {});
            jfxInitialized = true;
        }
    }

    public abstract void start(Stage stage) throws Exception;
}