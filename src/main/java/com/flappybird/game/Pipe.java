package com.flappybird.game;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// Represents a single pipe obstacle (either top or bottom) in the game
public class Pipe {

    // Dimensions for the pipe asset
    public static final int WIDTH  = 64;
    public static final int HEIGHT = 512;

    // Position, scoring state, and visual asset variables
    public double x, y;
    public boolean passed;
    private final Image img;

    public Pipe(Image img, double x, double y) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.passed = false;
    }

    // Updates the pipe's horizontal position, moving it left across the screen
    public void update(double velocityX) {

        x += velocityX;
    }

    // Draws the pipe's PNG asset onto the active game canvas
    public void draw(GraphicsContext gc) {
        gc.drawImage(img, x, y, WIDTH, HEIGHT);
    }

    // Checks if the pipe has completely moved off the left side of the screen
    public boolean isOffScreen() {
        return x + WIDTH < 0;
    }
}