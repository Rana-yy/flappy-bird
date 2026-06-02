package com.flappybird.game;

import java.util.List; // To work with the pipe list.

// Manages all collision detection logic for the bird, pipes, and screen boundaries
public class CollisionManager {

    // Private constructor to prevent instantiation
    private CollisionManager() {}

    // Checks if the bird has collided with any of the active pipes in the game
    public static boolean checkPipeCollision(Bird bird, List<Pipe> pipes) {
        for (Pipe pipe : pipes) {
            if (intersects(bird, pipe)) {
                return true;             // Returns true immediately upon the first collision.
            }
        }
        return false; // Returns false if there is no collision with any pipe.
    }

    // Checks if the bird has hit the ground boundary (the ceiling limit is handled in Bird.update)
    public static boolean checkBoundaryCollision(Bird bird) {
        // The game ends if the bird's bottom edge goes below the screen.
        return bird.y + Bird.HEIGHT > GameConfig.BOARD_HEIGHT;
        // Note: The upper limit is restricted by Math.max within Bird.update().
    }

    // Determines if the bird and a pipe overlap using the Axis-Aligned Bounding Box (AABB) algorithm
    private static boolean intersects(Bird bird, Pipe pipe) {
        return bird.x < pipe.x + Pipe.WIDTH  &&
                bird.x + Bird.WIDTH > pipe.x  &&
                bird.y < pipe.y + Pipe.HEIGHT  &&
                bird.y + Bird.HEIGHT > pipe.y;

        // If all four conditions are true, it means the rectangles overlap.
    }
}