package com.example.todolist;

import com.example.todolist.dao.TaskDAO;
import com.example.todolist.model.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TodoController {
    @FXML
    public ListView<Task> taskListView;
    @FXML
    public TextField filterField;
    @FXML
    public VBox root;

    public TaskDAO taskDAO;
    private int currentPage = 0;
    private final int tasksPerPage = 3;
    private boolean isDarkTheme = true;

    public TodoController() {
        taskDAO = new TaskDAO();
    }
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTasksPerPage() {
        return tasksPerPage;
    }

    @FXML
    private void initialize() {
        filterField.textProperty().addListener((observable, oldValue, newValue) -> filterTasks(newValue));

        taskListView.setCellFactory(param -> new TaskCell(taskDAO,this));

        loadTasks();
    }
    public boolean isDarkTheme() {
        return isDarkTheme;
    }

    public void loadTasks() {
        taskListView.getItems().clear();
        List<Task> allTasks = taskDAO.getAllTasks();

        allTasks.sort(Comparator.comparingInt(Task::getId));

        int start = currentPage * tasksPerPage;
        int end = Math.min(start + tasksPerPage, allTasks.size());

        if (start < allTasks.size()) {
            taskListView.getItems().addAll(allTasks.subList(start, end));
        } else {
            currentPage--;
            loadTasks();
        }
    }

    public void filterTasks(String filter) {
        taskListView.getItems().clear();
        taskListView.getItems().addAll(taskDAO.getAllTasks().stream()
                .filter(task -> task.getTitle().contains(filter) || task.getDescription().contains(filter))
                .collect(Collectors.toList()));
    }

    @FXML
    private void addTask() {
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Add New Task");
        dialog.setHeaderText("Enter task details:");

        if (isDarkTheme) {
            dialog.getDialogPane().getStylesheets().add("/com/example/todolist/darkmode.css");
        } else {
            dialog.getDialogPane().getStylesheets().add("/com/example/todolist/lightmode.css");
        }

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(cancelButtonType);

        addButton.getStyleClass().add("save-button");
        cancelButton.getStyleClass().add("cancel-button");
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");
        CheckBox completedCheckBox = new CheckBox("Completed");
        completedCheckBox.setSelected(false);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Completed:"), 0, 2);
        grid.add(completedCheckBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Task(0, titleField.getText(), descriptionField.getText(), completedCheckBox.isSelected());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newTask -> {
            if (newTask != null) {
                taskDAO.createTask(newTask);
                loadTasks();
            }
        });
    }
    @FXML
    private void sortByTitle() {
        List<Task> allTasks = taskDAO.getAllTasks();
        allTasks.sort(Comparator.comparing(Task::getTitle));
        taskListView.getItems().setAll(allTasks);
    }

    @FXML
    private void sortByCompletion() {
        List<Task> allTasks = taskDAO.getAllTasks();
        allTasks.sort(Comparator.comparing(Task::isCompleted).reversed());
        taskListView.getItems().setAll(allTasks);
    }
    @FXML
    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            loadTasks();
        }
    }

    @FXML
    private void nextPage() {
        currentPage++;
        loadTasks();
    }

    @FXML
    public void toggleTheme() {
        // Переключаем тему
        if (isDarkTheme) {
            root.getStylesheets().remove("/com/example/todolist/darkmode.css");
            root.getStylesheets().add("/com/example/todolist/lightmode.css");
        } else {
            root.getStylesheets().remove("/com/example/todolist/lightmode.css");
            root.getStylesheets().add("/com/example/todolist/darkmode.css");
        }
        isDarkTheme = !isDarkTheme;
    }
}