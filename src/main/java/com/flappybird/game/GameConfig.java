package com.flappybird.game;

// Stores global constant values used throughout the game to avoid hardcoded values
public final class GameConfig {

    // Private constructor to prevent instantiation
    private GameConfig() {}

    // Screen dimensions
    public static final int BOARD_WIDTH  = 360;
    public static final int BOARD_HEIGHT = 640;

    // Physics constants
    public static final double VELOCITY_X = -4; // Horizontal movement speed for pipes

    //Pipe generation settings
    public static final long PIPE_INTERVAL_NS = 1_500_000_000L; // Pipe spawn interval (1.5 sec, nanoseconds)
    public static final int  OPENING_SPACE    = BOARD_HEIGHT / 4; // The gap between the upper and lower pipes

    //Target frame rate
    public static final int TARGET_FPS = 60; // Reference FPS for AnimationTimer delta control
}