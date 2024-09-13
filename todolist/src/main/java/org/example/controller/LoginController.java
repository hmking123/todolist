package org.example.controller;

import org.example.model.User;
import org.example.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private AuthService authService;

    public LoginController() {
        this.authService = new AuthService();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = authService.authenticate(username, password);
        if (user != null) {
            openTodoScreen(user);
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authService.register(username, password)) {
            showAlert("Registration Successful", "You can now log in with your credentials.");
        } else {
            showAlert("Registration Failed", "Username already exists.");
        }
    }

    private void openTodoScreen(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/todo.fxml"));
            Parent root = loader.load();

            TodoController todoController = loader.getController();
            todoController.initData(user);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load Todo screen.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
