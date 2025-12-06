/**
 * Handles all JavaFX UI updates for the game board, falling brick,
 * score display, and notifications.
 *
 * Delegates all game logic to GameController and acts purely as the view layer.
 */

package com.tetris.ui.controllers;

import com.tetris.ui.views.ViewData;
import com.tetris.game.bricks.DownData;
import com.tetris.game.events.EventSource;
import com.tetris.game.events.EventType;
import com.tetris.game.events.MoveEvent;
import com.tetris.game.logic.InputEventListener;
import com.tetris.ui.views.GameOverPanel;
import com.tetris.ui.views.NotificationPanel;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.FadeTransition;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Group;
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

    @FXML private Pane pauseOverlay;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    @FXML private Button restartButton;
    @FXML private Button quitButton;

    @FXML private Group groupNotification;
    @FXML private GameOverPanel gameOverPanel;

    private Rectangle[][][] nextPieceRectangles;
    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;

    private Timeline timeLine;
    private InputEventListener eventListener;

    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        gamePanel.setFocusTraversable(true);
        gamePanel.setOnKeyPressed(this::handleKeyPress);
        gamePanel.requestFocus();

        gameOverPanel.setVisible(false);
        pauseOverlay.setVisible(false);

        // Wire up buttons
        resumeButton.setOnAction(e -> resumeGame());
        restartButton.setOnAction(e -> restartGame());
        quitButton.setOnAction(e -> System.exit(0));

        // NEW: Wire up game over panel restart button
        gameOverPanel.setOnRestart(this::restartGame);

        initializeNextPiecePanels();
        initializeHoldPieceDisplay();
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

        // NEW: R key works even during game over
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

            case LEFT, A ->
                    refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));

            case RIGHT, D ->
                    refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));

            case UP, W ->
                    refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));

            case DOWN, S ->
                    moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));

            case SPACE -> {
                DownData data = eventListener.onHardDropEvent(
                        new MoveEvent(EventType.DOWN, EventSource.USER)
                );

                if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
                    NotificationPanel n = new NotificationPanel("+" + data.getClearRow().getScoreBonus());
                    groupNotification.getChildren().add(n);
                    n.showScore(groupNotification.getChildren());
                }

                refreshBrick(data.getViewData());
            }

            case C ->
                    refreshBrick(eventListener.onHoldEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
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

    private void pauseGame() {
        isPause.set(true);
        timeLine.pause();

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
        timeLine.play();
        pauseOverlay.setVisible(false);
        gamePanel.requestFocus();
    }

    private void restartGame() {
        if (timeLine != null) timeLine.stop();

        isPause.set(false);
        isGameOver.set(false);

        pauseOverlay.setVisible(false);
        gameOverPanel.setVisible(false);

        eventListener.createNewGame();
        refreshGameBackground(new int[0][0]);

        gamePanel.requestFocus();
        timeLine.play();
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

        if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel n = new NotificationPanel("+" + data.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(n);
            n.showScore(groupNotification.getChildren());
        }

        refreshBrick(data.getViewData());
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener listener) {
        this.eventListener = listener;
    }

    public void bindScore(IntegerProperty scoreProperty) {
        scoreProperty.addListener((o, oldVal, newVal) ->
                scoreLabel.setText("SCORE: " + newVal)
        );
    }

    public void gameOver() {
        timeLine.stop();
        isGameOver.set(true);
        gameOverPanel.setVisible(true);
        gameOverPanel.toFront();
    }
}




