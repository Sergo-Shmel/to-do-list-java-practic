package com.example.todolist;

import com.example.todolist.dao.TaskDAO;
import com.example.todolist.model.Task;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

public class TaskCell extends ListCell<Task> {
    public final Button editButton = new Button();
    public final Button deleteButton = new Button();
    private final Button expandButton = new Button();
    public final Label titleLabel = new Label();
    private final HBox contentBox = new HBox(10);
    private final HBox buttonBox = new HBox(10);
    private final TaskDAO taskDAO;
    private final VBox vbox = new VBox(5);
    private final TodoController todoController;

    public TaskCell(TaskDAO taskDAO, TodoController todoController1) {
        this.taskDAO = taskDAO;
        this.todoController = todoController1;

        editButton.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.PENCIL));
        deleteButton.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TRASH));

        expandButton.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.EYE));
        deleteButton.getStyleClass().add("delete-button");
        expandButton.getStyleClass().add("expand-button");
        editButton.getStyleClass().add("edit-button");

        editButton.setPrefSize(33, 33);
        expandButton.setPrefSize(33, 33);
        deleteButton.setPrefSize(33, 33);

        buttonBox.getChildren().addAll(expandButton, editButton, deleteButton);

        editButton.setOnAction(event -> {
            Task task = getItem();
            if (task != null) {
                showEditDialog(task, todoController.isDarkTheme());
            }
        });

        expandButton.setOnAction(event -> {
            Task task = getItem();
            if (task != null) {
                showDetailsDialog(task, todoController.isDarkTheme());
            }
        });

        deleteButton.setOnAction(event -> {
            Task task = getItem();
            if (task != null) {
                if (confirmDeletion()) {
                    taskDAO.deleteTask(task.getId());
                    getListView().getItems().remove(task);

                    int totalTasks = taskDAO.getAllTasks().size();
                    int tasksPerPage = todoController.getTasksPerPage();
                    int currentPage = todoController.getCurrentPage();

                    if ((currentPage * tasksPerPage) >= totalTasks) {
                        todoController.setCurrentPage(currentPage - 1);
                    }

                    todoController.loadTasks();
                }
            }
        });

        buttonBox.setAlignment(Pos.CENTER_LEFT);
        titleLabel.setAlignment(Pos.CENTER_LEFT);
        titleLabel.prefWidthProperty().bind(contentBox.widthProperty().multiply(0.7));
        titleLabel.setPadding(new Insets(5));
        contentBox.setPadding(new Insets(5, 10, 5, 10));
        contentBox.getChildren().addAll(titleLabel, buttonBox);

        VBox.setVgrow(titleLabel, Priority.ALWAYS);

        vbox.getChildren().addAll(contentBox);
    }

    @Override
    public void updateItem(Task task, boolean empty) {
        super.updateItem(task, empty);
        if (empty || task == null) {
            setGraphic(null);
            setText(null);
        } else {
            titleLabel.setText(task.getTitle());

            if (task.isCompleted()) {
                contentBox.getStyleClass().setAll("completed-task");
            } else {
                contentBox.getStyleClass().setAll("incomplete-task");
            }
            contentBox.getStyleClass().add("content-box");

            setGraphic(vbox);
        }
    }

    private void showEditDialog(Task task, boolean isDarkTheme) {
        Platform.runLater(() -> {
            Dialog<Task> dialog = new Dialog<>();
            dialog.setTitle("Edit Task");
            dialog.setHeaderText("Edit the task details:");

            if (isDarkTheme) {
                dialog.getDialogPane().getStylesheets().add("/com/example/todolist/darkmode.css");
            } else {
                dialog.getDialogPane().getStylesheets().add("/com/example/todolist/lightmode.css");
            }

            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

            TextField titleField = new TextField(task.getTitle());
            TextField descriptionField = new TextField(task.getDescription());
            CheckBox completedCheckBox = new CheckBox("Completed");
            completedCheckBox.setSelected(task.isCompleted());

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
                if (dialogButton == saveButtonType) {
                    task.setTitle(titleField.getText());
                    task.setDescription(descriptionField.getText());
                    task.setCompleted(completedCheckBox.isSelected());
                    return task;
                }
                return null;
            });

            dialog.showAndWait().ifPresent(updatedTask -> {
                if (updatedTask != null) {
                    taskDAO.updateTask(updatedTask);
                    getListView().refresh();
                }
            });
        });
    }

    private void showDetailsDialog(Task task, boolean darkTheme) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Task Details");
        dialog.setHeaderText("Details for task: " + task.getTitle());
        if (todoController.isDarkTheme()) {
            dialog.getDialogPane().getStylesheets().remove("/com/example/todolist/lightmode.css");
            dialog.getDialogPane().getStylesheets().add("/com/example/todolist/darkmode.css");
        } else {
            dialog.getDialogPane().getStylesheets().remove("/com/example/todolist/darkmode.css");
            dialog.getDialogPane().getStylesheets().add("/com/example/todolist/lightmode.css");
        }
        ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(closeButtonType);
        Button closeButton = (Button) dialog.getDialogPane().lookupButton(closeButtonType);

        closeButton.getStyleClass().add("cancel-button");

        Label titleLabel = new Label("Title: " + task.getTitle());
        Label descriptionLabel = new Label("Description: " + task.getDescription());
        Label completedLabel = new Label("Completed: " + (task.isCompleted() ? "Yes" : "No"));

        VBox detailsBox = new VBox(10);
        detailsBox.setPadding(new Insets(20));
        detailsBox.getChildren().addAll(titleLabel, descriptionLabel, completedLabel);

        dialog.getDialogPane().setContent(detailsBox);

        dialog.showAndWait();
    }

    private boolean confirmDeletion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this task?");
        alert.setContentText("This action cannot be undone.");


        if (todoController.isDarkTheme()) {
            alert.getDialogPane().getStylesheets().add("/com/example/todolist/darkmode.css");
        } else {
            alert.getDialogPane().getStylesheets().add("/com/example/todolist/lightmode.css");
        }
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);


        okButton.getStyleClass().add("save-button");
        cancelButton.getStyleClass().add("cancel-button");


        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }
}