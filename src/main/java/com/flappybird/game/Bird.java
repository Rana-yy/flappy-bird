package com.flappybird.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

// Represents the player-controlled bird, handling its physics and rendering
public class Bird {

    // Screen boundaries and dimensions for the bird entity
    public static final int START_X = GameConfig.BOARD_WIDTH / 8;
    public static final int START_Y = GameConfig.BOARD_HEIGHT / 2;
    public static final int WIDTH   = 34;
    public static final int HEIGHT  = 24;

    // Position and movement variables
    public double x, y;
    public double velocityY;
    private final Image img;

    // Physics constants for gravity and jumping
    private static final double GRAVITY     = 1;
    private static final double JUMP_FORCE  = -9;


    public Bird(Image img) {
        this.img = img;
        reset();
    }

    // Resets the bird to its starting position when the game restarts
    public void reset() {
        x = START_X;
        y = START_Y;
        velocityY = 0;
    }

    // Updates the bird's position by applying gravity in each frame
    public void update() {
        velocityY += GRAVITY;
        y += velocityY;
        y = Math.max(y, 0); //Prevent the bird from flying off the top boundary of the screen
    }

    // Applies an upward impulse force when the player presses Space
    public void jump() {
        velocityY = JUMP_FORCE;
    }

    // Draws the bird's PNG asset onto the active game canvas
    public void draw(GraphicsContext gc) {
        gc.drawImage(img, x, y, WIDTH, HEIGHT);
   }

    // Returns the current X coordinate for collision handling
    public double getX() {

        return 0;
    }

    //Returns the current Y coordinate for collision handling
    public double getY() {

        return 0;
    }
}