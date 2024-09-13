package org.example.controller;

import org.example.model.Task;
import org.example.model.User;
import org.example.service.TodoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class TodoController {
    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    @FXML
    private TableColumn<Task, Boolean> completedColumn;
    @FXML
    private TextField newTaskField;

    private User currentUser;
    private TodoService todoService;
    private ObservableList<Task> tasks;

    public TodoController() {
        this.todoService = new TodoService();
        this.tasks = FXCollections.observableArrayList();
    }

    public void initData(User user) {
        this.currentUser = user;
        loadTasks();
    }

    @FXML
    private void initialize() {
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        completedColumn.setCellValueFactory(new PropertyValueFactory<>("completed"));
        taskTable.setItems(tasks);
    }

    private void loadTasks() {
        tasks.clear();
        tasks.addAll(todoService.getUserTasks(currentUser));
    }

    @FXML
    private void handleAddTask() {
        String description = newTaskField.getText().trim();
        if (!description.isEmpty()) {
            todoService.addTask(currentUser, description);
            newTaskField.clear();
            loadTasks();
        }
    }

    @FXML
    private void handleMarkCompleted() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            todoService.markTaskAsCompleted(selectedTask.getId());
            loadTasks();
        }
    }

    @FXML
    private void handleDeleteTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            todoService.deleteTask(selectedTask.getId());
            loadTasks();
        }
    }

    @FXML
    private void handleUpdateTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            String newDescription = showEditDialog(selectedTask.getDescription());
            if (newDescription != null && !newDescription.isEmpty()) {
                todoService.updateTaskDescription(selectedTask.getId(), newDescription);
                loadTasks();
            }
        }
    }

    private String showEditDialog(String currentDescription) {
        TextInputDialog dialog = new TextInputDialog(currentDescription);
        dialog.setTitle("Edit Task");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new description:");

        return dialog.showAndWait().orElse(null);
    }
}