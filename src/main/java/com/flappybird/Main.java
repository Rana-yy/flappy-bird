package com.flappybird;

import com.flappybird.game.StartGameScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.flappybird.auth.LoginScene;
import com.flappybird.auth.User;
import com.flappybird.auth.UserManager;
import com.flappybird.game.GameScene;
import com.flappybird.game.GameConfig;

public class Main extends Application {

    private Stage stage;
    private User loggedInUser;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        stage.setTitle("Flappy Bird Demo");
        stage.setResizable(false);

        // Loads the login screen first when the application starts
        showLoginScene();

        stage.show();
    }

    //Displays the login UI and transitions to the start screen upon successful login
    private void showLoginScene() {
        BorderPane root = new BorderPane();
        root.setCenter(LoginScene.createLoginUI(user -> {
            loggedInUser = user;
            showStartScreen();
        }));

        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
    }

    //Displays the "Press Space to Start" main menu screen
    private void showStartScreen() {
        BorderPane root = new BorderPane();
        root.setCenter(StartGameScene.createStartUI(loggedInUser, () -> {
            showGameScene();
        }));

        Scene scene = new Scene(root, 360, 640);
        stage.setScene(scene);
        // Request focus so the start screen can capture the SPACE key press event immediately
        if (root.getCenter() != null) {
            root.getCenter().requestFocus();
        }
    }

    /**
     * Displays the game scene after login and start screen.
     */
    // Sets up the active gameplay loop and handles high score saving logic
    private void showGameScene() {
        GameScene gameRoot = new GameScene((finalScore) -> {
            // If the player achieves a new high score, update local storage and reload data
            if (finalScore > loggedInUser.getScore()) {
                loggedInUser.setScore(finalScore);
                UserManager.saveUser(loggedInUser);
                // reload user to reflect persisted data when returning to start
                User reloaded = UserManager.login(loggedInUser.getUsername(), loggedInUser.getPassword());
                if (reloaded != null) {
                    loggedInUser = reloaded;
                }
            }
        });
        
        Scene scene = new Scene(gameRoot, GameConfig.BOARD_WIDTH, GameConfig.BOARD_HEIGHT);

        stage.setScene(scene);
        stage.sizeToScene();
    }

    /**
     * Saves the final score and returns to start screen.
     */
    // Updates the high score if necessary and returns the user to the start screen
    private void saveAndReturnToStart(int finalScore) {
        // Update the logged-in user's score if it's higher than the previous high score
        if (finalScore > loggedInUser.getScore()) {
            loggedInUser.setScore(finalScore);
        }
        
        // Save the user with the updated high score
        UserManager.saveUser(loggedInUser);
        // Reload user data to guarantee the updated high score is correctly displayed
        User reloaded = UserManager.login(loggedInUser.getUsername(), loggedInUser.getPassword());
        if (reloaded != null) {
            loggedInUser = reloaded;
        }
        
        // Returns to start screen
        showStartScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}