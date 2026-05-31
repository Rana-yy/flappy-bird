package com.flappybird.auth;


import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

// Builds the login and registration interface layout using JavaFX components
public class LoginScene {

    private User loggedInUser;
    private LoginCallback onLoginSuccess;

    // Callback interface to handle successful login transitions smoothly
    public interface LoginCallback {
        void onSuccess(User user);
    }

    // Creates and structures the login/register form view
    public static VBox createLoginUI(LoginCallback onLoginSuccess) {

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 40px;");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 40px;");

        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #0066cc;");

        // Registration
        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                resultLabel.setText("Please enter username and password");
                resultLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");
                return;
            }

            User user = new User(username, password, 0);
            UserManager.saveUser(user);

            resultLabel.setText("User registered! Now you can login.");
            resultLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: green;");
            usernameField.clear();
            passwordField.clear();
        });

        // Login
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                resultLabel.setText("Please enter username and password");
                resultLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");
                return;
            }

            User user = UserManager.login(username, password);

            if (user != null) {
                resultLabel.setText("Welcome " + user.getUsername() + "!");
                resultLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: green;");
                // Call the callback to switch to game scene
                onLoginSuccess.onSuccess(user);
            } else {
                resultLabel.setText("Wrong username/password");
                resultLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");
            }
        });

        VBox root = new VBox(15);
        root.getChildren().addAll(
                new Label("Flappy Bird - Login"),
                usernameField,
                passwordField,
                registerButton,
                loginButton,
                resultLabel
        );

        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 50px; -fx-background-color: cyan;");

        return root;
    }
}