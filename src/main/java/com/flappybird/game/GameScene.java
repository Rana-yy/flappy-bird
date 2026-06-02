package com.flappybird.game;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;  // For dynamic pipe list
import java.util.Iterator;   // To safely remove elements from the list
import java.util.List;

// Coordinates gameplay mechanics, updates entity states, and renders objects on the canvas
public class GameScene extends StackPane {

    //CALLBACK FOR GAME END
    public interface GameEndCallback {
        void onGameEnd(int finalScore);
    }

    // Canvas rendering components
    private final Canvas canvas;
    private final GraphicsContext gc;

    //Images
    private final Image backgroundImg;
    public final Image bgImage;
    private final Image topPipeImg;
    private final Image bottomPipeImg;

    //Game objects
    private final Bird bird;
    private final List<Pipe> pipes;
    private final ScoreManager scoreManager;

    //Game loop
    private AnimationTimer gameLoop;
    private long lastPipeTime = 0;

    // Game state
    private boolean gameOver = false;
    private GameEndCallback gameEndCallback;

    public GameScene() {

        this(null); // Call the overloaded constructor with null callback
    }

    public GameScene(GameEndCallback callback) {
        this.gameEndCallback = callback;

        // Load all visual game assets from resources
        backgroundImg = loadImage("flappybirdbg.png");
        bgImage = backgroundImg; // Expose background for other scenes
        Image birdImg = loadImage("flappybird.png");
        topPipeImg = loadImage("toppipe.png");
        bottomPipeImg = loadImage("bottompipe.png");

        //Creates canvas
        canvas = new Canvas(GameConfig.BOARD_WIDTH, GameConfig.BOARD_HEIGHT);
        gc = canvas.getGraphicsContext2D(); // Gets the drawing context.
        getChildren().add(canvas);              // Added to layout

        // Creates game objects
        bird = new Bird(birdImg);
        pipes = new ArrayList<>();
        scoreManager = new ScoreManager();

        //Configure input handling and core loops
        setupInput(); // Space key listener is set up.
        buildGameLoop(); // AnimationTimer is defined.
        gameLoop.start();

        // To capture keyboard events, focus is placed on the canvas.
        canvas.setFocusTraversable(true);
        canvas.requestFocus();
    }

    // Safely reads raw image assets from the project classpath
    private Image loadImage(String fileName) {

        String path = "/com/image/" + fileName;
        java.io.InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            // Provide a clear runtime error if a resource is missing to help debugging.
            throw new IllegalArgumentException("Resource not found: " + path);
        }
        return new Image(is);
    }

    // Binds keyboard events to make the bird jump or trigger an in-place restart
    private void setupInput() {
        canvas.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.UP) {
                if (!gameOver) {
                    bird.jump();
                } else {
                    // If the game is over and SPACE or UP is pressed, restart the game in-place.
                    restartGame();
                }
            }
        });
    }

    /**
     * Gets the current score.
     */
    public int getScore() {

        return scoreManager.getScore();
    }

    // Initializes the ~60 FPS update loop that periodically triggers entity updates and canvas redraws
    private void buildGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Generate a new pipe obstacle pair every 1.5 seconds
                if (lastPipeTime == 0 || now - lastPipeTime >= GameConfig.PIPE_INTERVAL_NS) {
                    spawnPipes();        // A new pair of pipes is added.
                    lastPipeTime = now;  // The timer is updated
                }
                update();
                render();
            }
        };
    }

    // Calculates positions and spawns a aligned top and bottom pipe pair with a passable gap
    private void spawnPipes() {

        int randomY = (int) (-Pipe.HEIGHT / 4 - Math.random() * (Pipe.HEIGHT / 2));


        Pipe top = new Pipe(topPipeImg, GameConfig.BOARD_WIDTH, randomY);
        pipes.add(top);

        Pipe bottom = new Pipe(bottomPipeImg, GameConfig.BOARD_WIDTH, randomY + Pipe.HEIGHT + GameConfig.OPENING_SPACE);
        pipes.add(bottom);
    }

    // Runs physics ticks, handles score increments, and evaluates game-over states
    private void update() {
        // Bird's physics update (gravity + position)
        bird.update();

        // Iterate through active pipes to adjust positioning and safely clear off-screen obstacles
        Iterator<Pipe> it = pipes.iterator();
        while (it.hasNext()) {
            Pipe pipe = it.next();
            pipe.update(GameConfig.VELOCITY_X);

            scoreManager.checkAndScore(bird, pipe);

            if (pipe.isOffScreen()) {
                it.remove();
            }
        }

        // Process boundary or obstacle collision checks to trigger game end states
        if (CollisionManager.checkPipeCollision(bird, pipes) ||
                CollisionManager.checkBoundaryCollision(bird)) {
            gameOver = true;
            gameLoop.stop();
            // Call the end callback to allow external code to persist the final score
            // without changing the current scene.
            if (gameEndCallback != null) {
                int finalScore = scoreManager.getScore();
                gameEndCallback.onGameEnd(finalScore);
            }
        }
    }

    // Clears and completely redraws all game objects onto the canvas surface layered by depth
    private void render() {

        gc.drawImage(backgroundImg, 0, 0, GameConfig.BOARD_WIDTH, GameConfig.BOARD_HEIGHT);


        for (Pipe pipe : pipes) {
            pipe.draw(gc);
        }

        bird.draw(gc);
        scoreManager.draw(gc, gameOver);
    }

    // Resets entity data structures to perform a seamless restart
    private void restartGame() {
        bird.reset();
        pipes.clear();
        scoreManager.reset();
        gameOver = false;
        lastPipeTime = 0;
        gameLoop.start();
        canvas.requestFocus();
    }
}