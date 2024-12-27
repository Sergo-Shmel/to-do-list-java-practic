package com.example.todolist;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        URL fxmlUrl = MainApp.class.getResource("/com/example/todolist/todo-view.fxml");
        if (fxmlUrl == null) {
            System.err.println("FXML файл не найден!");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("To-Do List");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}