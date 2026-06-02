package com.flappybird.game;

import javafx.scene.canvas.GraphicsContext; // To draw the score on the screen
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

// Tracks the player's real-time score and handles rendering the score UI
public class ScoreManager {

    // Score is tracked as a double because passing each pipe object adds 0.5 points (top + bottom = 1 point)
    private double score;

    //Visual settings for the score text display
    private static final String FONT_NAME = "Arial";
    private static final int FONT_SIZE = 32;
    private static final int TEXT_X = 10;
    private static final int TEXT_Y = 35;

    public ScoreManager() {

        score = 0;
    }

    // Resets the score counter when restarting the game
    public void reset() {

        score = 0;
    }

    // Validates if the bird safely cleared a pipe obstacle and increments the score
    public void checkAndScore(Bird bird, Pipe pipe) {

        if (!pipe.passed && bird.x > pipe.x + Pipe.WIDTH) {
            score += 0.5;
            pipe.passed = true;
        }
    }

    public int getScore() {

        return (int) score;
    }

    // Renders the active score or a "Game Over" message directly on the game canvas
    public void draw(GraphicsContext gc, boolean gameOver) {
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(FONT_NAME, FONT_SIZE));

        if (gameOver) {

            gc.fillText("Game Over: " + getScore(), TEXT_X, TEXT_Y);

        } else {

            gc.fillText(String.valueOf(getScore()), TEXT_X, TEXT_Y);
        }
    }
}