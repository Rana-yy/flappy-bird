package com.flappybird.game;

import com.flappybird.auth.User;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

// Handles the "Press Space to Start" menu screen displayed after a successful login
public class StartGameScene {

    // Callback interface to trigger the game transition when SPACE is pressed
    public interface StartCallback {
        void onStart();
    }

    // Builds the main menu interface layer over the background image asset
    public static StackPane createStartUI(User user, StartCallback onStart) {

        // Load the game background image
        Image backgroundImage = loadImage("flappybirdbg.png");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(360);
        backgroundView.setFitHeight(640);
        backgroundView.setPreserveRatio(false);

        Label welcomeLabel = new Label("Welcome, " + user.getUsername() + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 10px;");

        Label highScoreLabel = new Label("Your High Score: " + user.getScore());
        highScoreLabel.setFont(Font.font("Arial", 20));
        highScoreLabel.setStyle("-fx-text-fill: white; -fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 10px;");

        Label instructionLabel = new Label("Press SPACE to Start");
        instructionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        instructionLabel.setStyle("-fx-text-fill: white; -fx-background-color: rgba(0, 0, 0, 0.6); -fx-padding: 15px;");

        VBox contentBox = new VBox(30);
        contentBox.getChildren().addAll(
                welcomeLabel,
                highScoreLabel,
                instructionLabel
        );

        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPrefWidth(360);
        contentBox.setPrefHeight(640);

        // Create a StackPane with background image and content
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundView, contentBox);

        // Set up a listener to execute the callback when the player hits the spacebar
        root.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                onStart.onStart();
            }
        });

        // Enable focus traversability to ensure the screen instantly intercepts key events
        root.setFocusTraversable(true);
        root.requestFocus();

        return root;
    }

    // Utility method to safely load raw graphical files from the resource directory
    private static Image loadImage(String fileName) {
        String path = "/com/image/" + fileName;
        java.io.InputStream is = StartGameScene.class.getResourceAsStream(path);
        if (is == null) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }
        return new Image(is);
    }
}
