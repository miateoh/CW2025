/**
 * Handles all JavaFX UI updates for the game board, falling brick,
 * score display, and notifications.
 *
 * Delegates all game logic to GameController and acts purely as the view layer.
 */

package com.tetris.ui.controllers;

import com.tetris.Main;
import com.tetris.sound.SoundManager;
import com.tetris.ui.views.ViewData;
import com.tetris.game.bricks.DownData;
import com.tetris.game.events.EventSource;
import com.tetris.game.events.EventType;
import com.tetris.game.events.MoveEvent;
import com.tetris.game.logic.InputEventListener;
import com.tetris.ui.views.GameOverPanel;
import com.tetris.ui.views.NotificationPanel;
import com.tetris.game.data.HighScoreManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.FadeTransition;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import javafx.scene.transform.Scale;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML private GridPane gamePanel;
    @FXML private GridPane brickPanel;

    @FXML private Pane holdPiecePane;
    @FXML private GridPane holdPieceGrid;

    @FXML private GridPane nextPiecePanel1;
    @FXML private GridPane nextPiecePanel2;
    @FXML private GridPane nextPiecePanel3;

    @FXML private Label scoreLabel;
    @FXML private Label levelLabel;

    @FXML private Pane pauseOverlay;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    @FXML private Button restartButton;
    @FXML private Button quitButton;

    @FXML private Group groupNotification;
    @FXML private GameOverPanel gameOverPanel;

    @FXML private Label comboLabel;
    @FXML private Label linesLabel;
    @FXML private Pane gameContainer;
    @FXML private StackPane rootPane;
    @FXML private Label timerLabel;
    @FXML private Button mainMenuButton;
    @FXML private Button settingsButton;

    private Rectangle[][][] nextPieceRectangles;
    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;

    private Timeline timeLine;
    private InputEventListener eventListener;

    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    private HighScoreManager highScoreManager;
    private double baseWidth = 500;
    private double baseHeight = 500;

    private boolean timeTrial = false;
    private Timeline timeTrialTimer;
    private int timeLeft = 60;
    private boolean isTimeTrial = false;
    private int remainingTime = 60;
    private Timeline timeTrialTimeline;

    private double sprintTime = 0.0;
    private Timeline sprintTimeline;

    private boolean isTimeTrialMode = false;
    private boolean isSprintMode = false;
    private int sprintTarget = 40; // default
    private int sprintLinesCleared = 0;

    private Timeline sprintTimer;
    private long sprintStartTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        gamePanel.setFocusTraversable(true);
        gamePanel.setOnKeyPressed(this::handleKeyPress);
        gamePanel.requestFocus();

        gameOverPanel.setVisible(false);
        pauseOverlay.setVisible(false);

        // Initialize high score manager
        highScoreManager = new HighScoreManager();

        // Wire up buttons
        resumeButton.setOnAction(e -> resumeGame());
        restartButton.setOnAction(e -> {
            SoundManager.play("restart");
            restartGame();
        });
        quitButton.setOnAction(e -> System.exit(0));
        mainMenuButton.setOnAction(e -> returnToMainMenu());

        settingsButton.setOnAction(e -> {
            SoundManager.play("menu");
            openSettings();
        });

        // Wire up game over panel buttons
        gameOverPanel.setOnRestart(this::restartGame);
        gameOverPanel.setOnResetScores(this::resetHighScores);
        gameOverPanel.setOnMainMenu(this::returnToMainMenu);

        addAutoScaling();

        initializeNextPiecePanels();
        initializeHoldPieceDisplay();

        Platform.runLater(() -> {
            StackPane.setAlignment(gameContainer, null);

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.fullScreenProperty().addListener((obs, oldV, newV) -> {
                Platform.runLater(this::updatePosition);
            });

            updatePosition();
        });
    }

    private void openSettings() {
        try {
            // load settings FXML
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/settings.fxml"));
            Parent root = loader.load();

            // pause BGM while in settings
            SoundManager.pauseBGM();

            // show settings screen
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root, 700, 700));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ===========================
    // KEY INPUT
    // ===========================

    private void handleKeyPress(KeyEvent keyEvent) {

        if (keyEvent.getCode() == KeyCode.P) {
            togglePause();
            keyEvent.consume();
            return;
        }

        if (keyEvent.getCode() == KeyCode.R) {
            restartGame();
            keyEvent.consume();
            return;
        }

        if (isPause.get() || isGameOver.get()) {
            keyEvent.consume();
            return;
        }

        switch (keyEvent.getCode()) {

            case LEFT, A -> {
                refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
            }

            case RIGHT, D -> {
                refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
            }

            case UP, W -> {
                SoundManager.play("rotate");
                refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
            }

            case DOWN, S -> {
                SoundManager.play("softDrop");
                moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
            }

            case SPACE -> {
                SoundManager.play("hardDrop");
                DownData data = eventListener.onHardDropEvent(
                        new MoveEvent(EventType.DOWN, EventSource.USER)
                );
                showScoreNotification(data);
                refreshBrick(data.getViewData());
            }

            case C -> {
                SoundManager.play("hold");
                refreshBrick(eventListener.onHoldEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
            }
        }
        keyEvent.consume();
    }

    // ===========================
    // PAUSE SYSTEM
    // ===========================

    @FXML
    private void togglePause() {
        if (isGameOver.get()) return;

        if (isPause.get()) resumeGame();
        else pauseGame();
    }

    private void returnToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource("main_menu.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root, 700, 700));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pauseGame() {
        isPause.set(true);
        SoundManager.play("pause");

        SoundManager.pauseBGM();

        if (timeLine != null) timeLine.pause();
        if (isTimeTrialMode && timeTrialTimeline != null)
            timeTrialTimeline.pause();

        if (isSprintMode && sprintTimer != null)
            sprintTimer.pause();

        pauseOverlay.setOpacity(0);
        pauseOverlay.setVisible(true);
        pauseOverlay.toFront();

        FadeTransition ft = new FadeTransition(Duration.millis(150), pauseOverlay);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    private void resumeGame() {
        isPause.set(false);
        SoundManager.play("unpause");

        SoundManager.resumeBGM();

        if (timeLine != null) timeLine.play();
        if (isTimeTrialMode && timeTrialTimeline != null)
            timeTrialTimeline.play();

        if (isSprintMode && sprintTimer != null)
            sprintTimer.play();

        pauseOverlay.setVisible(false);
        gamePanel.requestFocus();
    }

    private void restartGame() {
        // Stop fall timeline
        if (timeLine != null) timeLine.stop();

        // Stop time trial
        if (timeTrialTimeline != null) timeTrialTimeline.stop();

        // Stop sprint
        if (sprintTimer != null) sprintTimer.stop();

        SoundManager.stopBGM();
        SoundManager.playBGM("background_music.wav");

        isPause.set(false);
        isGameOver.set(false);

        pauseOverlay.setVisible(false);
        gameOverPanel.setVisible(false);

        // Clear high scores display when restarting
        gameOverPanel.clearHighScoresDisplay();

        // RESET THE GAME BOARD
        eventListener.createNewGame();
        refreshGameBackground(new int[0][0]);

        // ===== TIME TRIAL MODE RESET =====
        if (isTimeTrialMode) {
            remainingTime = 60;
            timerLabel.setVisible(true);
            timerLabel.setText("TIME: 60");
            startTimeTrialTimer();
        }

        // ===== SPRINT MODE RESET (THE FIX) =====
        if (isSprintMode) {

            sprintLinesCleared = 0;
            timerLabel.setVisible(true);
            timerLabel.setText("0.00");

            // reset time counters
            sprintTime = 0.0;

            // Stop old sprint timer if still running
            if (sprintTimer != null)
                sprintTimer.stop();

            // Delay start by 0.88 seconds
            PauseTransition delay = new PauseTransition(Duration.seconds(0.88));
            delay.setOnFinished(ev -> {
                sprintStartTime = System.currentTimeMillis();

                sprintTimer = new Timeline(new KeyFrame(
                        Duration.millis(10),
                        e -> updateSprintTimer()
                ));
                sprintTimer.setCycleCount(Timeline.INDEFINITE);
                sprintTimer.play();
            });

            delay.play();

            SoundManager.playBGM("background_music.wav");
            timeLine.play();
        }

        // Restart fall speed
        timeLine.play();
        gamePanel.requestFocus();
    }

    // ===========================
    // INITIALIZE GAME VIEW
    // ===========================

    public void initGameView(int[][] boardMatrix, ViewData brick) {

        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];

        for (int i = 2; i < boardMatrix.length; i++)
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                r.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = r;
                gamePanel.add(r, j, i - 2);
            }

        int[][] data = brick.getBrickData();
        rectangles = new Rectangle[data.length][data[0].length];

        for (int y = 0; y < data.length; y++)
            for (int x = 0; x < data[y].length; x++) {
                Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                r.setFill(getFillColor(data[y][x]));
                rectangles[y][x] = r;
                brickPanel.add(r, x, y);
            }

        updateBrickPanelPosition(brick);
        updateNextPiecesDisplay(brick);
        updateHoldPieceDisplay(brick);

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                t -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));

        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    // ===========================
    // HOLD PIECE
    // ===========================

    private void initializeHoldPieceDisplay() {
        holdPieceGrid.getChildren().clear();
    }

    private void updateHoldPieceDisplay(ViewData brick) {
        holdPieceGrid.getChildren().clear();

        int[][] hold = brick.getHoldBrickData();
        if (hold == null) return;

        int SIZE = 16;

        for (int y = 0; y < hold.length; y++)
            for (int x = 0; x < hold[y].length; x++)
                if (hold[y][x] != 0) {
                    Rectangle r = new Rectangle(SIZE, SIZE);
                    r.setFill(getFillColor(hold[y][x]));
                    r.setArcWidth(9);
                    r.setArcHeight(9);
                    holdPieceGrid.add(r, x, y);
                }
    }

    private void addAutoScaling() {
        rootPane.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            updatePosition();
        });

        rootPane.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            updatePosition();
        });

        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(this::updatePosition);
            }
        });

        gameContainer.getTransforms().clear();
    }

    private void updateScaleAndPosition(Scale scale) {
        double containerW = gameContainer.getPrefWidth();  // 500
        double containerH = gameContainer.getPrefHeight(); // 500

        double rootW = rootPane.getWidth();
        double rootH = rootPane.getHeight();

        if (rootW == 0 || rootH == 0) return;

        // Calculate scale factor
        double scaleFactor = Math.min(
                rootW / containerW,
                rootH / containerH
        );

        scale.setX(scaleFactor);
        scale.setY(scaleFactor);

        // Calculate scaled dimensions
        double scaledW = containerW * scaleFactor;
        double scaledH = containerH * scaleFactor;

        // Center the scaled container
        double x = (rootW - scaledW) / 2;
        double y = (rootH - scaledH) / 2;

        gameContainer.setLayoutX(x);
        gameContainer.setLayoutY(y);
    }

    private void updatePosition() {
        double containerW = 500;  // Base size
        double containerH = 500;

        double rootW = rootPane.getWidth();
        double rootH = rootPane.getHeight();

        if (rootW == 0 || rootH == 0) return;

        // Calculate scale
        double scaleX = rootW / containerW;
        double scaleY = rootH / containerH;
        double scale = Math.min(scaleX, scaleY);

        // Apply scale using setScaleX/Y instead of Transform
        gameContainer.setScaleX(scale);
        gameContainer.setScaleY(scale);

        // Calculate position - accounting for how setScale works
        // setScale scales from the CENTER of the node by default
        double scaledW = containerW * scale;
        double scaledH = containerH * scale;

        // Position so the CENTER of gameContainer is at the CENTER of rootPane
        double centerX = rootW / 2;
        double centerY = rootH / 2;

        // layoutX/Y positions the TOP-LEFT corner, so we need to offset
        // by half the UNSCALED container size (since scale is from center)
        gameContainer.setLayoutX(centerX - containerW / 2);
        gameContainer.setLayoutY(centerY - containerH / 2);
    }

    private void applyScaleAndCenter(Scale scale) {
        double containerWidth = gameContainer.getPrefWidth();   // 500
        double containerHeight = gameContainer.getPrefHeight(); // 500

        double availableWidth = rootPane.getWidth();
        double availableHeight = rootPane.getHeight();

        if (availableWidth == 0 || availableHeight == 0) return;

        // Calculate scale factor
        double scaleFactor = Math.min(
                availableWidth / containerWidth,
                availableHeight / containerHeight
        );

        scale.setX(scaleFactor);
        scale.setY(scaleFactor);

        // Calculate scaled dimensions
        double scaledW = containerWidth * scaleFactor;
        double scaledH = containerHeight * scaleFactor;

        // Center the container
        double x = (availableWidth - scaledW) / 2;
        double y = (availableHeight - scaledH) / 2;

        gameContainer.setLayoutX(x);
        gameContainer.setLayoutY(y);
    }

    // ===========================
    // NEXT PIECES (3 PREVIEW)
    // ===========================

    private void initializeNextPiecePanels() {
        nextPieceRectangles = new Rectangle[3][4][4];

        GridPane[] panels = {nextPiecePanel1, nextPiecePanel2, nextPiecePanel3};
        int SIZE = 16;

        for (int p = 0; p < 3; p++) {
            panels[p].getChildren().clear();

            for (int y = 0; y < 4; y++)
                for (int x = 0; x < 4; x++) {
                    Rectangle r = new Rectangle(SIZE, SIZE);
                    r.setFill(Color.TRANSPARENT);
                    r.setArcWidth(9);
                    r.setArcHeight(9);
                    nextPieceRectangles[p][y][x] = r;
                    panels[p].add(r, x, y);
                }
        }
    }

    private void updateNextPiecesDisplay(ViewData brick) {
        List<int[][]> next = brick.getNextBricksData();
        if (next == null || next.size() < 3) return;

        for (int i = 0; i < 3; i++)
            drawNextPiece(i, next.get(i));
    }

    private void drawNextPiece(int index, int[][] shape) {
        clearNextPiecePanel(index);

        if (shape == null) return;

        int offsetX = (4 - shape[0].length) / 2;
        int offsetY = (4 - shape.length) / 2;

        for (int y = 0; y < shape.length; y++)
            for (int x = 0; x < shape[y].length; x++)
                if (shape[y][x] != 0) {
                    Rectangle r = nextPieceRectangles[index][offsetY + y][offsetX + x];
                    setRectangleData(shape[y][x], r);
                }
    }

    private void clearNextPiecePanel(int index) {
        for (int y = 0; y < 4; y++)
            for (int x = 0; x < 4; x++)
                nextPieceRectangles[index][y][x].setFill(Color.TRANSPARENT);
    }

    // ===========================
    // FALLING BRICK + GHOST
    // ===========================

    private void refreshBrick(ViewData brick) {
        if (isPause.get() || isGameOver.get()) return;

        updateBrickPanelPosition(brick);

        int[][] shape = brick.getBrickData();

        for (int y = 0; y < shape.length; y++)
            for (int x = 0; x < shape[y].length; x++)
                setRectangleData(shape[y][x], rectangles[y][x]);

        drawGhost(brick);
        updateNextPiecesDisplay(brick);
        updateHoldPieceDisplay(brick);
    }

    private void updateBrickPanelPosition(ViewData brick) {
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(gamePanel.getLayoutY() - 42 + brick.getyPosition() * BRICK_SIZE);
    }

    private void drawGhost(ViewData brick) {

        gamePanel.getChildren().removeIf(
                n -> n instanceof Rectangle && ((Rectangle) n).getOpacity() == 0.3
        );

        if (isGameOver.get()) return;

        int[][] shape = brick.getBrickData();
        int ghostY = brick.getGhostLandingY();

        for (int sy = 0; sy < shape.length; sy++)
            for (int sx = 0; sx < shape[sy].length; sx++)
                if (shape[sy][sx] != 0) {

                    int boardX = brick.getxPosition() + sx;
                    int boardY = ghostY + sy - 2;

                    if (boardY >= 0 && boardX >= 0) {
                        Rectangle ghost = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                        ghost.setFill(Color.GRAY);
                        ghost.setOpacity(0.3);
                        ghost.setArcWidth(9);
                        ghost.setArcHeight(9);
                        gamePanel.add(ghost, boardX, boardY);
                    }
                }
    }

    // ===========================
    // BACKGROUND + SCORE
    // ===========================

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++)
            for (int j = 0; j < board[i].length; j++)
                setRectangleData(board[i][j], displayMatrix[i][j]);
    }

    private void setRectangleData(int val, Rectangle r) {
        r.setFill(getFillColor(val));
        r.setArcWidth(9);
        r.setArcHeight(9);
    }

    private Paint getFillColor(int i) {
        return switch (i) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.AQUA;
            case 2 -> Color.BLUEVIOLET;
            case 3 -> Color.DARKGREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.BEIGE;
            case 7 -> Color.BURLYWOOD;
            default -> Color.WHITE;
        };
    }

    // ===========================
    // GAME LOGIC HOOKS
    // ===========================

    private void moveDown(MoveEvent e) {
        DownData data = eventListener.onDownEvent(e);
        showScoreNotification(data); // FIXED: Use safe method
        refreshBrick(data.getViewData());
        gamePanel.requestFocus();
    }

    // NEW: Safe method to show score notifications
    private void showScoreNotification(DownData data) {
        if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0
                && groupNotification != null && groupNotification.getChildren() != null) {
            NotificationPanel n = new NotificationPanel("+" + data.getClearRow().getScoreBonus());
            SoundManager.play("lineClear");
            groupNotification.getChildren().add(n);
            n.showScore(groupNotification.getChildren());
        }
    }

    public void setEventListener(InputEventListener listener) {
        this.eventListener = listener;
    }

    public void bindScore(IntegerProperty scoreProperty) {
        scoreProperty.addListener((o, oldVal, newVal) ->
                scoreLabel.setText("SCORE: " + newVal)
        );
    }

    // ===========================
    // LEVEL SYSTEM
    // ===========================

    public void bindLevel(IntegerProperty levelProperty) {
        levelProperty.addListener((o, oldVal, newVal) ->
                levelLabel.setText(String.valueOf(newVal))
        );
        levelLabel.setText(String.valueOf(levelProperty.get()));
    }

    public void setGameSpeed(int speedMs) {
        if (timeLine != null) {
            boolean wasPlaying = timeLine.getStatus() == Timeline.Status.RUNNING;
            timeLine.stop();

            timeLine = new Timeline(new KeyFrame(
                    Duration.millis(speedMs),
                    t -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
            ));

            timeLine.setCycleCount(Timeline.INDEFINITE);

            if (wasPlaying && !isPause.get() && !isGameOver.get()) {
                timeLine.play();
            }
        }
    }

    public void showLevelUpNotification(int newLevel) {
        // FIXED: Safe notification
        if (groupNotification != null && groupNotification.getChildren() != null) {
            NotificationPanel notification = new NotificationPanel("LEVEL " + newLevel + "!");
            SoundManager.play("levelUp");
            groupNotification.getChildren().add(notification);
            notification.setLayoutY(100);
            notification.showScore(groupNotification.getChildren());
        }
    }

    // ===========================
    // GAME OVER
    // ===========================

    public void gameOver() {

        SoundManager.stopBGM();
        SoundManager.play("gameover");

        // Stop timers
        if (timeLine != null) timeLine.stop();
        if (timeTrialTimeline != null) timeTrialTimeline.stop();
        if (sprintTimeline != null) sprintTimeline.stop();

        isGameOver.set(true);

        // ---------- SPRINT MODE CUSTOM END SCREEN ----------
        // -------- SPRINT MODE END --------
        if (isSprintMode) {

            if (sprintTimer != null) sprintTimer.stop();

            double finalTime = sprintTime;   // Now contains the correct measured time

            gameOverPanel.hideGameOverTitle();
            int finalScore = Integer.parseInt(scoreLabel.getText().replace("SCORE: ", "").trim());
            gameOverPanel.showSprintResult(sprintTime, sprintTarget, finalScore);

            gameOverPanel.setVisible(true);
            gameOverPanel.toFront();
            return;
        }
        // ---------- NORMAL GAME OVER (Marathon + Time Trial) ----------
        String scoreText = scoreLabel.getText().replace("SCORE: ", "");
        int finalScore;
        try {
            finalScore = Integer.parseInt(scoreText.trim());
        } catch (NumberFormatException e) {
            finalScore = 0;
        }

        highScoreManager.addScore(finalScore);
        gameOverPanel.updateHighScores(highScoreManager.getHighScores(), finalScore);

        gameOverPanel.setVisible(true);
        gameOverPanel.toFront();
    }

    // ===========================
    // HIGH SCORE RESET
    // ===========================

    private void resetHighScores() {
        // Show confirmation notification if available
        if (groupNotification != null && groupNotification.getChildren() != null) {
            NotificationPanel resetNotification = new NotificationPanel("High Scores Reset!");
            resetNotification.setStyle("-fx-text-fill: #FF6666;");
            groupNotification.getChildren().add(resetNotification);
            resetNotification.showScore(groupNotification.getChildren());
        } else {
            System.out.println("Note: groupNotification is not available for showing reset notification");
        }

        // Reset the scores
        highScoreManager.resetHighScores();

        // Get current score from label
        String scoreText = scoreLabel.getText().replace("SCORE: ", "");
        int finalScore;
        try {
            finalScore = Integer.parseInt(scoreText.trim());
        } catch (NumberFormatException e) {
            finalScore = 0;
        }

        // Update display with empty scores
        gameOverPanel.updateHighScores(highScoreManager.getHighScores(), finalScore);

        System.out.println("High scores have been reset.");
    }

    // ===========================
    // COMBO SYSTEM
    // ===========================

    public void bindCombo(IntegerProperty comboProperty) {
        comboProperty.addListener((o, oldVal, newVal) -> {
            if (newVal.intValue() > 1) {
                comboLabel.setText("COMBO: " + newVal + "x");
                comboLabel.setVisible(true);
            } else {
                comboLabel.setVisible(false);
            }
        });
        comboLabel.setVisible(false);
    }

    public void showComboNotification(int comboCount, int bonusPoints) {
        // FIXED: Safe notification
        if (groupNotification != null && groupNotification.getChildren() != null) {

            SoundManager.play("combo");

            NotificationPanel notification = new NotificationPanel(
                    "COMBO x" + comboCount + "! (+" + bonusPoints + ")"
            );
            notification.setTranslateY(-150);
            groupNotification.getChildren().add(notification);
            notification.showScore(groupNotification.getChildren());
        }
    }

    // ===========================
    // LINES DISPLAY
    // ===========================

    public void bindLines(IntegerProperty linesProperty) {
        linesProperty.addListener((o, oldVal, newVal) -> {
            linesLabel.setText(String.valueOf(newVal));

            if (isSprintMode && newVal.intValue() >= sprintTarget) {
                sprintCompleted();
            }
        });

        linesLabel.setText(String.valueOf(linesProperty.get()));
    }

    private void sprintCompleted() {

        if (sprintTimeline != null) sprintTimeline.stop();
        isGameOver.set(true);

        // WAIT 1 FRAME so score label updates from the final line clear
        Platform.runLater(() -> {

            // Now the score label contains the correct updated score
            gameOver();
        });
    }

    private void finishSprintMode() {
        isGameOver.set(true);

        if (timeLine != null) timeLine.stop();
        if (sprintTimer != null) sprintTimer.stop();

        long elapsed = System.currentTimeMillis() - sprintStartTime;
        double seconds = elapsed / 1000.0;

        timerLabel.setText(String.format("%.2f", seconds));

        gameOver();
    }

    public void enableTimeTrialMode(boolean enabled) {
        this.isTimeTrial = enabled;

        if (enabled) {
            timerLabel.setVisible(true);
            timerLabel.setText("TIME: 60");
            remainingTime = 60;
            startTimeTrialTimer();
        } else {
            timerLabel.setVisible(false);
        }
    }

    public void setGameMode(boolean timeTrial, boolean sprint, int sprintTarget) {
        this.isTimeTrialMode = timeTrial;
        this.isSprintMode = sprint;
        this.sprintTarget = sprintTarget;

        if (isTimeTrialMode) {
            remainingTime = 60;
            timerLabel.setVisible(true);
            timerLabel.setText("TIME: 60");
            startTimeTrialTimer();
        }

        if (isSprintMode) {
            enableSprintMode(sprintTarget);
        }
    }

    public void enableSprintMode(int targetLines) {
        this.isSprintMode = true;
        this.sprintTarget = targetLines;
        this.sprintLinesCleared = 0;

        // Start timer
        sprintStartTime = System.currentTimeMillis();

        sprintTimer = new Timeline(
                new KeyFrame(Duration.millis(100), e -> updateSprintTimer())
        );
        sprintTimer.setCycleCount(Timeline.INDEFINITE);
        sprintTimer.play();

        timerLabel.setVisible(true);
        timerLabel.setText("0.00");
    }

    private void updateSprintTimer() {
        long elapsed = System.currentTimeMillis() - sprintStartTime;
        sprintTime = elapsed / 1000.0;       // <— Now sprintTime will store real time

        timerLabel.setText(String.format("%.2f", sprintTime));
    }

    public void initTimeTrial(boolean isEnabled) {
        this.timeTrial = isEnabled;

        if (!isEnabled) return;

        timerLabel.setVisible(true);
        timerLabel.setText("60");

        timeTrialTimer = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    timeLeft--;
                    timerLabel.setText(String.valueOf(timeLeft));
                    if (timeLeft <= 0) {
                        gameOver(); // normal gameOver call — reuses scoreboard
                    }
                })
        );

        timeTrialTimer.setCycleCount(60);
        timeTrialTimer.play();
    }

    private void startTimeTrialTimer() {
        if (timeTrialTimeline != null) {
            timeTrialTimeline.stop();
        }

        timeTrialTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    remainingTime--;
                    timerLabel.setText("TIME: " + remainingTime);

                    if (remainingTime <= 0) {
                        timeTrialTimeline.stop();
                        endGameDueToTime();
                    }
                })
        );

        timeTrialTimeline.setCycleCount(Timeline.INDEFINITE);
        timeTrialTimeline.play();
    }

    private void endGameDueToTime() {
        isGameOver.set(true);

        // Stop timelines safely
        if (timeLine != null) timeLine.stop();
        if (timeTrialTimeline != null) timeTrialTimeline.stop();

        // Trigger actual game over logic
        gameOver();
    }

    private void startSprintTimer() {
        if (sprintTimeline != null)
            sprintTimeline.stop();

        sprintTimeline = new Timeline(new KeyFrame(
                Duration.millis(10),   // update every 0.01 sec (10 ms)
                e -> {
                    sprintTime += 0.01;
                    timerLabel.setText(String.format("TIME: %.2f", sprintTime));
                }
        ));

        sprintTimeline.setCycleCount(Timeline.INDEFINITE);
        sprintTimeline.play();
    }
}



